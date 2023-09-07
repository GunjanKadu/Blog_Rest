package com.api.rest.IT;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public TestRestTemplate getTestRestTemplate() {
        return new TestRestTemplate();
    }

}
