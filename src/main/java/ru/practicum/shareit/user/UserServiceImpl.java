package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
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
    public UserDto update(Long id, UserDto userDto) {
        User user = UserMapper.toUser(findUserById(id));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
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
