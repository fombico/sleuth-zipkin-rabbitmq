package com.fombico.app1;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@EnableBinding({CustomProcessor.class})
public class MessagingService {

    private CustomProcessor customProcessor;

    public void sendMessage(String payload) {
        customProcessor.outputChannel().send(MessageBuilder.withPayload(payload).build());
    }
}
