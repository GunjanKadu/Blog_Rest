package com.api.rest.IT.AuthController;

import com.api.rest.controllers.AuthController.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
public class AuthUtil {

    private TestRestTemplate testRestTemplate;
    private String baseUrl;

    public ResponseEntity<Object> registerUser(RegisterRequest request) {

        String registerUrl = baseUrl + "/auth/register";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(registerUrl);
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, null);

        ResponseEntity<Object> response = testRestTemplate.exchange(registerUrl, HttpMethod.POST,
                requestEntity, Object.class);

        return response;
    }
}
