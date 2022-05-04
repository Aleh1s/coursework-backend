package ua.palamar.courseworkbackend.validator.registration;

import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.validator.RegistrationValidator;

@Component
public class SimpleRegistrationValidator implements RegistrationValidator {
    @Override
    public boolean isEmailValid(String email) {
        return true;
    }

    @Override
    public boolean isPasswordValid(String password) {
        return true;
    }

    @Override
    public boolean isFirstNameValid(String firstName) {
        return true;
    }

    @Override
    public boolean isLastNameValid(String lastName) {
        return true;
    }

    @Override
    public boolean isCityValid(String city) {
        return true;
    }

    @Override
    public boolean isAddressValid(String address) {
        return true;
    }

    @Override
    public boolean isPostNumberValid(String postNumber) {
        return true;
    }

    @Override
    public boolean isPhoneNumberValid(String phoneNumber) {
        return true;
    }
}
