package org.lab;

import org.lab.model.*;
import org.lab.repository.*;
import org.lab.service.*;
import org.lab.workflow.*;

import java.time.LocalDate;

public class Main {
    public static void main() {

        UserRepository userRepo = new UserRepository();
        ProjectRepository projectRepo = new ProjectRepository();
        MilestoneRepository milestoneRepo = new MilestoneRepository();
        TicketRepository ticketRepo = new TicketRepository();
        BugReportRepository bugRepo = new BugReportRepository();

        RoleValidationService roleValidator = new RoleValidationService();

        UserService userService = new UserService(userRepo, projectRepo, ticketRepo, bugRepo);
        ProjectService projectService = new ProjectService(projectRepo, roleValidator);
        MilestoneService milestoneService = new MilestoneService(milestoneRepo, ticketRepo, roleValidator);
        TicketService ticketService = new TicketService(ticketRepo, roleValidator);
        BugReportService bugService = new BugReportService(bugRepo, roleValidator);

        WorkflowService workflow = new WorkflowService(ticketService, bugService);

        User manager = userService.register("Alice (Manager)");
        User dev = userService.register("Bob (Developer)");
        User tester = userService.register("Eve (Tester)");

        Project project = projectService.createProject(manager, "Enterprise Project");
        project = projectService.addMember(project, manager, dev, Role.DEVELOPER);
        project = projectService.addMember(project, manager, tester, Role.TESTER);

        Milestone milestone = milestoneService.createMilestone(manager, project, "Milestone 1",
                LocalDate.now(), LocalDate.now().plusDays(14));

        Ticket ticket = workflow.apply(manager, project, milestone,
                new CreateTicket("Implement Auth", "JWT-based login/logout"));

        ticket = workflow.apply(manager, project, ticket,
                new AssignDeveloper(dev.id()));

        ticket = workflow.apply(dev, project, ticket, new StartWork());
        ticket = workflow.apply(dev, project, ticket, new CompleteWork());

        System.out.println("Ticket status after completion: " + ticket.status());

        BugReport bug = workflow.apply(tester, project, null,
                new ReportBug("Login fails on empty password"));

        bug = workflow.apply(dev, project, bug, new FixBug());
        bug = workflow.apply(tester, project, bug, new TestBug());
        bug = workflow.apply(tester, project, bug, new CloseBug());

        System.out.println("Bug status after closure: " + bug.status());

        milestone = milestoneService.closeMilestone(manager, project, milestone);

        String report = """
                ===== PROJECT REPORT =====
                Project: %s
                Milestone: %s
                Milestone status: %s
                Ticket: %s
                Ticket status: %s
                Bug status: %s
                =========================
                """.formatted(
                project.name(),
                milestone.name(),
                milestone.status(),
                ticket.title(),
                ticket.status(),
                bug.status()
        );

        System.out.println(report);
    }
}
