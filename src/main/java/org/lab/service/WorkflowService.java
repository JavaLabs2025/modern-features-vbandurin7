package org.lab.service;

import org.lab.model.BugReport;
import org.lab.model.Milestone;
import org.lab.model.Project;
import org.lab.model.Ticket;
import org.lab.model.TicketStatus;
import org.lab.model.User;
import org.lab.workflow.Action;
import org.lab.workflow.AssignDeveloper;
import org.lab.workflow.CloseBug;
import org.lab.workflow.CompleteWork;
import org.lab.workflow.CreateTicket;
import org.lab.workflow.FixBug;
import org.lab.workflow.ReportBug;
import org.lab.workflow.StartWork;
import org.lab.workflow.TestBug;

public class WorkflowService {

    private final TicketService ticketService;
    private final BugReportService bugService;

    public WorkflowService(
            TicketService ticketService,
            BugReportService bugService
    ) {
        this.ticketService = ticketService;
        this.bugService = bugService;
    }

    @SuppressWarnings("unchecked")
    public <T> T apply(
            User user,
            Project project,
            Object target,
            Action<T> action
    ) {
        return switch (action) {

            case CreateTicket ct -> (T) ticketService.createTicket(
                    user, project, (Milestone) target,
                    ct.title(), ct.description()
            );

            case AssignDeveloper ad -> (T) ticketService.assignDeveloper(
                    user, project, (Ticket) target,
                    new User(ad.developerId(), "tmp")
            );

            case StartWork _ -> (T) ticketService.updateStatus(
                    user, project, (Ticket) target,
                    TicketStatus.IN_PROGRESS
            );

            case CompleteWork _ -> (T) ticketService.updateStatus(
                    user, project, (Ticket) target,
                    TicketStatus.DONE
            );

            case ReportBug rb -> (T) bugService.createBug(
                    user, project, rb.description()
            );

            case FixBug _ -> (T) bugService.fixBug(
                    user, project, (BugReport) target
            );

            case TestBug _ -> (T) bugService.testBug(
                    user, project, (BugReport) target
            );

            case CloseBug _ -> (T) bugService.closeBug(
                    user, project, (BugReport) target
            );
        };
    }
}
