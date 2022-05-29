package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.HTTPTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import utils.LocalDateTimeDeserializer;
import utils.LocalDateTimeSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Handler implements HttpHandler {
    private final String uri = "http://localhost:8078";
    private final Managers managers = new Managers();
    private TaskManager tasksManager;

    private Gson gson;

    public Handler() throws IOException, InterruptedException {
        tasksManager = managers.getDefault(uri);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gson = gsonBuilder.create();

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
                String id = exchange.getRequestURI().toString().substring("/tasks/task/?id=".length());
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
                String id = exchange.getRequestURI().toString().substring("/tasks/subtask/?id=".length());
                text = tasksManager.getSubtaskById(id).toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            } else if (path.contains("tasks/subtask/epic/?id=")) {
                String id = exchange.getRequestURI().toString().substring("/tasks/subtask/epic/?id=".length());
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
                String id = exchange.getRequestURI().toString().substring("/tasks/epic/?id=".length());
                text = tasksManager.getEpicById(id).toString();
                resp = text.getBytes(UTF_8);
                exchange.sendResponseHeaders(200, resp.length);
                exchange.getResponseBody().write(resp);
            }
        } else if (path.contains("tasks/load")) {
            try {
                String key = exchange.getRequestURI().toString().substring("/tasks/load/?key=".length());
                tasksManager = HTTPTaskManager.loadFromServer("http://localhost:8078");
                exchange.sendResponseHeaders(200,0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        exchange.close();
    }

    public void handlePost(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().toString();
        if (path.contains("tasks/task")) {
            try {
                String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(json, Task.class);
//                task.setStartTime(LocalDateTime.now());
                tasksManager.saveTask(task);
                exchange.sendResponseHeaders(200,0);
                System.out.println(task);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        } else if (path.contains("tasks/epic")) {
            String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = gson.fromJson(json, Epic.class);
            tasksManager.saveEpic(epic);
            exchange.sendResponseHeaders(200,0);
            System.out.println(epic);
        } else if (path.contains("tasks/subtask")) {
            String json = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Subtask subtask = gson.fromJson(json, Subtask.class);
            tasksManager.saveSubtask(subtask);
            exchange.sendResponseHeaders(200, 0);
            System.out.println(subtask);
        }
        exchange.close();
    }

    public void handleDelete(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().toString();
        if (path.contains("tasks/task")) {
            if (path.equals("tasks/task")) {
                tasksManager.clearTasks();
                exchange.sendResponseHeaders(200, 0);
                System.out.println("Все task удалены.");
            }
            if (path.equals("tasks/task/?id=")) {
                String id = exchange.getRequestURI().toString().substring("/tasks/task/?id=".length());
                if (tasksManager.getTaskById(id) != null) {
                    tasksManager.deleteTask(id);
                    exchange.sendResponseHeaders(200, 0);
                    System.out.println("Task " + id + " удалена.");
                } else {
                    exchange.sendResponseHeaders(404, 0);
                    System.out.println("Task " + id + " не найден.");
                }
            }
        } else if (path.contains("tasks/subtask")) {
            if (path.equals("tasks/subtask")) {
                tasksManager.clearSubtasks();
                exchange.sendResponseHeaders(200, 0);
                System.out.println("Все subtask удалены.");
            }
            if (path.equals("tasks/subtask/?id=")) {
                String id = exchange.getRequestURI().toString().substring("tasks/subtask/?id=".length());
                if (tasksManager.getEpicById(id) != null) {
                    tasksManager.deleteSubtask(id);
                    exchange.sendResponseHeaders(200, 0);
                    System.out.println("Subtask " + id + " удалена.");
                } else {
                    exchange.sendResponseHeaders(404, 0);
                    System.out.println("Subtask " + id + " не найден.");
                }
            }
        } else if (path.contains("tasks/epic")) {
            if (path.equals("/tasks/epic")) {
                tasksManager.clearEpics();
                exchange.sendResponseHeaders(200, 0);
                System.out.println("Все epic удалены.");
            } else if (path.contains("tasks/epic/?id=")) {
                String id = exchange.getRequestURI().toString().substring("/tasks/epic/?id=".length());
                if (tasksManager.getSubtaskById(id) != null) {
                    tasksManager.deleteEpic(id);
                    exchange.sendResponseHeaders(200, 0);
                    System.out.println("Epic" + id + " удален.");
                } else {
                    exchange.sendResponseHeaders(404, 0);
                    System.out.println("Epic" + id + " не найден.");
                }
            }
        }
        exchange.close();
    }
}
