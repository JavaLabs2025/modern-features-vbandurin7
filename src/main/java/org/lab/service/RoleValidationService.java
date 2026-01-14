package org.lab.service;

import org.lab.model.Project;
import org.lab.model.Role;
import org.lab.model.User;

import java.util.Arrays;

public class RoleValidationService {

    public void requireRole(Project project, User user, Role... allowedRoles) {
        Role actualRole = project.getUserRole(user.id())
                .orElseThrow(() ->
                        new RuntimeException("Пользователь не участвует в проекте"));

        boolean allowed = Arrays.stream(allowedRoles)
                .anyMatch(r -> r == actualRole);

        if (!allowed) {
            throw new RuntimeException(STR."Недостаточно прав. Роль пользователя: \{actualRole}");
        }
    }
}
