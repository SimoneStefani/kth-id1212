package me.sstefani.todo;


public class DataHolder {

    private static final DataHolder holder = new DataHolder();

    private String jwt;

    public static DataHolder getInstance() {
        return holder;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}
