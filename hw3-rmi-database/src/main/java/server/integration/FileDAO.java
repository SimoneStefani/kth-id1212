package server.integration;

import server.model.File;
import server.model.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> managerThreadLocal = new ThreadLocal<>();

    public FileDAO() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.netprog.jpa");
    }

    public List<File> findAllFiles(User owner) {
        EntityManager entityManager = openEntityManager();

        Query query = entityManager.createQuery("SELECT f FROM File f WHERE (f.privateAccess=false OR f.owner=:owner)");
        query.setParameter("owner", owner);
        List<File> files;

        try {
            files = query.getResultList();
        } catch (NoResultException e) {
            files = new ArrayList<>();
        }

        entityManager.close();
        return files;
    }

    public File findFileByName(String name) {
        EntityManager entityManager = openEntityManager();

        Query query = entityManager.createQuery("SELECT f FROM File f WHERE f.name=:name");
        query.setParameter("name", name);
        File file;

        try {
            file = (File) query.getSingleResult();
        } catch (NoResultException e) {
            file = null;
        }

        entityManager.close();
        return file;
    }

    public void storeFile(File file) {
        EntityManager entityManager = beginTransaction();
        entityManager.persist(file);
        commitTransaction();
    }

    public void updateFile(File file) {
        EntityManager entityManager = beginTransaction();
        entityManager.merge(file);
        commitTransaction();
    }

    public void destroyFile(File file) {
        EntityManager entityManager = beginTransaction();
        entityManager.remove(entityManager.merge(file));
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
