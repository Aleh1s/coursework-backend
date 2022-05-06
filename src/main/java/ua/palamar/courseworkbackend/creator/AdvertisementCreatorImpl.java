package ua.palamar.courseworkbackend.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.AdvertisementAdapter;
import ua.palamar.courseworkbackend.dto.AdvertisementModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.HouseAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ServiceAdvertisementEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.ItemAdvertisementRepository;

@Component
public class AdvertisementCreatorImpl implements AdvertisementCreator {

    private final AdvertisementAdapter advertisementAdapter;
    private final ItemAdvertisementRepository itemAdvertisementRepository;

    @Autowired
    public AdvertisementCreatorImpl(AdvertisementAdapter advertisementAdapter,
                                    ItemAdvertisementRepository itemAdvertisementRepository) {
        this.advertisementAdapter = advertisementAdapter;
        this.itemAdvertisementRepository = itemAdvertisementRepository;
    }

    @Override
    public Advertisement createAdvertisement(AdvertisementModel advertisementModel, String email) {
        String category = advertisementModel.category();

        return switch (category) {
            case "ITEM" -> {
                ItemAdvertisementEntity advertisement
                        = advertisementAdapter.getItemAdvertisement(advertisementModel, email);
                itemAdvertisementRepository.save(advertisement);
                yield advertisement;
            }
            case "SERVICE" -> {
                ServiceAdvertisementEntity advertisement
                        = advertisementAdapter.getServiceAdvertisement(advertisementModel);
                // todo: save advertisement
                yield advertisement;
            }
            case "HOUSE" -> {
                advertisementAdapter.getHouseAdvertisement(advertisementModel);
                HouseAdvertisementEntity advertisement
                        = advertisementAdapter.getHouseAdvertisement(advertisementModel);
                // todo: save advertisement
                yield advertisement;
            }
            default -> throw new ApiRequestException(
                    String.format("Invalid category %s", category)
            );
        };
    }
}
