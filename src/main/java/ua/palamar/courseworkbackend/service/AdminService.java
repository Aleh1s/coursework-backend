package ua.palamar.courseworkbackend.service;

import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.user.UserStatus;

import javax.servlet.http.HttpServletRequest;

public interface AdminService {

    void changeUserAccountStatus(String email, UserStatus status);
    void changeAdvertisementsStatus(String id, AdvertisementStatus status, HttpServletRequest request);
    AdvertisementsResponse getAdvertisementsForModeration(AdvertisementCriteria criteria);
}
