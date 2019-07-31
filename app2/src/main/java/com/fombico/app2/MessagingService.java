package com.fombico.app2;

import brave.propagation.ExtraFieldPropagation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@EnableBinding({CustomProcessor.class})
public class MessagingService {

    RestTemplateBuilder restTemplateBuilder;

    @StreamListener(CustomProcessor.CHANNEL)
    public void handleMessage(Message<String> message) {
        String storeId = ExtraFieldPropagation.get("store-id");

        log.info("Received message: {} {}", message.getPayload(), storeId);
        String response = restTemplateBuilder.build().getForObject("http://localhost:8080/hello", String.class);

        storeId = ExtraFieldPropagation.get("store-id");
        log.info("Made api call and received {}, {}", response, storeId);
    }
}

