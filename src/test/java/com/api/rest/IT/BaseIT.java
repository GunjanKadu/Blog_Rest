package com.api.rest.IT;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BaseIT {

    @Value("${server.port}")
    public int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public String baseUrl;

    public int getPort() {
        return port;
    }

    public String getBaseUrl() {
        baseUrl = String.format("http://localhost:%d/api/v1/", port);
        return this.baseUrl;
    }

    public TestRestTemplate getRestTemplate() {
        return restTemplate;
    }

}
