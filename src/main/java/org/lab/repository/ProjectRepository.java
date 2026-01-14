package org.lab.repository;

import org.lab.model.Project;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectRepository {
    private final Map<UUID, Project> storage = new HashMap<>();

    public Project save(Project project) {
        storage.put(project.id(), project);
        return project;
    }

    public Optional<Project> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Project> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Project> findByUser(UUID userId) {
        return storage.values().stream()
                .filter(p -> p.memberships().stream().anyMatch(u -> u.userId().equals(userId)))
                .collect(Collectors.toList());
    }
}
