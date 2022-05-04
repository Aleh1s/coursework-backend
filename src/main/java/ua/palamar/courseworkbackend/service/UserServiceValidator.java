package ua.palamar.courseworkbackend.service;

public interface UserServiceValidator {

    boolean userWithEmailExists(String email);
    boolean userWithPhoneNumberExists(String phoneNumber);

}
