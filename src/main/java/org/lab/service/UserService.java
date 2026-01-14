package org.lab.service;

import org.lab.model.*;
import org.lab.repository.*;

import java.util.List;
import java.util.UUID;

public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;
    private final BugReportRepository bugRepository;

    public UserService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TicketRepository ticketRepository,
            BugReportRepository bugRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
        this.bugRepository = bugRepository;
    }

    public User register(String name) {
        User user = new User(UUID.randomUUID(), name);
        return userRepository.save(user);
    }

    public List<Project> getUserProjects(User user) {
        return projectRepository.findAll().stream()
                .filter(p -> p.getUserRole(user.id()).isPresent())
                .toList();
    }

    public List<Ticket> getUserTickets(User user) {
        return ticketRepository.findAll().stream()
                .filter(t -> t.assignedDeveloperId() != null && user.id().equals(t.assignedDeveloperId()))
                .toList();
    }

    public List<BugReport> getBugsToFix(User user) {
        return bugRepository.findAll().stream()
                .filter(b -> user.id().equals(b.assignedDeveloperId()))
                .filter(b -> b.status() == BugStatus.NEW || b.status() == BugStatus.FIXED)
                .toList();
    }
}
