package manager;

import enums.Status;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class HTTPTaskManagerTest {

    private final String PATH = "http://localhost:8078";

    private TaskManager taskManager;

    @BeforeEach
    public void init() throws IOException, InterruptedException {
        new KVServer().start();
        taskManager = Managers.getDefault(PATH);

    }

    @Test
    public void saveAndLoadFromServerTest() throws IOException, InterruptedException {
        Task task = getTask();
        Epic epic = getEpic();
        Subtask subtask = getSubtask();
        taskManager.saveTask(task);
        taskManager.saveEpic(epic);
        taskManager.saveSubtask(subtask);
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());
        TaskManager taskManagerFromServer = HTTPTaskManager.loadFromServer(PATH);

        Assertions.assertEquals(task, taskManagerFromServer.getTaskById(task.getId()));
        Assertions.assertEquals(epic, taskManagerFromServer.getEpicById(epic.getId()));
        Assertions.assertEquals(subtask, taskManagerFromServer.getSubtaskById(subtask.getId()));
    }

    private Task getTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setStatus(Status.NEW);
        task.setDescription("desc");
        task.setName("name");
        task.setStartTime(LocalDateTime.now());
        return task;
    }

    private Epic getEpic() {
        Epic epic = new Epic();
        epic.setId(UUID.randomUUID().toString());
        epic.setStatus(Status.NEW);
        epic.setDescription("desc");
        epic.setName("name");
        epic.setStartTime(LocalDateTime.now());
        return epic;
    }

    private Subtask getSubtask() {
        Subtask subtask = new Subtask();
        subtask.setId(UUID.randomUUID().toString());
        subtask.setStatus(Status.NEW);
        subtask.setDescription("desc");
        subtask.setName("name");
        subtask.setStartTime(LocalDateTime.now());
        return subtask;

    }
}
