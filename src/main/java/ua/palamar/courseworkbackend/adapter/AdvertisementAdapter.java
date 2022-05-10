package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.HouseAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ServiceAdvertisementEntity;

public interface AdvertisementAdapter {

    ItemAdvertisementEntity getItemAdvertisement(AdvertisementRequestModel advertisementRequestModel, String email);
    ServiceAdvertisementEntity getServiceAdvertisement(AdvertisementRequestModel advertisementRequestModel);
    HouseAdvertisementEntity getHouseAdvertisement(AdvertisementRequestModel advertisementRequestModel);

}
