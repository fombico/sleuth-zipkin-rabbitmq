package com.fombico.app1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@EnableBinding({CustomProcessor.class})
@Component
public class MessagingService {

    @Autowired
    private CustomProcessor customProcessor;

    public void sendMessage(String message) {
        customProcessor.outputChannel().send(MessageBuilder.withPayload(message).build());
    }
}
