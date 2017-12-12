package me.sstefani.todoapi.controller;

import me.sstefani.todoapi.integration.TaskRepository;
import me.sstefani.todoapi.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping("/tasks")
    public Task createTask(@Valid @RequestBody Task task) {
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
        if(task == null) return ResponseEntity.notFound().build();

        task.setTitle(data.getTitle());
        task.setCompleted(data.isCompleted());

        return ResponseEntity.ok(taskRepository.save(task));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable(value = "id") Long taskId) {
        Task task = taskRepository.findOne(taskId);
        if(task == null) return ResponseEntity.notFound().build();

        taskRepository.delete(task);
        return ResponseEntity.ok().build();
    }
}
