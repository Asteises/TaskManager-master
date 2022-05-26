package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;
import utils.LocalDateTimeAdapter;
import java.io.IOException;
import java.time.LocalDateTime;

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

    public static HTTPTaskManager loadFromServer(String key, String uri) throws IOException, InterruptedException {
        HTTPTaskManager manager;

        manager = gson.fromJson(kvTaskClient.load(key), HTTPTaskManager.class);

        return manager;
    }

    public void save() {

        String taskManagerJson = gson.toJson(this);
        kvTaskClient.put("backup", taskManagerJson);
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public Task getTaskById(String taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(String epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(String subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }

    @Override
    public void deleteTask(String taskId) {
        super.deleteTask(taskId);
        save();
    }

    @Override
    public void deleteEpic(String epicId) {
        super.deleteEpic(epicId);
        save();
    }

    @Override
    public void deleteSubtask(String subtaskId) {
        super.deleteSubtask(subtaskId);
        save();
    }
}
