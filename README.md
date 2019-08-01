# Sleuth, Rabbitmq, Zipkin

With our forces combined, we can trace all the things.


## Running Zipkin
- need to specify the `RABBIT_URI` or zipkin will not get _anything at all_.
```
java -jar zipkin-server-2.15.0-exec.jar --RABBIT_URI=amqp://guest:guest@localhost:5672
```

## Goal
Imagine we have multiple microservices in different stores (different `store-id`) all feeding to the same log aggregate. 
It would be nice to filter by `store-id` when tracing logs.

Let's say we have an operation that spans across multiple microservices.
We want to have a `store-id` appear in the logs for that operation,
and it should work over REST calls and messaging (AMQP - RabbitMQ) systems.
 
We will do this by adding [Baggage](https://cloud.spring.io/spring-cloud-sleuth/spring-cloud-sleuth.html#_propagating_span_context) to a trace.


## Learnings
- Use `RestTemplateBuilder` instead of `new RestTemplate()` to get trace ids populating properly
- `spring.sleuth.baggage-keys` must be set with the baggage key we want to use (e.g. `store-id`) for it to be sent by sleuth
- `spring.sleuth.log.slf4j.whitelisted-mdc-keys` must be set so we can display the baggage key in the log
- Override `logging.pattern.level` to set the `store-id` in the logs. The default pattern is specified
 [here](https://cloud.spring.io/spring-cloud-sleuth/spring-cloud-sleuth.html#_features) 
- use `ExtraFieldPropagation` to set a baggage key-value pair 

- When adding logs to a REST call, the span has already been created and 'decorated' with ids.
  As such, if you try to set a baggage key-value in this example:
    ```
        @PostMapping("/hello")
        public void createGreeting() {
            ExtraFieldPropagation.set("store-id","123");
            log.info("POST Hello");
            messagingService.sendMessage("Hello, world");
        }
    ``` 
    you would not see the `store-id` in that log. The span has already been created/decorated and it will not pick up the 
    changes until the next time it would need to decorate (e.g. REST/Message call).
    
    A ~~hack~~ workaround for this is for App1 to provide a `CurrentTraceContext.ScopeDecorator` bean 
    that sets the `ExtraFieldPropagation` and `MDC`' with a `store-id`. 
    Since App1 is the entrypoint, it makes sense for it to initialize the trace with a `store-id`.  
    It is a random value here, but you can imagine it being a constant/configuration.

- Sleuth uses a `Slf4jScopeDecorator` to add trace and span ids of a span to the slf4j log in the `decorateScope` method.
  It does this by using the `MDC` class to set values for the keys specified in the log format (`logging.pattern.level`)
  When the current span is null, these MDC fields get wiped.
  A new span is created and `Slf4jScopeDecorator` repopulates the `MDC` fields (traceId, spanId, etc) 
  with their previous values using `ThreadContextCurrentTraceContextScope`. 
  
  However, it __does not__ repopulate any of the Sleuth Baggage or Propagation keys.

  The log in App2 after the RestTemplate call suffers from this and would be missing the `store-id`.

  ```
    @StreamListener(CustomProcessor.CHANNEL)
    public void handleMessage(Message<String> message) {
        log.info("Received message: {}", message.getPayload());
        String response = restTemplateBuilder.build().getForObject("http://localhost:8080/hello", String.class);
        log.info("Made api call and received {}", response); // <--- no store-id
    }
  ```

  A ~~hack~~ workaround for this is to create a `CurrentTraceContext.ScopeDecorator` that saves
  the `MDC`'s `store-id` before returning a `CurrentTraceContext.Scope` that repopulates it, 
  much like how `Slf4jScopeDecorator.ThreadContextCurrentTraceContextScope` works for trace/span ids.
