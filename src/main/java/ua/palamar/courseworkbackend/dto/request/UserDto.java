package ua.palamar.courseworkbackend.dto.request;

import java.util.Optional;

public record UserDto(
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> phoneNumber
) {
}
