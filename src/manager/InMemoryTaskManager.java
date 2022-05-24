package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected final HistoryManager historyManager;
    private final Map<String, Task> tasks;
    private final Map<String, Epic> epics;
    private final Map<String, Subtask> subtasksEpics;


    private static Set<Task> sortedTasks;

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasksEpics = new HashMap<>();
        sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    /**
     * В задаче именно так и написано: Сложность получения должна быть уменьшена с O(n log n) до O(n).
     */
    public List<Task> getPrioritizedTasks() {
        return sortedTasks.stream().toList();
    }

    public static boolean checkTime(LocalDateTime startTime, int duration) {
        if (sortedTasks.size() == 0 || (startTime.isAfter(sortedTasks.stream().toList().get(0).getStartTime()) ||
                startTime.plusMinutes(duration).isBefore(sortedTasks.stream().toList().
                        get(sortedTasks.size() - 1).getStartTime()))) {
            return true;
        }
        return false;
    }

    @Override
    public void saveEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void saveTask(Task task) {
        tasks.put(task.getId(), task);
        if (checkTime(task.getStartTime(), task.getDuration())) {
            sortedTasks.add(task);
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        if (epics.get(subtask.getEpicId()) != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.newSubtask(subtask);
            subtasksEpics.put(subtask.getId(), subtask);
            if (checkTime(subtask.getStartTime(), subtask.getDuration())) {
                sortedTasks.add(subtask);
            }
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksEpics.values());
    }

    @Override
    public void clearTasks() {
        historyManager.deleteAllTasksFromHistory(tasks);
        tasks.clear();
        Set<Task> newTasks = new HashSet<>(tasks.values());
        sortedTasks.removeAll(newTasks);
    }

    @Override
    public void clearEpics() {
        historyManager.deleteAllEpicsFromHistory(epics);
        Set<Task> newSubtasks = new HashSet<>(subtasksEpics.values());
        clearSubtasks();
        epics.clear();
        sortedTasks.removeAll(newSubtasks);
    }

    @Override
    public void clearSubtasks() {
        historyManager.deleteAllSubtasksFromHistory(epics);
        for (Epic epic : epics.values()) {
            Set<Task> newSubtasks = new HashSet<>(epic.getSubtasks());
            sortedTasks.remove(newSubtasks);
            epic.getSubtasks().clear();
        }
    }

    @Override
    public Task getTaskById(String taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(String epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(String subtaskId) {
        Subtask subtask = subtasksEpics.get(subtaskId);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void updateTask(Task newTask) {
        Task oldTask = tasks.get(newTask.getId());
        if (checkTime(newTask.getStartTime(), newTask.getDuration()) ||
                (newTask.getStartTime().equals(oldTask.getStartTime()) &&
                        newTask.getDuration() == oldTask.getDuration())) {
            sortedTasks.remove(oldTask);
            tasks.put(newTask.getId(), newTask);
            sortedTasks.add(newTask);
        }
    }

    /**
     * Epic нет в sortedTasks.
     */
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        epics.get(epic.getId()).changeStatus();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        Subtask oldSubtask = subtasksEpics.get(newSubtask.getId());
        if (checkTime(newSubtask.getStartTime(), newSubtask.getDuration()) ||
                (newSubtask.getStartTime().equals(oldSubtask.getStartTime()) &&
                        newSubtask.getDuration() == oldSubtask.getDuration())) {
            sortedTasks.remove(oldSubtask);
            subtasksEpics.put(newSubtask.getId(), newSubtask);
            Epic epic = epics.get(newSubtask.getEpicId());
            epic.getSubtasks().remove(oldSubtask);
            epic.getSubtasks().add(newSubtask);
            epic.changeStatus();
            sortedTasks.add(newSubtask);
        }
    }

    @Override
    public void deleteTask(String taskId) {
        sortedTasks.remove(tasks.get(taskId));
        historyManager.remove(taskId);
        tasks.remove(taskId);

    }

    @Override
    public void deleteEpic(String epicId) {
        sortedTasks.remove(epics.get(epicId));
        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void deleteSubtask(String subtaskId) {
        for (Epic epic : epics.values()) {
            sortedTasks.remove(subtasksEpics.get(subtaskId));
            historyManager.remove(subtaskId);
            epic.getSubtasks().remove(subtaskId);
        }
    }

    @Override
    public List<Subtask> getAllSubtaskByEpic(String epicId) {
        return new ArrayList<>(epics.get(epicId).getSubtasks());
    }

    @Override
    public void printAllTasks(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println("id: " + task.getId() + ". " + task.getName());
        }
    }

    @Override
    public void printAllEpicsAndSubtasks(List<Epic> epics) {
        for (Epic epic : epics) {
            System.out.println("id: " + epic.getId() + ". " + epic.getName());
            for (Subtask subtask : epic.getSubtasks()) {
                System.out.println("    id: " + subtask.getId() + ". " + subtask.getName());
            }
        }
    }

    @Override
    public List<Task> history() {
        List<Task> list = historyManager.getHistory();
        return list;
    }
}
