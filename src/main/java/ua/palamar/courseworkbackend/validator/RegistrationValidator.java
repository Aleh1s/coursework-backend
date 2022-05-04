package ua.palamar.courseworkbackend.validator;

import org.springframework.stereotype.Component;

@Component
public interface RegistrationValidator {

    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    boolean isFirstNameValid(String firstName);
    boolean isLastNameValid(String lastName);
    boolean isCityValid(String city);
    boolean isAddressValid(String address);
    boolean isPostNumberValid(String postNumber);
    boolean isPhoneNumberValid(String phoneNumber);

}
