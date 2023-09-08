package com.api.rest.IT;

import com.api.rest.IT.AuthController.AuthUtil;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public TestRestTemplate getTestRestTemplate() {
        return new TestRestTemplate();
    }

    @Bean
    public BaseIT baseIT() {
        return new BaseIT();
    }

    @Bean
    public AuthUtil authUtil() {
        BaseIT baseIT = baseIT();
        return new AuthUtil(baseIT.getRestTemplate(), baseIT().getBaseUrl());
    }

}
