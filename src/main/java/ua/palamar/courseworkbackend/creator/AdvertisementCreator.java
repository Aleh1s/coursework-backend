package ua.palamar.courseworkbackend.creator;

import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

public interface AdvertisementCreator {

    Advertisement createAdvertisement(AdvertisementModel advertisementModel);

}
