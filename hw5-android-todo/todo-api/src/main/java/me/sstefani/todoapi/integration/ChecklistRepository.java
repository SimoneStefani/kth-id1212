package me.sstefani.todoapi.integration;

import me.sstefani.todoapi.model.Checklist;
import me.sstefani.todoapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {
    List<Checklist> findAllByUsers(User user);
    Checklist findByCode(String code);
}
