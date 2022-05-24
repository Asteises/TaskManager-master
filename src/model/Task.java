package model;

import enums.Status;
import enums.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/*
Task - класс описывающий задачу.
 */
public class Task {
    private String name;
    private String description;
    private String id;
    private Status status;
    protected Type type;

    private int duration;

    private LocalDateTime startTime;


    /**
     * Зачем передавать статус вручную для новой задачи? При создании задача имеет статус NEW, так как она новая.
     * То есть, зачем пользователю создавать задачу со статусом В процессе или Задача завершена?
     */
    public Task(Status status, String name, String description, int duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = createId();
        this.status = status;
        type = Type.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    /**
     * Расчет времени завершения задачи
     */
    public LocalDateTime getEndTime(LocalDateTime startTime, int duration) {
        return startTime.plusMinutes(duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    private String createId() {

        String id = String.valueOf(UUID.randomUUID());
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + "," + duration + "," + startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }
}
