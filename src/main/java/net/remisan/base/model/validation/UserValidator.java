package net.remisan.base.model.validation;

import net.remisan.base.model.User;
import net.remisan.base.model.validation.util.UserValidationHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Autowired
    private UserValidationHelper userValidationHelper;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        User user = (User) obj;
        this.userValidationHelper.validateEmail(errors, user.getId(), user.getEmail());
        this.userValidationHelper.validateLogin(errors, user.getId(), user.getLogin());
        this.userValidationHelper.validatePassword(errors, user.getPlainPassword(), user.getPlainPasswordConfirmation());
    }
}