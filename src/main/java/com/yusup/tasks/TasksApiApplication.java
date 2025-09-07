// src/main/java/com/yusup/tasks/TasksApiApplication.java
package com.yusup.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // scans com.yusup.tasks and all sub-packages
public class TasksApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TasksApiApplication.class, args);
    }
}
