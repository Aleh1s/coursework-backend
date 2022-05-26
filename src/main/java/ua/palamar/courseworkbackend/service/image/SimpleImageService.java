package ua.palamar.courseworkbackend.service.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.image.ImageEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementRepository;
import ua.palamar.courseworkbackend.service.ImageService;

@Service
public class SimpleImageService implements ImageService {

    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public SimpleImageService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public ImageEntity getImageByAdvertisementId(String advertisementId) {
        Advertisement advertisement = advertisementRepository.findAdvertisementByIdJoinFetchImage(advertisementId)
                .orElseThrow(() -> new ApiRequestException(
                        String.format(
                                "Advertisement with id %s does not exist", advertisementId
                        )
                ));

        return advertisement.getImage();
    }
}
