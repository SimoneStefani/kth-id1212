package me.sstefani.todo.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User {

    private Long id;
    private String name;
    private String username;
    private String password;
    private Set<Checklist> checklists = new HashSet<>();
    private Date createdAt;
    private Date updatedAt;

    public User() {
    }

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Checklist> getChecklists() {
        return checklists;
    }

    public void setChecklists(Set<Checklist> checklists) {
        this.checklists = checklists;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
