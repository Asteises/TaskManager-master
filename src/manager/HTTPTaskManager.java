package manager;

import enums.Type;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager{
    private static KVTaskClient kvTaskClient;

    public HTTPTaskManager(String uri) throws IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(uri);
    }

    public static HTTPTaskManager loadFromServer(String key, String uri) throws IOException, InterruptedException {
        HTTPTaskManager manager = new HTTPTaskManager(uri);

            StringBuilder sb = new StringBuilder(kvTaskClient.load(key));
            String[] strings = sb.toString().split("\n");
            List<String> historyList = historyFromString(strings[strings.length - 1]);
            strings[strings.length - 1] = null;
            strings[strings.length - 2] = null;
            for (String value : strings) {
                if (value != null) {
                    Task task = fromString(value);
                    if (task.getType().equals(Type.TASK)) {
                        manager.saveTask(task);
                    } else if (task.getType().equals(Type.EPIC)) {
                        manager.saveEpic((Epic) task);
                    } else {
                        manager.saveSubtask((Subtask) task);
                    }
                    if (historyList.contains(task.getId())) {
                        manager.history().add(task);
                    }
                }
            }
        return manager;
    }

    public void save() {

        StringBuilder sb = new StringBuilder();
        for (Task task : super.getAllTasks()) {
            sb.append(task.toString());
            sb.append("\n");
        }
        for (Epic epic : super.getAllEpics()) {
            sb.append(epic.toString());
            sb.append("\n");
        }
        for (Subtask subtask : super.getAllSubtasks()) {
            sb.append(subtask.toString());
            sb.append("\n");
        }
        sb.append("\n");
        if (!super.history().isEmpty()) {
            for (Task task : super.history()) {
                sb.append(task.getId()).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            try {
                kvTaskClient.put("backup", sb.toString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
