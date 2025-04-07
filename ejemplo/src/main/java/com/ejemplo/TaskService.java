package com.ejemplo;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskService {
    private Map<Long, Task> taskMap = new HashMap<>();
    private Long currentId = 1L;

    public List<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public Task getTaskById(Long id) {
        return taskMap.get(id);
    }

    public Task createTask(Task task) {
        task.setId(currentId++);
        taskMap.put(task.getId(), task);
        return task;
    }

    public boolean deleteTask(Long id) {
        return taskMap.remove(id) != null;
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskMap.get(id);
        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setCompleted(updatedTask.isCompleted());
            return existingTask;
        }
        return null;
    }
}
