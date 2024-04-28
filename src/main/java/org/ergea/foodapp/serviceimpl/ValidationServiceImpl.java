package org.ergea.foodapp.serviceimpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
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
