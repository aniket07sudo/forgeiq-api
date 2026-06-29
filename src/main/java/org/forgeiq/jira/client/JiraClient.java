package org.forgeiq.jira.client;

import lombok.RequiredArgsConstructor;
import org.forgeiq.jira.entity.JiraConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JiraClient {

    private final RestClient.Builder restClientBuilder;

    private RestClient getClient(JiraConnection connection) {

        String credentials =
                connection.getEmail() + ":" + connection.getApiToken();

        String encoded =
                Base64.getEncoder()
                        .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return restClientBuilder
                .baseUrl(connection.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoded)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public <T> T get(
            JiraConnection connection,
            String path,
            Class<T> responseType
    ) {

        return getClient(connection)
                .get()
                .uri(path)
                .retrieve()
                .body(responseType);
    }

    public <T, R> R post(
            JiraConnection connection,
            String path,
            T request,
            Class<R> responseType
    ) {

        return getClient(connection)
                .post()
                .uri(path)
                .body(request)
                .retrieve()
                .body(responseType);
    }

    public <T> void put(
            JiraConnection connection,
            String path,
            T request
    ) {

        getClient(connection)
                .put()
                .uri(path)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void delete(
            JiraConnection connection,
            String path
    ) {

        getClient(connection)
                .delete()
                .uri(path)
                .retrieve()
                .toBodilessEntity();
    }

    public <T> T get(
            JiraConnection connection,
            String path,
            ParameterizedTypeReference<T> typeReference
    ) {

        return getClient(connection)
                .get()
                .uri(path)
                .retrieve()
                .body(typeReference);
    }

}