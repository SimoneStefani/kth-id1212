package server.integration;

import server.model.File;
import server.model.User;

import javax.persistence.*;

public class UserDAO {
    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> managerThreadLocal = new ThreadLocal<>();

    public UserDAO() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.netprog.jpa");
    }

    public User findUserByUsername(String username) {
        EntityManager entityManager = openEntityManager();

        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username");
        query.setParameter("username", username);
        User user;

        try {
            user = (User) query.getSingleResult();
        } catch (NoResultException e) {
            user = null;
        }

        entityManager.close();
        return user;
    }

    public void storeUser(User user) {
        EntityManager entityManager = beginTransaction();
        entityManager.persist(user);
        commitTransaction();
    }

    public void destroyUser(User user) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(entityManager.merge(user));
        commitTransaction();
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
