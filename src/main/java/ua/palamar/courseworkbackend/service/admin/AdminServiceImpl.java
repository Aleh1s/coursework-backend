package ua.palamar.courseworkbackend.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.dto.criteria.AdvertisementCriteria;
import ua.palamar.courseworkbackend.dto.response.AdvertisementsResponse;
import ua.palamar.courseworkbackend.entity.advertisement.Advertisement;
import ua.palamar.courseworkbackend.entity.advertisement.AdvertisementStatus;
import ua.palamar.courseworkbackend.entity.user.UserStatus;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.AdvertisementRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.AdminService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdminServiceImpl(
            UserRepository userRepository,
            AdvertisementRepository advertisementRepository
    ) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public void changeUserAccountStatus(String email, UserStatus status) {
        if (userRepository.existsByEmail(email)) {
            userRepository.updateUserStatusById(email, status);
        } else {
            throw new ApiRequestException(
                    String.format("User with email %s does not exist", email)
            );
        }
    }

    @Override
    public void changeAdvertisementStatus(String id, AdvertisementStatus status, HttpServletRequest request) {
        if (advertisementRepository.existsById(id)) {
            advertisementRepository.updateAdvertisementStatusById(id, status);
        } else {
            throw new ApiRequestException(
                    String.format("Advertisement with id %s does not exist", id)
            );
        }
    }

    @Override
    public AdvertisementsResponse getAdvertisementsForModeration(AdvertisementCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.page(), criteria.limit(), Sort.by(criteria.sortBy()));
        AdvertisementStatus unchecked = AdvertisementStatus.UNCHECKED;
        List<Advertisement> advertisements = advertisementRepository.findAdvertisementsByStatus(unchecked, pageable);
        Long count = advertisementRepository.countAllByStatus(unchecked);
        return new AdvertisementsResponse(
                advertisements,
                count
        );
    }
}
