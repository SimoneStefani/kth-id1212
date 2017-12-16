package me.sstefani.todoapi.integration;

import me.sstefani.todoapi.model.Checklist;
import me.sstefani.todoapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByChecklist(Checklist checklist);
}
