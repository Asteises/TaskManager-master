package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private static KVTaskClient kvTaskClient;
    private static Gson gson;

    public HTTPTaskManager(String uri) throws IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(uri);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
    }

    public HTTPTaskManager() {
    }

    public static HTTPTaskManager loadFromServer(String uri) throws IOException, InterruptedException {
        HTTPTaskManager manager = new HTTPTaskManager(uri);

        List<Task> tasks = gson.fromJson(kvTaskClient.load("task"), new TypeToken<List<Task>>() {
        }.getType());
        if (tasks != null) {
            for (Task task : tasks) {
                manager.saveTask(task);
            }
        }

        List<Epic> epics = gson.fromJson(kvTaskClient.load("epic"), new TypeToken<List<Epic>>() {
        }.getType());
        if (epics != null) {
            for (Epic epic : epics) {
                manager.saveEpic(epic);
            }
        }

        List<Subtask> subtasks = gson.fromJson(kvTaskClient.load("subtask"), new TypeToken<List<Subtask>>() {
        }.getType());
        if (subtasks != null) {
            for (Subtask subtask : subtasks) {
                manager.saveSubtask(subtask);
            }
        }

        List<Task> tasksForHistory = gson.fromJson(kvTaskClient.load("history"), new TypeToken<List<Task>>() {
        }.getType());
        if (tasksForHistory != null) {
            for (Task task: tasksForHistory) {
                manager.historyManager.add(task);
            }
        }
        return manager;
    }

    // taskListLocal {task1}
    // taskListKVServer {task1}

    public void save(String key) {
        String json = null;
        switch (key) {
            case "task":
                json = gson.toJson(this.getAllTasks());
                break;
            case "epic":
                json = gson.toJson(this.getAllEpics());
                break;
            case "subtask":
                json = gson.toJson(this.getAllSubtasks());
                break;
            case "history":
                json = gson.toJson(this.getHistoryForJson());
        }
        kvTaskClient.put(key, json);
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        this.save("subtask");
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        this.save("epic");
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        this.save("task");
    }

    @Override
    public Task getTaskById(String taskId) {
        Task task = super.getTaskById(taskId);
        this.save("history");
        return task;
    }

    @Override
    public Epic getEpicById(String epicId) {
        Epic epic = super.getEpicById(epicId);
        this.save("history");
        return epic;
    }

    @Override
    public Subtask getSubtaskById(String subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        this.save("history");
        return subtask;
    }

    @Override
    public void deleteTask(String taskId) {
        super.deleteTask(taskId);
        this.save("task");

    }

    @Override
    public void deleteEpic(String epicId) {
        super.deleteEpic(epicId);
        this.save("epic");
    }

    @Override
    public void deleteSubtask(String subtaskId) {
        super.deleteSubtask(subtaskId);
        this.save("subtask");
    }
}
