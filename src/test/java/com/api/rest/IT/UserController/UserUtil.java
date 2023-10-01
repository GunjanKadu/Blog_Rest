package com.api.rest.IT.UserController;

import lombok.AllArgsConstructor;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor

public class UserUtil {

    private TestRestTemplate testRestTemplate;
    private String baseUrl;

    public <RESP_T> ResponseEntity<RESP_T> getMe(String token, Class<RESP_T> responseType) {
        String url = this.baseUrl + "users/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RESP_T> response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity,
                responseType);
        return response;
    }

}
