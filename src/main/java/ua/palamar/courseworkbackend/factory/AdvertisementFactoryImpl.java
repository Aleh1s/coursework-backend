package ua.palamar.courseworkbackend.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.adapter.AdvertisementAdapter;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.HouseAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.entity.advertisement.ServiceAdvertisementEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.ItemAdvertisementRepository;

@Component
public class AdvertisementFactoryImpl implements AdvertisementFactory {

    private final AdvertisementAdapter advertisementAdapter;
    private final ItemAdvertisementRepository itemAdvertisementRepository;

    @Autowired
    public AdvertisementFactoryImpl(AdvertisementAdapter advertisementAdapter,
                                    ItemAdvertisementRepository itemAdvertisementRepository) {
        this.advertisementAdapter = advertisementAdapter;
        this.itemAdvertisementRepository = itemAdvertisementRepository;
    }

    @Override
    public Advertisement createAdvertisement(AdvertisementRequestModel advertisementRequestModel, String email) {
        String category = advertisementRequestModel.category();

        return switch (category) {
            case "ITEM" -> {
                ItemAdvertisementEntity advertisement
                        = advertisementAdapter.getItemAdvertisement(advertisementRequestModel, email);
                itemAdvertisementRepository.save(advertisement);
                yield advertisement;
            }
            case "SERVICE" -> {
                ServiceAdvertisementEntity advertisement
                        = advertisementAdapter.getServiceAdvertisement(advertisementRequestModel);
                // todo: save advertisement
                yield advertisement;
            }
            case "HOUSE" -> {
                advertisementAdapter.getHouseAdvertisement(advertisementRequestModel);
                HouseAdvertisementEntity advertisement
                        = advertisementAdapter.getHouseAdvertisement(advertisementRequestModel);
                // todo: save advertisement
                yield advertisement;
            }
            default -> throw new ApiRequestException(
                    String.format("Invalid category %s", category)
            );
        };
    }
}
