package ua.palamar.courseworkbackend.service.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.palamar.courseworkbackend.adapter.RegistrationServiceAdapter;
import ua.palamar.courseworkbackend.dto.RegistrationModel;
import ua.palamar.courseworkbackend.entity.user.DeliveryInfoEntity;
import ua.palamar.courseworkbackend.entity.user.UserEntity;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.repository.DeliveryInfoRepository;
import ua.palamar.courseworkbackend.repository.UserRepository;
import ua.palamar.courseworkbackend.service.RegistrationService;
import ua.palamar.courseworkbackend.service.UserService;
import ua.palamar.courseworkbackend.service.UserServiceValidator;
import ua.palamar.courseworkbackend.validator.RegistrationValidator;

@Service
public class SimpleRegistrationService implements RegistrationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserServiceValidator userServiceValidator;
    private final RegistrationValidator registrationValidator;

    private final DeliveryInfoRepository deliveryInfoRepository;
    private final RegistrationServiceAdapter registrationServiceAdapter;

    @Autowired
    public SimpleRegistrationService(UserService userService,
                                     UserRepository userRepository,
                                     UserServiceValidator userServiceValidator,
                                     RegistrationValidator registrationValidator,
                                     DeliveryInfoRepository deliveryInfoRepository,
                                     RegistrationServiceAdapter registrationServiceAdapter) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userServiceValidator = userServiceValidator;
        this.registrationValidator = registrationValidator;
        this.deliveryInfoRepository = deliveryInfoRepository;
        this.registrationServiceAdapter = registrationServiceAdapter;
    }

    @Override
    public ResponseEntity<?> register(RegistrationModel registrationModel) {

        if (userServiceValidator.userWithEmailExists(registrationModel.email()))
            throw new ApiRequestException(
                    String.format("User with email: %s already exists", registrationModel.email())
            );

        if (userServiceValidator.userWithPhoneNumberExists(registrationModel.phoneNumber()))
            throw new ApiRequestException(
                    String.format("User with phone number: %s already exists", registrationModel.phoneNumber())
            );

        if (!registrationValidator.isEmailValid(registrationModel.email()))
            throw new IllegalStateException("Invalid email");

        if (!registrationValidator.isPasswordValid(registrationModel.password()))
            throw new IllegalStateException("Invalid password");

        if (!registrationValidator.isFirstNameValid(registrationModel.firstName()))
            throw new IllegalStateException("Invalid first name");

        if (!registrationValidator.isLastNameValid(registrationModel.lastName()))
            throw new IllegalStateException("Invalid last name");

        if (!registrationValidator.isCityValid(registrationModel.city()))
            throw new IllegalStateException("Invalid city");

        if (!registrationValidator.isPostNumberValid(registrationModel.postNumber()))
            throw new IllegalStateException("Invalid post number");

        if (!registrationValidator.isAddressValid(registrationModel.address()))
            throw new IllegalStateException("Invalid address");

        if (!registrationValidator.isPhoneNumberValid(registrationModel.phoneNumber()))
            throw new IllegalStateException("Invalid phone number");

        UserEntity newUser = registrationServiceAdapter.getUserEntity(registrationModel);
        DeliveryInfoEntity newDeliveryInfo = registrationServiceAdapter.getDeliveryInfoEntity(registrationModel);

        newUser.addDeliveryInfo(newDeliveryInfo);

        userRepository.save(newUser);
        deliveryInfoRepository.save(newDeliveryInfo);

        return new ResponseEntity<>("Successful registration", HttpStatus.CREATED);
    }
}
