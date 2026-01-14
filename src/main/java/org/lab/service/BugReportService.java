package org.lab.service;

import org.lab.model.*;
import org.lab.repository.BugReportRepository;

import java.util.UUID;

public class BugReportService {

    private final BugReportRepository repository;
    private final RoleValidationService roleValidator;

    public BugReportService(
            BugReportRepository repository,
            RoleValidationService roleValidator
    ) {
        this.repository = repository;
        this.roleValidator = roleValidator;
    }

    public BugReport createBug(
            User user,
            Project project,
            String description
    ) {
        roleValidator.requireRole(project, user, Role.DEVELOPER, Role.TESTER);

        BugReport bug = new BugReport(
                UUID.randomUUID(),
                project.id(),
                description,
                BugStatus.NEW,
                null
        );

        repository.save(bug);
        return bug;
    }

    public BugReport fixBug(
            User developer,
            Project project,
            BugReport bug
    ) {
        roleValidator.requireRole(project, developer, Role.DEVELOPER);

        BugReport updated = bug
                .assignDeveloper(developer.id())
                .changeStatus(BugStatus.FIXED);

        repository.save(updated);
        return updated;
    }

    public BugReport testBug(
            User tester,
            Project project,
            BugReport bug
    ) {
        roleValidator.requireRole(project, tester, Role.TESTER);

        BugReport updated = bug.changeStatus(BugStatus.TESTED);
        repository.save(updated);
        return updated;
    }

    public BugReport closeBug(
            User tester,
            Project project,
            BugReport bug
    ) {
        roleValidator.requireRole(project, tester, Role.TESTER);

        BugReport updated = bug.changeStatus(BugStatus.CLOSED);
        repository.save(updated);
        return updated;
    }
}