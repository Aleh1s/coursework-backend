package ua.palamar.courseworkbackend.service.itemAdvertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.advertisement.ItemAdvertisementEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.ItemAdvertisementRepository;
import ua.palamar.courseworkbackend.service.ItemAdvertisementService;

@Component
public class SimpleItemAdvertisementService implements ItemAdvertisementService {

    private final ItemAdvertisementRepository itemAdvertisementRepository;

    @Autowired
    public SimpleItemAdvertisementService(ItemAdvertisementRepository itemAdvertisementRepository) {
        this.itemAdvertisementRepository = itemAdvertisementRepository;
    }

    @Override
    public void removeById(String id) {
    }

    @Override
    public ItemAdvertisementEntity getById(String id) {
        return itemAdvertisementRepository.getItemAdvertisementEntityById(id)
                .orElseThrow(() -> new ApiRequestException(
                        String.format("Item with id %s does not exist", id)
                ));
    }
}
