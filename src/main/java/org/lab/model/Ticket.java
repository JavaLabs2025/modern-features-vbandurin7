package org.lab.model;

import java.util.UUID;

public record Ticket(
        UUID id,
        UUID projectId,
        UUID milestoneId,
        String title,
        String description,
        TicketStatus status,
        UUID assignedDeveloperId
) {

    public Ticket assignDeveloper(UUID developerId) {
        return new Ticket(
                id,
                projectId,
                milestoneId,
                title,
                description,
                status,
                developerId
        );
    }

    public Ticket changeStatus(TicketStatus newStatus) {
        return new Ticket(
                id,
                projectId,
                milestoneId,
                title,
                description,
                newStatus,
                assignedDeveloperId
        );
    }

    public boolean isCompleted() {
        return status == TicketStatus.DONE;
    }
}
