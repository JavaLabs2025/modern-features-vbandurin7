package org.lab.service;

import org.junit.jupiter.api.Test;
import org.lab.model.*;
import org.lab.repository.*;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MilestoneServiceTest {

    @Test
    void onlyOneActiveMilestoneAllowed() {
        MilestoneRepository milestoneRepo = new MilestoneRepository();
        TicketRepository ticketRepo = new TicketRepository();
        RoleValidationService validator = new RoleValidationService();

        MilestoneService service =
                new MilestoneService(milestoneRepo, ticketRepo, validator);

        User manager = new User(UUID.randomUUID(), "Manager");

        Project project = new Project(
                UUID.randomUUID(),
                "Test",
                java.util.List.of(new ProjectMembership(manager.id(), Role.MANAGER)),
                java.util.List.of(),
                java.util.List.of()
        );

        service.createMilestone(
                manager, project, "M1",
                LocalDate.now(), LocalDate.now().plusDays(5)
        );

        assertThrows(
                IllegalStateException.class,
                () -> service.createMilestone(
                        manager, project, "M2",
                        LocalDate.now(), LocalDate.now().plusDays(5)
                )
        );
    }
}
