package me.sstefani.todoapi;

import me.sstefani.todoapi.integration.ChecklistRepository;
import me.sstefani.todoapi.integration.TaskRepository;
import me.sstefani.todoapi.integration.UserRepository;
import me.sstefani.todoapi.model.Checklist;
import me.sstefani.todoapi.model.Task;
import me.sstefani.todoapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class TodoApiApplication implements CommandLineRunner{

    private final ChecklistRepository checklistRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public TodoApiApplication(ChecklistRepository checklistRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.checklistRepository = checklistRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        // Cleanup Database tables
        checklistRepository.deleteAllInBatch();
        taskRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        // ======================================

        User user = new User("Alan Turing", "turing@enigma.org", "secret");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        Checklist checklist1 = new Checklist("University todo list");
        Checklist checklist2 = new Checklist("Work todo list");
        Checklist checklist3 = new Checklist("Music todo list");

        Task task1 = new Task("First task");
        task1.setChecklist(checklist1);

        Task task2 = new Task("Second task");
        task2.setChecklist(checklist1);

        checklist1.getTasks().add(task1);
        checklist1.getTasks().add(task2);
        checklist1.getUsers().add(user);

        checklist3.getUsers().add(user);

        user.getChecklists().add(checklist1);
        user.getChecklists().add(checklist3);

        userRepository.save(user);
        checklistRepository.save(checklist2);

        // ======================================
    }
}
