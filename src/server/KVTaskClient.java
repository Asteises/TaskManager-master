package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String path;
    private String apiToken;
    private HttpClient client;

    public KVTaskClient(String path) {
        this.path = path;
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(path + "/register")).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 403) {
            System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
        } else if (response.statusCode() == 400) {
            System.out.println("Задан пустой запрос");
        }
        apiToken = response.body();
        System.out.println(apiToken);
    }

    public void put(String key, String json) {
        URI url = URI.create(path + "/save/" + key + "?API_TOKEN=" + apiToken);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 403) {
            System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
        } else if (key.isEmpty()) {
            System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key} " + response.statusCode());
        } else if (response.statusCode() == 405) {
            System.out.println("/save ждёт POST-запрос, а получил: " + response.statusCode());
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(path + "/load/" + key + "?API_TOKEN=" + apiToken)).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (response.statusCode() == 403) {
            System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
            return null;
        } else if (response.statusCode() == 400) {
            System.out.println("Key для получения пустой. key указывается в пути: /load/{key}");
            return null;
        } else if (response.statusCode() == 405) {
            System.out.println("/load ждёт GET-запрос, а получил: " + response.statusCode());
            return null;
        }
        return response.body();
    }
}