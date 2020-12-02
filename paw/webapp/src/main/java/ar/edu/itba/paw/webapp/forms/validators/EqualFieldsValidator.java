package ar.edu.itba.paw.webapp.forms.validators;

import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

public class EqualFieldsValidator implements ConstraintValidator<EqualFields, Object> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private static final long ONE = 1L;

    private String[] fields;

    @Override
    public void initialize(EqualFields equalFields) {
        fields = equalFields.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        final long count = Stream.of(fields)
                .map(field -> PARSER.parseExpression(field).getValue(value))
                .distinct()
                .count();
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("EqualFields")
                .addConstraintViolation();
        return count == ONE;
    }
}
