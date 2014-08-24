package net.remisan.base.model.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ValidationManager implements Validator {

    private List<Validator> validators;
    
    public ValidationManager() {
        super();
        this.validators = new ArrayList<Validator>();
    }
    
    public ValidationManager(List<Validator> validators) {
        super();
        this.validators = validators;
    }

    public boolean supports(Class<?> clazz) {
        for (Validator v : this.validators) {
            if (v.supports(clazz)) {
                return true;
            }
        }
        return false;
    }

    public void validate(Object target, Errors errors) {
        for (Validator v : this.validators) {
            if (v.supports(target.getClass())) {
                v.validate(target, errors);
            }
        }
    }
    
    public void addValidator(Validator validator) {
        this.validators.add(validator);
    }
}
