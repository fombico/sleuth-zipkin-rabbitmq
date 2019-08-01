package com.fombico.app2;

import brave.propagation.CurrentTraceContext;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

    @Bean
    CurrentTraceContext.ScopeDecorator restoreStoreIdScopeDecorator() {
        return (currentSpan, scope) -> {
            String storeId = MDC.get("store-id");
            return new RestoreStoreIdScope(storeId);
        };
    }

    @AllArgsConstructor
    private class RestoreStoreIdScope implements CurrentTraceContext.Scope {
        private String storeId;

        @Override
        public void close() {
            MDC.put("store-id", storeId);
        }
    }
}
