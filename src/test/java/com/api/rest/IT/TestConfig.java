package com.api.rest.IT;

import com.api.rest.IT.AuthController.AuthUtil;
import com.api.rest.IT.UserController.UserUtil;
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
    public BaseITTest baseIT() {
        return new BaseITTest();
    }

    @Bean
    public AuthUtil authUtil() {
        BaseITTest baseIT = baseIT();
        return new AuthUtil(baseIT.getRestTemplate(), baseIT().getBaseUrl());
    }

    @Bean
    public UserUtil userUtil() {
        BaseITTest baseIT = baseIT();
        return new UserUtil(baseIT.getRestTemplate(), baseIT.getBaseUrl());
    }

}
