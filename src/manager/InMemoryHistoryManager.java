package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> last;
    private final Map<String, Node<Task>> history;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    public void deleteAllTasksFromHistory(Map<String, Task> taskMap) {
        for (Task task : taskMap.values()) {
            if (history.get(task.getId()) != null) {
                remove(task.getId());
            }
        }
    }

    public void deleteAllEpicsFromHistory(Map<String, Epic> epicMap) {
        for (Epic epicTask : epicMap.values()) {
            if (epicMap.get(epicTask.getId()) != null) {
                for (Subtask subtask : epicTask.getSubtasks()) {
                    remove(subtask.getId());
                }
                remove(epicTask.getId());
            }
        }
    }

    public void deleteAllSubtasksFromHistory(Map<String, Epic> epicMap) {
        for (Epic epicTask : epicMap.values()) {
            for (Subtask subtask : epicTask.getSubtasks()) {
                if (history.get(subtask.getId()) != null) {
                    remove(subtask.getId());
                }
            }
        }
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(String id) {
        removeNode(history.remove(id));
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> next = node.getNext();
            Node<Task> prev = node.getPrev();
            if (next == null) {
                last = prev;
            } else {
                next.setPrev(prev);
            }
            if (prev == null) {
                head = next;
            } else {
                prev.setNext(next);
            }
        }
    }

    private void linkLast(Task task) {
        if (task == null) {
            return;
        }
        if (history.get(task.getId()) != null) {
            remove(task.getId());
        }
        Node<Task> temp = last;
        Node<Task> newNode = new Node<Task>(task, null, temp);
        last = newNode;
        if (head == null) {
            head = newNode;
        } else {
            temp.setNext(newNode);
        }
        history.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> temp = head;
        while (temp != null) {
            tasks.add(temp.getData());
            temp = temp.getNext();
        }
        return tasks;
    }

}
