package ar.edu.itba.paw.webapp.forms;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeCheckValidtor implements ConstraintValidator<RangeCheck, ProjectFilter> {

    @Override
    public void initialize(RangeCheck date) {
        // Nothing here
    }

    @Override
    public boolean isValid(ProjectFilter dto, ConstraintValidatorContext constraintValidatorContext) {
        if (dto.getMinCost() == null || dto.getMaxCost() == null || dto.getMinCost().matches("") || dto.getMaxCost().matches("")) {
            return true;
        }
        return Long.parseLong(dto.getMinCost()) <= Long.parseLong(dto.getMaxCost());
    }
}