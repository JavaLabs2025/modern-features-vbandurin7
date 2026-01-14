package org.lab.service;

import org.junit.jupiter.api.Test;
import org.lab.model.*;
import org.lab.repository.BugReportRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BugReportServiceTest {

    @Test
    void bugLifecycleHappyPath() {
        BugReportRepository repo = new BugReportRepository();
        RoleValidationService validator = new RoleValidationService();
        BugReportService service = new BugReportService(repo, validator);

        User dev = new User(UUID.randomUUID(), "Dev");
        User tester = new User(UUID.randomUUID(), "Tester");

        Project project = new Project(
                UUID.randomUUID(),
                "Test",
                java.util.List.of(
                        new ProjectMembership(dev.id(), Role.DEVELOPER),
                        new ProjectMembership(tester.id(), Role.TESTER)
                ),
                java.util.List.of(),
                java.util.List.of()
        );

        BugReport bug = service.createBug(tester, project, "Bug");
        bug = service.fixBug(dev, project, bug);
        bug = service.testBug(tester, project, bug);
        bug = service.closeBug(tester, project, bug);

        assertEquals(BugStatus.CLOSED, bug.status());
        assertFalse(bug.isOpen());
    }
}
