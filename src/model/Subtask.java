package model;

import enums.Status;
import enums.Type;

import java.time.LocalDateTime;

/*
Subtask - класс для подзадачи внутри задачи Epic.
 */
/**
 * Зачем передавать статус вручную для новой задачи? При создании задача имеет статус NEW, так как она новая.
 * То есть, зачем пользователю создавать задачу со статусом В процессе или Задача завершена?
 */
public class Subtask extends Task {
    private final String epicId;

    public Subtask(Status status, String epicId, String name, String description, int duration, LocalDateTime startTime) {
        super(status, name, description, duration, startTime);
        this.epicId = epicId;
        this.type = Type.SUBTASK;
    }

    public LocalDateTime getEndTime(LocalDateTime startTime, int duration) {
        return startTime.plusMinutes(duration);
    }

    public String getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return super.toString() + "," + getEpicId();
    }
}
