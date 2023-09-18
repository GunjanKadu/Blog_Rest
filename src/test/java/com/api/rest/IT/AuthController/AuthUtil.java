package com.api.rest.IT.AuthController;

import com.api.rest.controllers.AuthController.AuthenticationRequest;
import com.api.rest.controllers.AuthController.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class AuthUtil {

    private TestRestTemplate testRestTemplate;
    private String baseUrl;

    public <RESP_T> ResponseEntity<RESP_T> registerUser(RegisterRequest request, Class<RESP_T> responseType) {

        String registerUrl = baseUrl + "/auth/register";
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, null);

        ResponseEntity<RESP_T> response = testRestTemplate.exchange(registerUrl, HttpMethod.POST, requestEntity,
                responseType);

        return response;
    }

    public <RESP_T> ResponseEntity<RESP_T> loginUser(AuthenticationRequest request, Class<RESP_T> responseType) {

        String loginUrl = baseUrl + "/auth/login";
        HttpEntity<Object> requestEntity = new HttpEntity<>(request, null);

        ResponseEntity<RESP_T> response = testRestTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity,
                responseType);

        return response;
    }
}
