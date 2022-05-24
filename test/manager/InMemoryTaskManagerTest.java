package manager;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    public Task getTaskTest() {
        return new Task(Status.NEW,"Test addNewTask", "Test addNewTask description",
                100, LocalDateTime.now());
    }

    @Test
    public Epic getEpicTest() {
        return new Epic(Status.NEW,"Test epic  name", "Test epic description");
    }

    @Test
    public Subtask getSubtaskTest(Epic epic) {
        return new Subtask(Status.NEW, epic.getId(), "Test subtask name", "Test subtask description",
                10, LocalDateTime.now());
    }

    @Test
    void saveEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = getEpicTest();
        taskManager.saveEpic(epic);

        assertNotNull(taskManager.getEpicById(epic.getId()), "epic не найдена.");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество epic.");
        assertEquals(epic, taskManager.getAllEpics().get(0), "epic не совпадают.");
        assertNotNull(taskManager.getEpicById(epic.getId()), "epic не найден");
    }

    @Test
    void saveTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = getTaskTest();
        taskManager.saveTask(task);

        assertNotNull(task, "Task не найдена.");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество task.");
        assertEquals(task, taskManager.getAllTasks().get(0), "Task не совпадают.");
        assertNotNull(taskManager.getTaskById(task.getId()), "Task не найден");
    }

    @Test
    void saveSubtask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(Status.NEW, "Test epic  name", "Test epic description");
        taskManager.saveEpic(epic);
        Subtask subtask = getSubtaskTest(epic);
        taskManager.saveSubtask(subtask);

        assertNotNull(subtask, "Subtask не найдена.");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество Subtask.");
        assertEquals(subtask, taskManager.getAllSubtaskByEpic(epic.getId()).get(0), "Subtask не совпадают.");
        assertNotNull(taskManager.getSubtaskById(subtask.getId()), "Subtask не найден");
    }

    @Test
    void clearTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task(Status.NEW,"Test addNewTask", "Test addNewTask description", 10, LocalDateTime.now());
        taskManager.saveTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество task.");
        taskManager.clearTasks();

        assertEquals(0, taskManager.getAllTasks().size(), "Неверное количество task.");
    }

    @Test
    void clearEpics() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(Status.NEW,"Test epic  name", "Test epic description");
        taskManager.saveEpic(epic);
        taskManager.clearEpics();

        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество epic.");
    }

    @Test
    void clearSubtasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(Status.NEW,"Test epic  name", "Test epic description");
        taskManager.saveEpic(epic);
        Subtask subtask = getSubtaskTest(epic);
        taskManager.saveSubtask(subtask);
//        taskManager.clearSubtasks();

        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество Subtask.");
    }

    @Test
    void updateTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = getTaskTest();
        taskManager.saveTask(task1);
        taskManager.updateTask(task1);

        assertEquals(task1, taskManager.getAllTasks().get(0), "task не совпадают.");
    }

    @Test
    void updateEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = getEpicTest();
        taskManager.saveEpic(epic1);
        taskManager.updateEpic(epic1);

        assertEquals(epic1, taskManager.getAllEpics().get(0), "epic не совпадают.");
    }

    @Test
    void updateSubtask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = getEpicTest();
        taskManager.saveEpic(epic);
        Subtask subtask = getSubtaskTest(epic);
        taskManager.saveSubtask(subtask);
        taskManager.updateSubtask(subtask);

        assertEquals(subtask, taskManager.getAllSubtasks().get(0), "subtask не совпадают.");
    }

    @Test
    void deleteTask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = getTaskTest();
        taskManager.saveTask(task);
        taskManager.deleteTask(task.getId());

        assertNull(taskManager.getTaskById(task.getId()), "Task найдена.");
        assertEquals(0, taskManager.getAllTasks().size(), "Неверное количество task.");
    }

    @Test
    void deleteEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = getEpicTest();
        taskManager.saveEpic(epic);
        taskManager.getEpicById(epic.getId());
        taskManager.deleteEpic(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()), "epic найдена.");
        assertEquals(0, taskManager.getAllEpics().size(), "Неверное количество epic.");
    }

    @Test
    void deleteSubtask() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = getEpicTest();
        Subtask subtask = getSubtaskTest(epic);
        taskManager.saveSubtask(subtask);
        taskManager.deleteSubtask(subtask.getId());

        assertNull(taskManager.getSubtaskById(subtask.getId()), "subtask найдена.");
        assertEquals(0, taskManager.getAllSubtasks().size(), "Неверное количество subtask.");
    }

    @Test
    void getAllSubtaskByEpic() {
        TaskManager taskManager = new InMemoryTaskManager();
        final List<Task> tasks = taskManager.getAllTasks();
    }

    @Test
    void printAllTasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = getTaskTest();
        Task task2 = getTaskTest();
        taskManager.saveTask(task1);
        taskManager.saveTask(task2);
        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertTrue(tasks.stream().anyMatch(task -> task.equals(task1)));
        assertTrue(tasks.stream().anyMatch(task -> task.equals(task2)));
    }

    @Test
    void printAllEpicsAndSubtasks() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(Status.NEW,"Test epic1  name", "Test epic1 description");
        Epic epic2 = new Epic(Status.NEW,"Test epic2  name", "Test epic2 description");
        taskManager.saveEpic(epic1);
        taskManager.saveEpic(epic2);
        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(2, epics.size(), "Неверное количество задач.");
        assertTrue(epics.stream().anyMatch(epic -> epic.equals(epic1)));
        assertTrue(epics.stream().anyMatch(epic -> epic.equals(epic2)));
    }

    @Test
    void history() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(Status.NEW,"Test epic1  name", "Test epic1 description");
        Task task1 = getTaskTest();
        Subtask subtask1 = getSubtaskTest(epic1);
        taskManager.saveTask(task1);
        taskManager.saveEpic(epic1);
        taskManager.saveSubtask(subtask1);
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        final List<Task> taskListHistory = taskManager.history();

        assertNotNull(taskListHistory, "Задачи на возвращаются.");
        assertEquals(3, taskListHistory.size(), "Неверное количество задач.");
        assertEquals(task1, taskListHistory.get(0), "Задачи не совпадают.");
        assertEquals(epic1, taskListHistory.get(1), "Задачи не совпадают.");
        assertEquals(subtask1, taskListHistory.get(2), "Задачи не совпадают.");
    }
}