package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.entity.image.ImageEntity;

public interface ImageService {

    ImageEntity getImageByAdvertisementId(String advertisementId);

}
