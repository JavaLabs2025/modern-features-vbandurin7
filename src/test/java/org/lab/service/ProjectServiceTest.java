package org.lab.service;

import org.junit.jupiter.api.Test;
import org.lab.model.*;
import org.lab.repository.ProjectRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    @Test
    void managerCanAddMembers() {
        ProjectRepository projectRepo = new ProjectRepository();
        RoleValidationService validator = new RoleValidationService();
        ProjectService projectService = new ProjectService(projectRepo, validator);

        User manager = new User(UUID.randomUUID(), "Manager");
        User dev = new User(UUID.randomUUID(), "Dev");

        Project project = projectService.createProject(manager, "Test");

        project = projectService.addMember(project, manager, dev, Role.DEVELOPER);

        assertEquals(
                Role.DEVELOPER,
                project.getUserRole(dev.id()).orElseThrow()
        );
    }

    @Test
    void nonManagerCannotAddMembers() {
        ProjectRepository projectRepo = new ProjectRepository();
        RoleValidationService validator = new RoleValidationService();
        ProjectService projectService = new ProjectService(projectRepo, validator);

        User manager = new User(UUID.randomUUID(), "Manager");
        User outsider = new User(UUID.randomUUID(), "Outsider");

        Project project = projectService.createProject(manager, "Test");

        assertThrows(
                RuntimeException.class,
                () -> projectService.addMember(project, outsider, manager, Role.DEVELOPER)
        );
    }
}
