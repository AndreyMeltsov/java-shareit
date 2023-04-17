package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        if (users.stream().anyMatch(x -> Objects.equals(x.getEmail(), user.getEmail()))) {
            throw new AlreadyExistException("User with such email has already existed");
        }
        user.setId(++id);
        users.add(user);
        return user;
    }

    @Override
    public void update(User user) {
        if (users.stream()
                .filter(x -> !Objects.equals(x.getId(), user.getId()))
                .anyMatch(x -> Objects.equals(x.getEmail(), user.getEmail()))) {
            throw new AlreadyExistException("User with such email has already existed");
        }
        Optional<User> oldUser = users.stream()
                .filter(x -> Objects.equals(x.getId(), user.getId()))
                .findFirst();
        oldUser.ifPresent(value -> users.set(users.indexOf(value), user));
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = users.stream().filter(x -> Objects.equals(x.getId(), id)).findFirst();
        return user.orElseThrow(() -> new NotFoundException("User with such id wasn't found"));
    }

    @Override
    public void deleteUser(Long id) {
        Optional<User> user = users.stream().filter(x -> Objects.equals(x.getId(), id)).findFirst();
        user.ifPresent(users::remove);
    }
}
