package ua.palamar.courseworkbackend.factory;

import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;

public interface AdvertisementFactory {

    Advertisement createAdvertisement(AdvertisementRequestModel advertisementRequestModel, String email);

}
