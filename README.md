# Sleuth, Rabbitmq, Zipkin

With our forces combined, we can trace all the things.


## Running Zipkin
- need to specify the `RABBIT_URI` or zipkin will not get _anything at all_.
```
java -jar zipkin-server-2.15.0-exec.jar --RABBIT_URI=amqp://guest:guest@localhost:5672
```

## Notes
- Use `RestTemplateBuilder` instead of `new RestTemplate()` to get trace ids populating properly

