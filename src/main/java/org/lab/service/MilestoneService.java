package org.lab.service;

import org.lab.model.*;
import org.lab.repository.MilestoneRepository;
import org.lab.repository.TicketRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MilestoneService {

    private final MilestoneRepository milestoneRepo;
    private final TicketRepository ticketRepo;
    private final RoleValidationService roleValidator;

    public MilestoneService(
            MilestoneRepository milestoneRepo,
            TicketRepository ticketRepo,
            RoleValidationService roleValidator
    ) {
        this.milestoneRepo = milestoneRepo;
        this.ticketRepo = ticketRepo;
        this.roleValidator = roleValidator;
    }

    public Milestone createMilestone(
            User manager,
            Project project,
            String name,
            LocalDate start,
            LocalDate end
    ) {
        roleValidator.requireRole(project, manager, Role.MANAGER);

        boolean activeExists = milestoneRepo.findByProject(project.id()).stream()
                .anyMatch(m -> m.status() == MilestoneStatus.ACTIVE);

        if (activeExists) {
            throw new IllegalStateException("Активный майлстоун уже существует");
        }

        Milestone milestone = new Milestone(
                UUID.randomUUID(),
                project.id(),
                name,
                start,
                end,
                MilestoneStatus.ACTIVE,
                List.of()
        );

        milestoneRepo.save(milestone);
        return milestone;
    }

    public Milestone closeMilestone(
            User manager,
            Project project,
            Milestone milestone
    ) {
        roleValidator.requireRole(project, manager, Role.MANAGER);

        if (!milestone.canBeClosed(ticketRepo.findAll())) {
            throw new IllegalStateException("Есть невыполненные тикеты");
        }

        Milestone closed = milestone.changeStatus(MilestoneStatus.CLOSED);
        milestoneRepo.save(closed);
        return closed;
    }
}

