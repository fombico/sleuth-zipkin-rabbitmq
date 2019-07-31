package com.fombico.app1;

import brave.propagation.ExtraFieldPropagation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@Slf4j
public class ApiController {

    @Autowired
    private MessagingService messagingService;

    @GetMapping("/hello")
    public String hello12345() {
//        ExtraFieldPropagation.set("store-id", "12345");
        String storeId = ExtraFieldPropagation.get("store-id");
        log.info("GET Hello from " + storeId);
        return "hello " + new Random().nextInt();
    }

    @PostMapping("/hello")
    public void hello() {
        ExtraFieldPropagation.set("store-id", "12345");
        String storeId = ExtraFieldPropagation.get("store-id");

        log.info("POST Hello " + storeId);

        messagingService.sendMessage("Hello, world");
    }
}
