package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.response.ItemAdvertisementResponse;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.user.UserInfo;

public interface ItemAdvertisementAdapter {

    ItemAdvertisementResponse getResponse(ItemAdvertisementEntity advertisement, UserInfo userInfo);

}
