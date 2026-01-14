package org.lab.workflow;

import org.lab.model.Ticket;

public record CreateTicket(
        String title,
        String description
) implements Action<Ticket> {}
