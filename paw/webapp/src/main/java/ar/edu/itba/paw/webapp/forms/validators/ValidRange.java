package ar.edu.itba.paw.webapp.forms.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = RangeValidator.class)
public @interface ValidRange {
    String message() default "{ar.edu.itba.paw.webapp.forms.validators.ValidRange.message}";

    String minField();

    String maxField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
