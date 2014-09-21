package net.remisan.base.validation.util;

import net.remisan.base.model.validation.util.UserValidationHelper;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserValidationHelperImpl implements UserValidationHelper {
    
    public void validateEmail(Errors errors, Long id, String email) {
        
    }
    
    public void validateLogin(Errors errors, Long id, String login) {
        
    }
    
    public void validatePassword(Errors errors, String plainPassword, String plainPasswordConfirmation) {
        
    }
}
