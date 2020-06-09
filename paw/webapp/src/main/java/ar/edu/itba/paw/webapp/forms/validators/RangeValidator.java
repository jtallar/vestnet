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

        System.out.println("\n\nMin Value: " + minValue + " MaxValue: " + maxValue + "\n\n");

        try {
            System.out.println("\n\nNumbers: Min Value: " + Integer.parseInt(minValue) + " MaxValue: " + Integer.parseInt(maxValue) + "\n\n");
            System.out.println(Integer.parseInt(minValue) <= Integer.parseInt(maxValue));
            return Integer.parseInt(minValue) <= Integer.parseInt(maxValue);
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
