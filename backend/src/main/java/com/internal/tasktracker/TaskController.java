package com.internal.tasktracker;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/api/tasks")
    public ResponseEntity<?> searchTasks(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        // Normalize query input
        String query = (q == null) ? "" : q.trim();
        String searchTerm = query.isEmpty() ? "%" : "%" + query.toLowerCase() + "%";

        // Parse and validate status
        String normalizedStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                normalizedStatus = TaskStatus.valueOf(status.toUpperCase()).name();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid status value"));
            }
        }

        // Pagination safety
        page = Math.max(page, 1);
        pageSize = Math.max(pageSize, 1);

        // Logging
        System.out.println("[TaskController] q=\"" + query + "\" status=" + normalizedStatus
                + " page=" + page + " pageSize=" + pageSize);

        // Database-level pagination
        PageRequest pageable = PageRequest.of(page - 1, pageSize);

        Page<Task> pageResult = taskRepository.searchTasks(
                searchTerm,
                normalizedStatus,
                pageable
        );

        // Response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("items", pageResult.getContent());
        response.put("total", pageResult.getTotalElements());
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }
}