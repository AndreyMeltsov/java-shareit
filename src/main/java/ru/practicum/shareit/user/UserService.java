package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {
    List<UserDto> findAll();

    UserDto create(User user);

    UserDto update(Long id, Map<String, Object> fields);

    UserDto findUserById(Long id);

    void deleteUser(Long id);
}
