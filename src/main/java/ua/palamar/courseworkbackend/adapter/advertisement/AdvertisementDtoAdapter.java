package ua.palamar.courseworkbackend.adapter.advertisement;

import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.user.UserEntity;

public interface AdvertisementDtoAdapter {

    AdvertisementResponse getModel(
            Advertisement advertisement,
            UserEntity creator
    );

}
