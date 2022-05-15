package ua.palamar.courseworkbackend.dto.response;

import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

import java.util.Set;

public record MySalesResponse(
        Advertisement advertisement,
        UserResponseModel creator,
        Set<OrderDetailsModelResponse> orders
) {

}
