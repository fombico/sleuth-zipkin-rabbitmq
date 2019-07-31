package com.fombico.app1;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CustomProcessor {

    String CHANNEL = "sharedChannel";

    @Output(CHANNEL)
    MessageChannel outputChannel();

}
