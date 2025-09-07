package com.yusup.tasks.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final Map<String, List<String>> tasks = new HashMap<>();

    // Get tasks for a given day
    @GetMapping("/{day}")
    public List<String> getTasks(@PathVariable String day) {
        return tasks.getOrDefault(day, new ArrayList<>());
    }

    // Add a task to a given day
    @PostMapping("/{day}")
    public List<String> addTask(@PathVariable String day, @RequestBody String task) {
        tasks.computeIfAbsent(day, k -> new ArrayList<>()).add(task);
        return tasks.get(day);
    }
}
