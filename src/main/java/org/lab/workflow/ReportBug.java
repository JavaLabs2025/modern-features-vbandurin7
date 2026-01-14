package org.lab.workflow;

import org.lab.model.BugReport;

public record ReportBug(
        String description
) implements Action<BugReport> {}
