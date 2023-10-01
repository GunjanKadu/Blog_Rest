package com.api.rest.IT;

import com.api.rest.IT.AuthController.AuthUtil;
import com.api.rest.IT.UserController.UserUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    private final HttpClient httpClient = HttpClients.createDefault();

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
        return new AuthUtil(baseIT.getTestRestTemplate(), baseIT().getBaseUrl());
    }

    @Bean
    public UserUtil userUtil() {
        BaseITTest baseIT = baseIT();
        return new UserUtil(baseIT.getTestRestTemplate(), baseIT.getBaseUrl(), httpClient);
    }

}
