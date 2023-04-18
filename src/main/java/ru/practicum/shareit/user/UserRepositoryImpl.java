package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private long id;

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public User create(User user) {
        if (users.stream().anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail()))) {
            throw new AlreadyExistException("User with such email has already existed");
        }
        user.setId(++id);
        users.add(user);
        return user;
    }

    @Override
    public void update(User user) {
        if (users.stream()
                .filter(u -> !Objects.equals(u.getId(), user.getId()))
                .anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail()))) {
            throw new AlreadyExistException("User with such email has already existed");
        }
        users.stream()
                .filter(u -> Objects.equals(u.getId(), user.getId()))
                .findFirst()
                .ifPresent(value -> users.set(users.indexOf(value), user));
    }

    @Override
    public User findUserById(Long id) {
        return users.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User with such id wasn't found"));
    }

    @Override
    public void deleteUser(Long id) {
        users.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst()
                .ifPresent(users::remove);
    }
}
