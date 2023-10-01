package com.api.rest.IT.UserController;

import com.api.rest.BlogUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;

@AllArgsConstructor
public class UserUtil {

    private TestRestTemplate testRestTemplate;
    private String baseUrl;
    private HttpClient httpClient;

    public <RESP_T> ResponseEntity<RESP_T> getMe(String token, Class<RESP_T> responseType) {
        String url = this.baseUrl + "users/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<RESP_T> response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity,
                responseType);
        return response;
    }

    public HttpResponse editMe(String token, BlogUser requestBody)
            throws IOException {
        String url = this.baseUrl + "users/me/";

        HashMap<String, String> blogUserRequestBody = new HashMap<>();
        blogUserRequestBody.put("firstName", requestBody.getFirstName());
        blogUserRequestBody.put("lastName", requestBody.getLastName());
        blogUserRequestBody.put("email", requestBody.getEmail());
        blogUserRequestBody.put("age", Integer.toString(requestBody.getAge()));
        blogUserRequestBody.put("password", requestBody.getPassword());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyString = objectMapper.writeValueAsString(blogUserRequestBody);
        StringEntity entity = new StringEntity(requestBodyString);

        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.addHeader("Content-Type", "application/json");
        httpPatch.addHeader("Authorization", "Bearer " + token);
        httpPatch.setEntity(entity);

        return httpClient.execute(httpPatch);
    }

}
