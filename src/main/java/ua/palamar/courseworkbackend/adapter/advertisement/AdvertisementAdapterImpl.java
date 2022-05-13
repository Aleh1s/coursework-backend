package ua.palamar.courseworkbackend.adapter.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.AdvertisementAdapter;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.*;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.service.UserService;

import java.time.LocalDateTime;

import static ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementStatus.*;
import static ua.palamar.courseworkbackend.entity.advertisement.Category.*;

@Component
public class AdvertisementAdapterImpl implements AdvertisementAdapter {

    private final UserService userService;

    @Autowired
    public AdvertisementAdapterImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ItemAdvertisementEntity getItemAdvertisement(AdvertisementRequestModel advertisementRequestModel, String email) {
        UserEntity creator = userService.getUserEntityByEmail(email);

        int height = advertisementRequestModel.height()
                .orElseThrow(() -> new ApiRequestException("Invalid height"));
        int width = advertisementRequestModel.width()
                .orElseThrow(() -> new ApiRequestException("Invalid width"));
        int length = advertisementRequestModel.length()
                .orElseThrow(() -> new ApiRequestException("Invalid length"));

        DimensionsEntity dimensions = new DimensionsEntity(
                null,
                length,
                height,
                width
        );

        return new ItemAdvertisementEntity(
                null,
                advertisementRequestModel.title(),
                advertisementRequestModel.description(),
                ITEM,
                UNCONFIRMED,
                LocalDateTime.now(),
                null,
                creator,
                null,
                dimensions
        );
    }

    @Override
    public ServiceAdvertisementEntity getServiceAdvertisement(AdvertisementRequestModel advertisementRequestModel) {
        return null;
    }

    @Override
    public HouseAdvertisementEntity getHouseAdvertisement(AdvertisementRequestModel advertisementRequestModel) {
        return null;
    }
}
