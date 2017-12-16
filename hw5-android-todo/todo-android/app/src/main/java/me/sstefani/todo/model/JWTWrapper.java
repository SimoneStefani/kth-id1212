package me.sstefani.todo.model;

public class JWTWrapper {

    private String jwt;

    public JWTWrapper(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
