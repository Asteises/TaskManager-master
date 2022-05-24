package manager;

import enums.Status;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void deleteAllTasksFromHistory() {
        TaskManager taskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        Task task1 = new Task(Status.NEW,"Test task  name", "Test task description", 100, LocalDateTime.now());
        taskManager.saveTask(task1);

    }

    @Test
    void deleteAllEpicsFromHistory() {
    }

    @Test
    void deleteAllSubtasksFromHistory() {
    }

    @Test
    void add() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void remove() {
    }

    @Test
    void linkLast() {
    }

    @Test
    void getTasks() {
    }
}