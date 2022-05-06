package ua.palamar.courseworkbackend.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.Category;
import ua.palamar.courseworkbackend.repository.AdvertisementsRepository;

import java.util.Calendar;
import java.util.List;

@Service
public class SimpleGeneralizedAdvertisementService implements ua.palamar.courseworkbackend.service.GeneralizedAdvertisementService {

    private final AdvertisementsRepository advertisementsRepository;

    @Autowired
    public SimpleGeneralizedAdvertisementService(AdvertisementsRepository advertisementsRepository) {
        this.advertisementsRepository = advertisementsRepository;
    }

    public ResponseEntity<?> getAllAdvertisementsByCategory(Category category) {
        List<Advertisement> advertisements = advertisementsRepository.findAllByCategory(category);
        return new ResponseEntity<>(advertisements, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getAllAdvertisementsByEmail(String email) {
        List<Advertisement> advertisements = advertisementsRepository.findAllByCreatedByEmail(email);
        return new ResponseEntity<>(advertisements, HttpStatus.ACCEPTED);
    }
}
