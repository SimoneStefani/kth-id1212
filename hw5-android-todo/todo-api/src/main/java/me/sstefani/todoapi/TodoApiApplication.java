package me.sstefani.todoapi;

import me.sstefani.todoapi.integration.ChecklistRepository;
import me.sstefani.todoapi.integration.TaskRepository;
import me.sstefani.todoapi.model.Checklist;
import me.sstefani.todoapi.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodoApiApplication implements CommandLineRunner{

    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public TodoApiApplication(ChecklistRepository checklistRepository, TaskRepository taskRepository) {
        this.checklistRepository = checklistRepository;
        this.taskRepository = taskRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        // Cleanup Database tables
        checklistRepository.deleteAllInBatch();
        taskRepository.deleteAllInBatch();

        // ======================================

        Checklist checklist = new Checklist("University todo list");

        Task task1 = new Task("First task");
        task1.setChecklist(checklist);

        Task task2 = new Task("Second task");
        task2.setChecklist(checklist);

        checklist.getTasks().add(task1);
        checklist.getTasks().add(task2);

        checklistRepository.save(checklist);

        // ======================================
    }
}
