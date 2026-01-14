package org.lab.repository;

import org.lab.model.Milestone;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MilestoneRepository {

    private final Map<UUID, Milestone> storage = new ConcurrentHashMap<>();

    public Milestone save(Milestone milestone) {
        storage.put(milestone.id(), milestone);
        return milestone;
    }

    public Optional<Milestone> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Milestone> findAll() {
        return List.copyOf(storage.values());
    }

    public List<Milestone> findByProject(UUID projectId) {
        return storage.values().stream()
                .filter(m -> m.projectId().equals(projectId))
                .toList();
    }
}
