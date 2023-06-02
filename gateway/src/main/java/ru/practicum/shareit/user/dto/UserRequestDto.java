package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.practicum.shareit.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequestDto {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 100)
    private String name;

    @NotBlank(groups = Marker.OnCreate.class)
    @Email
    @Size(max = 320)
    private String email;
}
