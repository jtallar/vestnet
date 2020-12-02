package ar.edu.itba.paw.webapp.forms.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {

    Class<? extends Enum<?>> enumClazz();

    String message() default "{ar.edu.itba.paw.webapp.forms.validators.ValidEnum.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}