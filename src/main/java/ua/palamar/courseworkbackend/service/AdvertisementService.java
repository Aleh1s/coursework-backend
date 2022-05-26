package ua.palamar.courseworkbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.palamar.courseworkbackend.dto.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.request.AdvertisementRequestModel;
import ua.palamar.courseworkbackend.dto.response.AdvertisementResponse;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Service
public interface AdvertisementService {

    AdvertisementResponse save(AdvertisementRequestModel advertisementRequestModel, HttpServletRequest request, MultipartFile file);

    AdvertisementsResponse getAllByCriteria(AdvertisementCriteria criteria);

    AdvertisementResponse getByIdJoinFetchCreator(String id);

    Set<AdvertisementResponse> getAllByEmail(String email);

    void remove(String id, HttpServletRequest request);
}
