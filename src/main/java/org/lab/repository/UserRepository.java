package org.lab.repository;

import org.lab.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    public User save(User user) {
        users.put(user.id(), user);
        return user;
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    public boolean existsById(UUID id) {
        return users.containsKey(id);
    }
}
