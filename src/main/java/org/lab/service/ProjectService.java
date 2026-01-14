package org.lab.service;

import org.lab.model.*;
import org.lab.repository.ProjectRepository;

import java.util.List;
import java.util.UUID;

public class ProjectService {

    private final ProjectRepository repository;
    private final RoleValidationService roleValidator;

    public ProjectService(
            ProjectRepository repository,
            RoleValidationService roleValidator
    ) {
        this.repository = repository;
        this.roleValidator = roleValidator;
    }

    public Project createProject(User creator, String name) {
        Project project = new Project(
                UUID.randomUUID(),
                name,
                List.of(new ProjectMembership(creator.id(), Role.MANAGER)),
                List.of(),
                List.of()
        );
        repository.save(project);
        return project;
    }

    public Project addMember(
            Project project,
            User manager,
            User user,
            Role role
    ) {
        roleValidator.requireRole(project, manager, Role.MANAGER);

        Project updated = project.addMember(user.id(), role);
        repository.save(updated);
        return updated;
    }
}
