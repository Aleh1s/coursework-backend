package ua.palamar.courseworkbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequest;
import ua.palamar.courseworkbackend.dto.request.UpdateAdvertisementRequest;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsDetailsResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;
import ua.palamar.courseworkbackend.dto.response.UpdateAdvertisementResponse;

import javax.servlet.http.HttpServletRequest;

@Service
public interface AdvertisementService {

    AdvertisementResponse createAdvertisement(AdvertisementRequest advertisementRequest, HttpServletRequest request, MultipartFile file);

    AdvertisementsResponse getAllByCriteria(AdvertisementCriteria criteria);

    AdvertisementResponse getByIdJoinFetchCreator(String id);

    AdvertisementsDetailsResponse getAllByEmailAndCriteria(String email, AdvertisementCriteria advertisementCriteria);

    void removeAdvertisement(String id, HttpServletRequest request);

    UpdateAdvertisementResponse updateAdvertisement(UpdateAdvertisementRequest request, HttpServletRequest httpServletRequest);
}
