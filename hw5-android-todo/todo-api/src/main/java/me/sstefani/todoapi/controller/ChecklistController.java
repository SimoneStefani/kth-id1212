package me.sstefani.todoapi.controller;

import me.sstefani.todoapi.integration.ChecklistRepository;
import me.sstefani.todoapi.integration.UserRepository;
import me.sstefani.todoapi.model.Checklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChecklistController {

    @Autowired
    ChecklistRepository checklistRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/checklists")
    public List<Checklist> getAllChecklists(Principal principal) {
        return checklistRepository.findAllByUsers(userRepository.findByUsername(principal.getName()));
    }

    @PostMapping("/checklists")
    public Checklist createChecklist(@Valid @RequestBody Checklist checklist) {
        return checklistRepository.save(checklist);
    }

    @GetMapping("/checklists/{id}")
    public ResponseEntity<Checklist> getChecklistById(@PathVariable(value = "id") Long checklistId) {
        Checklist checklist = checklistRepository.findOne(checklistId);
        return checklist == null ? ResponseEntity.notFound().build() : ResponseEntity.ok().body(checklist);
    }

    @PutMapping("/checklists/{id}")
    public ResponseEntity<Checklist> updateChecklist(@PathVariable(value = "id") Long checklistId, @Valid @RequestBody Checklist data) {
        Checklist checklist = checklistRepository.findOne(checklistId);
        if (checklist == null) return ResponseEntity.notFound().build();

        checklist.setName(data.getName());

        return ResponseEntity.ok(checklistRepository.save(checklist));
    }

    @DeleteMapping("/checklists/{id}")
    public ResponseEntity<Checklist> deleteChecklist(@PathVariable(value = "id") Long checklistId) {
        Checklist checklist = checklistRepository.findOne(checklistId);
        if (checklist == null) return ResponseEntity.notFound().build();

        checklistRepository.delete(checklist);
        return ResponseEntity.ok().build();
    }
}
