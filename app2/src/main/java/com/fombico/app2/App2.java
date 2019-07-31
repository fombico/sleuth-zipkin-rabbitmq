package com.fombico.app2;

import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import brave.propagation.Propagation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class App2 {

    public static void main(String[] args) {
        SpringApplication.run(App2.class, args);
    }

    @Bean
    Propagation.Factory propFactory() {
        return ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
                .addPrefixedFields("baggage-", Arrays.asList("store-id"))
                .build();
    }
}
