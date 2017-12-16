package me.sstefani.todoapi.controller;

import me.sstefani.todoapi.integration.ChecklistRepository;
import me.sstefani.todoapi.integration.TaskRepository;
import me.sstefani.todoapi.model.Checklist;
import me.sstefani.todoapi.model.Task;
import me.sstefani.todoapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ChecklistRepository checklistRepository;

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping("/checklists/{checklistId}/tasks")
    public Task createTask(@Valid @RequestBody Task task, @PathVariable(value = "checklistId") Long checklistId) {
        Checklist checklist = checklistRepository.findOne(checklistId);
        checklist.getTasks().add(task);
        task.setChecklist(checklist);
        return taskRepository.save(task);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable(value = "id") Long taskId) {
        Task task = taskRepository.findOne(taskId);
        return task == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(task);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable(value = "id") Long taskId, @Valid @RequestBody Task data) {
        Task task = taskRepository.findOne(taskId);
        if (task == null) return ResponseEntity.notFound().build();

        task.setTitle(data.getTitle());
        task.setCompleted(data.isCompleted());

        return ResponseEntity.ok(taskRepository.save(task));
    }

    @DeleteMapping("/checklists/{checklistId}/tasks/{taskId}")
    public ResponseEntity<Task> deleteTask(@PathVariable(value = "checklistId") Long checklistId, @PathVariable(value = "taskId") Long taskId) {
        Checklist checklist = checklistRepository.findOne(checklistId);
        Task task = taskRepository.findOne(taskId);
        if (task == null) return ResponseEntity.notFound().build();

        task.setChecklist(null);
        checklist.getTasks().remove(task);

        checklistRepository.save(checklist);
        taskRepository.delete(task);
        return ResponseEntity.ok().build();
    }
}
