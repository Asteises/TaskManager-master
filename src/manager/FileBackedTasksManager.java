package manager;

import enums.Status;
import enums.Type;
import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path backup = Path.of("backup.csv");
    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final String HEADER = "id,type,name,status,description,epic,duration,startTime";

    public FileBackedTasksManager() {
    }

    public static FileBackedTasksManager loadFromFile(File file) {

        FileBackedTasksManager manager = new FileBackedTasksManager();
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader bf = new BufferedReader(fileReader);
            String line;
            bf.readLine();
            StringBuilder sb = new StringBuilder();
            while ((line = bf.readLine()) != null) {
                sb.append(line).append("\n");
            }
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
                        manager.historyManager.add(task);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return manager;
    }

    protected static List<String> historyFromString(String string) {
        return Arrays.asList(string.split(","));
    }

    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        for (Task task : super.history()) {
            sb.append(task.getId());
        }
        return String.valueOf(sb);
    }

    public List<Task> getHistoryForJson() {
        return super.history();
    }

    public String toString() {
        return super.getAllTasks().toString() + "\n" + super.getAllEpics().toString() + "\n"
                + super.getAllSubtasks().toString() + "\n\n" + getHistory();
    }

    public String toString(Task task) {
        return task.toString();
    }

    /**
     * создаем LocalDateTime для метода fromString из startTime
     */
    private LocalDateTime stringToLocalDateTime(String string) {
        return LocalDateTime.parse(string, this.DATE_TIME_FORMATTER);
    }

    public static Task fromString(String value) {
        String[] strings = value.split(",");
        if (strings[1].equals("TASK")) {
            Task task = new Task(Status.valueOf(strings[3]), strings[2], strings[4], Integer.parseInt(strings[5]), LocalDateTime.parse(strings[6]));
            task.setId(strings[0]);
            return task;
        } else if (strings[1].equals("EPIC")) {
            Epic epic = new Epic(Status.valueOf(strings[3]), strings[2], strings[4]);
            epic.setId(strings[0]);
            return epic;
        } else  {
            Subtask subtask = new Subtask(Status.valueOf(strings[3]), strings[7], strings[2], strings[4], Integer.parseInt(strings[5]), LocalDateTime.parse(strings[6]));
            subtask.setId(strings[0]);
            return subtask;
        }
    }

    public void save() {
        try (BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(String.valueOf(backup)), StandardCharsets.UTF_8))) {
            bf.write(HEADER);
            bf.newLine();
            for (Task task : super.getAllTasks()) {
                bf.write(task.toString());
                bf.newLine();
            }
            for (Epic epic : super.getAllEpics()) {
                bf.write(epic.toString());
                bf.newLine();
            }
            for (Subtask subtask : super.getAllSubtasks()) {
                bf.write(subtask.toString());
                bf.newLine();
            }
            bf.newLine();
            StringBuilder sb = new StringBuilder();
            if (!super.history().isEmpty()) {
                for (Task task : super.history()) {
                    sb.append(task.getId()).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                bf.write(sb.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        //save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        //save();
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        //save();
    }

    @Override
    public Task getTaskById(String taskId) {
        Task task = super.getTaskById(taskId);
        //save();
        return task;
    }

    @Override
    public Epic getEpicById(String epicId) {
        Epic epic = super.getEpicById(epicId);
        //save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(String subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        //save();
        return subtask;
    }

    @Override
    public void deleteTask(String taskId) {
        super.deleteTask(taskId);
        //save();
    }

    @Override
    public void deleteEpic(String epicId) {
        super.deleteEpic(epicId);
        //save();
    }

    @Override
    public void deleteSubtask(String subtaskId) {
        super.deleteSubtask(subtaskId);
        //save();
    }
}
