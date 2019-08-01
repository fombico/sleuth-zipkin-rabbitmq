package com.fombico.app2;

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

    private RestTemplateBuilder restTemplateBuilder;

    @StreamListener(CustomProcessor.CHANNEL)
    public void handleMessage(Message<String> message) {
        log.info("Received message: {}", message.getPayload());
        String response = restTemplateBuilder.build().getForObject("http://localhost:8080/hello", String.class);
        log.info("Made api call and received {}", response);
    }
}

