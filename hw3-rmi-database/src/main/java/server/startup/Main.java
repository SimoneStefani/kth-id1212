package server.startup;

import server.integration.UserDAO;

public class Main {
    public static void main(String args[]) {
        UserDAO userDAO = new UserDAO();
        userDAO.registerUser("pew", "banana");
        userDAO.loginUser("pew", "banana");
    }
}
