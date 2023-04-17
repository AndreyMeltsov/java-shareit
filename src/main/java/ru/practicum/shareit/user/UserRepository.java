package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User create(User user);

    void update(User user);

    User findUserById(Long id);

    void deleteUser(Long id);
}
