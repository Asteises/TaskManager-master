package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import enums.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class HandlerTest {

    private HttpClient client;
    private KVServer kvServer;
    private HttpServer httpServer;

    @BeforeEach
    void init() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        httpServer = HttpServer.create();
        Handler handler = new Handler();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", handler);
        httpServer.start();
        client = HttpClient.newBuilder().build();
    }

    @AfterEach
    void tearDown() {
        httpServer.stop(1);
        kvServer.stop();
    }



    @Test
    public void testPostTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(getTask());
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        Task task = getTask();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI uriTask = URI.create("http://localhost:8080/tasks/task/?id=" + task.getId());
        HttpRequest requestTask = HttpRequest.newBuilder().uri(uriTask).GET().build();
        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, responseTask.statusCode());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        Task task = getTask();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        URI uriTask = URI.create("http://localhost:8080/tasks/task/?id=" + task.getId());
        HttpRequest requestTask = HttpRequest.newBuilder().uri(uriTask).DELETE().build();
        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, responseTask.statusCode());
    }

    private Task getTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setStatus(Status.NEW);
        task.setDescription("desc");
        task.setName("name");
        return task;
    }
}
