package org.lab.workflow;

public sealed interface Action<T>
        permits CreateTicket,
        AssignDeveloper,
        StartWork,
        CompleteWork,
        ReportBug,
        FixBug,
        TestBug,
        CloseBug {
}

