package server.integration;

import org.mindrot.jbcrypt.BCrypt;
import server.model.User;

import javax.persistence.*;

public class UserDAO {
    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> managerThreadLocal = new ThreadLocal<>();

    public UserDAO() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.netprog.jpa");
    }

    public User registerUser(String username, String password) {
        EntityManager entityManager = beginTransaction();

        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username");
        query.setParameter("username", username);
        User user;
        User newUser = null;

        try {
            user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            newUser = new User(username, password);
            entityManager.persist(newUser);
        }
        commitTransaction();
        return newUser;
    }

    public void unregisterUser(String username, String password) {
        EntityManager entityManager = beginTransaction();

        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username");
        query.setParameter("username", username);
        User user = null;

        try {
            user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("User not found");
        }

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            entityManager.remove(user);
            System.out.println("User successfully deleted");
        }

        commitTransaction();
    }

    public User loginUser(String username, String password) {
        EntityManager entityManager = openEntityManager();

        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username");
        query.setParameter("username", username);
        User user = null;

        try {
            user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("User not found");
        }

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            System.out.println("Logged in as " + user.getUsername());
        }

        entityManager.close();
        return user;
    }

    public void logoutUser() {

    }

    private EntityManager openEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private EntityManager beginTransaction() {
        EntityManager entityManager = openEntityManager();
        managerThreadLocal.set(entityManager);
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) transaction.begin();
        return entityManager;
    }

    private void commitTransaction() {
        managerThreadLocal.get().getTransaction().commit();
    }
}
