package ar.edu.itba.paw.webapp.forms;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeCheckValidtor implements ConstraintValidator<RangeCheck, CategoryFilter> {

    @Override
    public void initialize(RangeCheck date) {
        // Nothing here
    }

    @Override
    public boolean isValid(CategoryFilter dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getMin() == null || dto.getMax() == null || dto.getMin().matches("") || dto.getMax().matches("")) {
            return true;
        }
        return Long.parseLong(dto.getMin()) <= Long.parseLong(dto.getMax());
    }
}