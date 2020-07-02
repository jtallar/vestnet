package ar.edu.itba.paw.webapp.forms.validators;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<ValidRange, Object> {
    private String minField;
    private String maxField;

    @Override
    public void initialize(ValidRange validRange) {
        this.minField = validRange.minField();
        this.maxField = validRange.maxField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        String minValue = String.valueOf(new BeanWrapperImpl(value).getPropertyValue(minField));
        String maxValue = String.valueOf(new BeanWrapperImpl(value).getPropertyValue(maxField));

        try {
            return Integer.parseInt(minValue) <= Integer.parseInt(maxValue);
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
