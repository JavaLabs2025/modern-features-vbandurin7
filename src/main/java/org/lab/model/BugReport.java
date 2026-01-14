package org.lab.model;

import java.util.UUID;

public record BugReport(
        UUID id,
        UUID projectId,
        String description,
        BugStatus status,
        UUID assignedDeveloperId
) {

    public BugReport assignDeveloper(UUID developerId) {
        return new BugReport(
                id,
                projectId,
                description,
                status,
                developerId
        );
    }

    public BugReport changeStatus(BugStatus newStatus) {
        return new BugReport(
                id,
                projectId,
                description,
                newStatus,
                assignedDeveloperId
        );
    }

    public boolean isOpen() {
        return status != BugStatus.CLOSED;
    }
}
