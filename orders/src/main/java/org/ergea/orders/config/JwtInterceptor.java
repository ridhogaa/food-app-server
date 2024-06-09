package org.ergea.orders.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class JwtInterceptor implements ClientHttpRequestInterceptor {

    private final String token;

    public JwtInterceptor(String token) {
        this.token = token;
    }

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        // Add Authorization header with the JWT token
        request.getHeaders().set("Authorization", "Bearer " + token);
        return execution.execute(request, body);
    }
}
