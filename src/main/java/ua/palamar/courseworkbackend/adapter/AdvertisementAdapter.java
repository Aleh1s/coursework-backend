package ua.palamar.courseworkbackend.adapter;

import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.HouseAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ServiceAdvertisementEntity;

public interface AdvertisementAdapter {

    ItemAdvertisementEntity getItemAdvertisement(AdvertisementModel advertisementModel);
    ServiceAdvertisementEntity getServiceAdvertisement(AdvertisementModel advertisementModel);
    HouseAdvertisementEntity getHouseAdvertisement(AdvertisementModel advertisementModel);

}
