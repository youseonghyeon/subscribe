package com.example.subscribify.config;

import com.example.subscribify.service.payment.PgService;
import com.example.subscribify.service.payment.TestPgService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PgService pgService() {
        return new TestPgService();
    }
}
