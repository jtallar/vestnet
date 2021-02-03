package ar.edu.itba.paw.webapp.forms.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EqualFieldsValidator.class)
public @interface EqualFields {
    String message() default "{ar.edu.itba.paw.webapp.forms.validators.EqualFields.message}";

    String[] value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
