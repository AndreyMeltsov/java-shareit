package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.user.dto.UserDto;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        log.info("Users quantity is: {}", users.size());
        return users;
    }

    @Override
    public UserDto create(User user) {
        user = userRepository.create(user);
        log.info("User is added: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, Map<String, Object> fields) {
        User user = UserMapper.toUser(findUserById(id));
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(User.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(Objects.requireNonNull(field), user, value);
        });
        userRepository.update(user);
        log.info("User was updated in DB. New user is: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto findUserById(Long id) {
        User user = userRepository.findUserById(id);
        log.info("User was found in DB: {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
        log.info("User with id{} was deleted", id);
    }
}
