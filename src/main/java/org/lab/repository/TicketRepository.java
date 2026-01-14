package org.lab.repository;

import org.lab.model.Ticket;
import org.lab.model.TicketStatus;

import java.util.*;
import java.util.stream.Collectors;

public class TicketRepository {
    private final Map<UUID, Ticket> storage = new HashMap<>();

    public Ticket save(Ticket ticket) {
        storage.put(ticket.id(), ticket);
        return ticket;
    }

    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Ticket> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Ticket> findByDeveloper(UUID developerId) {
        return storage.values().stream()
                .filter(t -> t.assignedDeveloperId() != null &&  t.assignedDeveloperId().equals(developerId))
                .collect(Collectors.toList());
    }

    public List<Ticket> findByStatus(TicketStatus status) {
        return storage.values().stream()
                .filter(t -> t.status() == status)
                .collect(Collectors.toList());
    }
}
