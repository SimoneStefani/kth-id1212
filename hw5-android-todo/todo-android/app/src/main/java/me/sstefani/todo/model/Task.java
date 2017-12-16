package me.sstefani.todo.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {

    private Long id;
    private String title;
    private boolean completed;
    private Checklist checklist;
    private Date createdAt;
    private Date updatedAt;
    public Task() {
    }

    public Task(String title) {
        this.title = title;
        this.completed = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}

