package org.lab.service;

import org.lab.model.*;
import org.lab.repository.TicketRepository;

import java.util.UUID;

public class TicketService {

    private final TicketRepository repository;
    private final RoleValidationService roleValidator;

    public TicketService(
            TicketRepository repository,
            RoleValidationService roleValidator
    ) {
        this.repository = repository;
        this.roleValidator = roleValidator;
    }

    public Ticket createTicket(
            User user,
            Project project,
            Milestone milestone,
            String title,
            String description
    ) {
        roleValidator.requireRole(project, user, Role.MANAGER, Role.TEAM_LEAD);

        Ticket ticket = new Ticket(
                UUID.randomUUID(),
                project.id(),
                milestone.id(),
                title,
                description,
                TicketStatus.NEW,
                null
        );

        repository.save(ticket);
        return ticket;
    }

    public Ticket assignDeveloper(
            User user,
            Project project,
            Ticket ticket,
            User developer
    ) {
        roleValidator.requireRole(project, user, Role.MANAGER, Role.TEAM_LEAD);

        Ticket updated = ticket.assignDeveloper(developer.id());
        repository.save(updated);
        return updated;
    }

    public Ticket updateStatus(
            User developer,
            Project project,
            Ticket ticket,
            TicketStatus newStatus
    ) {
        roleValidator.requireRole(project, developer, Role.DEVELOPER);

        if (!developer.id().equals(ticket.assignedDeveloperId())) {
            throw new RuntimeException("Тикет назначен другому разработчику");
        }

        Ticket updated = ticket.changeStatus(newStatus);
        repository.save(updated);
        return updated;
    }

    public boolean isCompleted(
            User user,
            Project project,
            Ticket ticket
    ) {
        roleValidator.requireRole(project, user, Role.MANAGER, Role.TEAM_LEAD);
        return ticket.status() == TicketStatus.DONE;
    }
}
