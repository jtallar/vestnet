package ar.edu.itba.paw.webapp.forms;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = RangeCheckValidtor.class)
public @interface RangeCheck {

    String message() default "{ar.edu.itba.paw.webapp.forms.RangeCheck}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}