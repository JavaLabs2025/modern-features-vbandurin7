package org.lab.model;

import java.util.UUID;

public record ProjectMembership(UUID userId, Role role) {}
