package me.sstefani.todo.model;

import android.os.Parcelable;

import java.io.Serializable;

public class Checklist implements Serializable {

    private Long id;
    private String name;
    private String code;

    public Checklist(String name) {
        this.name = name;
    }

    public Checklist(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
