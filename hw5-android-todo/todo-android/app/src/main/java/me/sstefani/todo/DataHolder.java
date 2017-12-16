package me.sstefani.todo;


import me.sstefani.todo.model.User;

public class DataHolder {

    private static final DataHolder holder = new DataHolder();

    private String jwt;
    private User currentUser;

    public static DataHolder getInstance() {
        return holder;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public static DataHolder getHolder() {
        return holder;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
