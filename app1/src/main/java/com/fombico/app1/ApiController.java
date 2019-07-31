package com.fombico.app1;

import brave.propagation.ExtraFieldPropagation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Slf4j
@RestController
@AllArgsConstructor
public class ApiController {

    private MessagingService messagingService;

    @GetMapping("/hello")
    public String getGreeting() {
        String response = "hello " + new Random().nextInt();
        log.info("GET Hello with response {} ", response);
        return response;
    }

    @PostMapping("/hello")
    public void createGreeting() {
        ExtraFieldPropagation.set("store-id", "12345");
        String storeId = ExtraFieldPropagation.get("store-id");

        log.info("POST Hello " + storeId);

        messagingService.sendMessage("Hello, world");
    }
}
