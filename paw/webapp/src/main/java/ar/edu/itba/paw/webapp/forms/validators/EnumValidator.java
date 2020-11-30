package ar.edu.itba.paw.webapp.forms.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private List<String> valueList;

    @Override
    public void initialize(ValidEnum validEnum) {
        valueList = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = validEnum.enumClazz();
        final Enum<?>[] enumConstants = enumClass.getEnumConstants();
        // Valid values are CONSTANTS, not "stringName"
        for (Enum enumVal : enumConstants) {
            valueList.add(enumVal.toString().toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && valueList.contains(value.toUpperCase());
    }
}
