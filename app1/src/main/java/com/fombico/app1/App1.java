package com.fombico.app1;

import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.Propagation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class App1 {

    public static void main(String[] args) {
        SpringApplication.run(App1.class, args);
    }

    @Bean
    Propagation.Factory propFactory() {
        return ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
                .addPrefixedFields("baggage-", Arrays.asList("store-id"))
                .build();
    }
}
