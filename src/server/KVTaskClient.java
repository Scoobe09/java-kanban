package server;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URL url;
    private final String token;
    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = new URL(url);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/register"))
                .build();

        HttpResponse<String> response = client.send(httpRequest, handler);

        token = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + token))
                .build();

        HttpResponse<String> response = client.send(request, handler);
    }

    public String load(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + token))
                .build();

        HttpResponse<String> response = client.send(request, handler);
        return response.body();
    }
}