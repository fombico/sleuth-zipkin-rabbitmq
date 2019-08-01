package com.fombico.app1;

import brave.propagation.CurrentTraceContext;
import brave.propagation.ExtraFieldPropagation;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class SleuthConfig {

    @Bean
    CurrentTraceContext.ScopeDecorator setStoreIdIfAbsentScopeDecorator() {
        return (currentSpan, scope) -> {
            if (ExtraFieldPropagation.get("store-id") == null) {
                String randomStoreId = String.valueOf(new Random().nextInt(100000) + 100000);
                ExtraFieldPropagation.set("store-id", randomStoreId);
            }
            MDC.put("store-id", ExtraFieldPropagation.get("store-id"));
            return scope;
        };
    }
}
