package org.lab.model;

import java.util.*;

public record Project(
        UUID id,
        String name,
        List<ProjectMembership> memberships,
        List<Milestone> milestones,
        List<BugReport> bugReports
) {

    public Project addMember(UUID userId, Role role) {
        var updated = new ArrayList<>(memberships);
        updated.add(new ProjectMembership(userId, role));

        return new Project(
                id,
                name,
                List.copyOf(updated),
                milestones,
                bugReports
        );
    }

    public Optional<Role> getUserRole(UUID userId) {
        return memberships.stream()
                .filter(m -> m.userId().equals(userId))
                .map(ProjectMembership::role)
                .findFirst();
    }
}
