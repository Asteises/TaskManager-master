package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import enums.Status;
import manager.Managers;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;

class Handler implements HttpHandler {
    private final String uri = "http://localhost:8078";
    private final Managers managers = new Managers();
    private TaskManager tasksManager = managers.getDefault(uri);

    public Handler() throws IOException, InterruptedException {
        Task task1 = new Task(Status.NEW,"Заголовок task1", "Текст task1", 60, LocalDateTime.now());
        Task task2 = new Task(Status.NEW,"Заголовок task2", "Текст task2", 100, LocalDateTime.now().plusMinutes(60));
        tasksManager.saveTask(task1);
        tasksManager.saveTask(task2);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if ("GET".equals(exchange.getRequestMethod())) {
            handleGet(exchange);
        }

        if ("POST".equals(exchange.getRequestMethod())) {
            handlePost(exchange);
        }

        if ("DELETE".equals(exchange.getRequestMethod())) {
            handleDelete(exchange);
        }
    }

    public void handleGet(HttpExchange exchange) throws IOException {
        String text = "";
        byte[] resp;
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        String path = exchange.getRequestURI().toString();

        if (path.contains("tasks/task")) {
            if (path.equals("/tasks/task")) {
                text = tasksManager.getAllTasks().toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
                exchange.close();
            } else if (path.contains("tasks/task/?id=")) {
                String id = exchange.getRequestURI().getPath().substring("/tasks/task/?id=".length());
                text = tasksManager.getTaskById(id).toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            } else if (path.equals("/tasks/history")) {
                text = tasksManager.history().toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        } else if (path.contains("tasks/subtask")) {
            if (path.equals("/tasks/subtask")) {
                text = tasksManager.getAllSubtasks().toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            } else if (path.contains("tasks/subtask/?id=")) {
                String id = exchange.getRequestURI().getPath().substring("/tasks/subtask/?id=".length());
                text = tasksManager.getSubtaskById(id).toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            } else if (path.contains("tasks/subtask/epic/?id=")) {
                String id = exchange.getRequestURI().getPath().substring("/tasks/subtask/epic/?id=".length());
                text = tasksManager.getAllSubtaskByEpic(id).toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        } else if (path.contains("tasks/epic")) {
            if (path.equals("/tasks/epic")) {
                text = tasksManager.getAllEpics().toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            } else if (path.contains("tasks/epic/?id=")) {
                String id = exchange.getRequestURI().getPath().substring("/tasks/epic/?id=".length());
                text = tasksManager.getEpicById(id).toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        }
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        Gson gson = new Gson();
        String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        final Task task = gson.fromJson(json, Task.class);
        System.out.println(task);
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
    }
}
