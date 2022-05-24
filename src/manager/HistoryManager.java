package manager;

import model.Epic;
import model.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(String id);

    void deleteAllTasksFromHistory(Map<String, Task> tasks);

    void deleteAllEpicsFromHistory(Map<String, Epic> epics);

    void deleteAllSubtasksFromHistory(Map<String, Epic> epics);
}
