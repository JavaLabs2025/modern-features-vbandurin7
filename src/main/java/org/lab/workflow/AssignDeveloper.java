package org.lab.workflow;

import java.util.UUID;

import org.lab.model.Ticket;

public record AssignDeveloper(
        UUID developerId
) implements Action<Ticket> {}
