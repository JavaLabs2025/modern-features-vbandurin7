package org.lab.service;

import org.junit.jupiter.api.Test;
import org.lab.model.*;
import org.lab.repository.TicketRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketServiceTest {

    @Test
    void developerCanCompleteAssignedTicket() {
        TicketRepository repo = new TicketRepository();
        RoleValidationService validator = new RoleValidationService();
        TicketService service = new TicketService(repo, validator);

        User manager = new User(UUID.randomUUID(), "Manager");
        User dev = new User(UUID.randomUUID(), "Dev");

        Project project = new Project(
                UUID.randomUUID(),
                "Test",
                java.util.List.of(
                        new ProjectMembership(manager.id(), Role.MANAGER),
                        new ProjectMembership(dev.id(), Role.DEVELOPER)
                ),
                java.util.List.of(),
                java.util.List.of()
        );

        Milestone milestone = new Milestone(
                UUID.randomUUID(),
                project.id(),
                "M1",
                java.time.LocalDate.now(),
                java.time.LocalDate.now().plusDays(7),
                MilestoneStatus.ACTIVE,
                java.util.List.of()
        );

        Ticket ticket = service.createTicket(
                manager, project, milestone, "Task", "Desc"
        );

        ticket = service.assignDeveloper(manager, project, ticket, dev);
        ticket = service.updateStatus(dev, project, ticket, TicketStatus.DONE);

        assertTrue(ticket.isCompleted());
    }
}
