package com.fombico.app1;

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
        log.info("POST Hello");
        messagingService.sendMessage("Hello, world");
    }
}
