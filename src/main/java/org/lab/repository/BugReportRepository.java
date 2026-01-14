package org.lab.repository;

import org.lab.model.BugReport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BugReportRepository {

    private final Map<UUID, BugReport> storage = new ConcurrentHashMap<>();

    public BugReport save(BugReport bug) {
        storage.put(bug.id(), bug);
        return bug;
    }

    public List<BugReport> findAll() {
        return List.copyOf(storage.values());
    }

    public List<BugReport> findByProject(UUID projectId) {
        return storage.values().stream()
                .filter(b -> b.projectId().equals(projectId))
                .toList();
    }
}
