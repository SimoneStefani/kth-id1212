package me.sstefani.todoapi.controller;

import me.sstefani.todoapi.integration.ChecklistRepository;
import me.sstefani.todoapi.integration.UserRepository;
import me.sstefani.todoapi.model.Checklist;
import me.sstefani.todoapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChecklistRepository checklistRepository;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @GetMapping("/api/user")
    public User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    @PutMapping("/api/users/checklists/{checklistCode}")
    public ResponseEntity<Checklist> updateTask(@PathVariable(value = "checklistCode") String checklistCode, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Checklist checklist = checklistRepository.findByCode(checklistCode);
        if (checklist == null) return ResponseEntity.notFound().build();
        if (user == null) return ResponseEntity.notFound().build();

        user.getChecklists().add(checklist);
        checklist.getUsers().add(user);

        return ResponseEntity.ok(checklistRepository.save(checklist));
    }
}
