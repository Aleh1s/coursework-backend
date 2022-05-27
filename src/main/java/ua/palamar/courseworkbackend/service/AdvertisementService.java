package ua.palamar.courseworkbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequest;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
public interface AdvertisementService {

    AdvertisementResponse save(AdvertisementRequest advertisementRequest, HttpServletRequest request, MultipartFile file);

    AdvertisementsResponse getAllByCriteria(AdvertisementCriteria criteria);

    AdvertisementResponse getByIdJoinFetchCreator(String id);

    AdvertisementsDetailsResponse getAllByEmail(String email, AdvertisementCriteria advertisementCriteria);

    void remove(String id, HttpServletRequest request);
}
