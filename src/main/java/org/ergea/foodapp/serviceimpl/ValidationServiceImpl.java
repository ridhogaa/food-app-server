package org.ergea.foodapp.serviceimpl;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.ergea.foodapp.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    private Validator validator;

    @Override
    public void validate(Object request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
