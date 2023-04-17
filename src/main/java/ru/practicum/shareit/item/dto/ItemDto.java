package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ItemDto {
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    private Boolean available;

}
