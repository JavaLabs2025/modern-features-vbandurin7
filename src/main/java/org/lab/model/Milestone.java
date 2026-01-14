package org.lab.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record Milestone(
        UUID id,
        UUID projectId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        MilestoneStatus status,
        List<UUID> ticketIds
) {

    public Milestone addTicket(UUID ticketId) {
        var updated = new java.util.ArrayList<>(ticketIds);
        updated.add(ticketId);

        return new Milestone(
                id,
                projectId,
                name,
                startDate,
                endDate,
                status,
                List.copyOf(updated)
        );
    }

    public boolean canBeClosed(List<Ticket> tickets) {
        return tickets.stream()
                .filter(t -> ticketIds.contains(t.id()))
                .allMatch(Ticket::isCompleted);
    }

    public Milestone changeStatus(MilestoneStatus newStatus) {
        return new Milestone(
                id,
                projectId,
                name,
                startDate,
                endDate,
                newStatus,
                ticketIds
        );
    }

    public boolean isActive() {
        return status == MilestoneStatus.ACTIVE;
    }
}
