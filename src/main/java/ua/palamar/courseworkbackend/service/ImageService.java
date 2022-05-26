package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.entity.image.Image;

public interface ImageService {

    Image getImageByAdvertisementId(String advertisementId);

}
