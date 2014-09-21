package net.remisan.base.model.validation.util;

import org.springframework.validation.Errors;

public interface UserValidationHelper {

    public void validateEmail(Errors errors, Long id, String email) ;
    
    public void validateLogin(Errors errors, Long id, String login);
    
    public void validatePassword(Errors errors, String plainPassword, String plainPasswordConfirmation) ;
}
