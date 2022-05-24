package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

/*
TaskManager - класс управляющий всеми задачами.
 */
public interface TaskManager {

    // Создание. Сам объект должен передаваться в качестве параметра:
    void saveEpic(Epic epic);

    void saveTask(Task task);

    void saveSubtask(Subtask subtask);

    // Получение списка всех задач:
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    // Удаление всех задач:
    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    // Получение по идентификатору:
    Task getTaskById(String taskId);

    Epic getEpicById(String epicId);

    Subtask getSubtaskById(String subtaskId);

    // Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра:
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    // Удаление по идентификатору:
    void deleteTask(String taskId);

    void deleteEpic(String epicId);

    void deleteSubtask(String subtaskId);

    // Получение списка всех подзадач определённого эпика:
    List<Subtask> getAllSubtaskByEpic(String epicId);

    // Выводим в консоль все таски для проверки:
    void printAllTasks(List<Task> tasks);

    // Выводим в консоль все епики с подзадачами для проверки:
    void printAllEpicsAndSubtasks(List<Epic> epics);

    List<Task> history();
}
