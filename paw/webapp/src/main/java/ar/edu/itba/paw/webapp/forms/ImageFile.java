package ar.edu.itba.paw.webapp.forms;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface ImageFile {
    String message() default "{ar.edu.itba.paw.webapp.forms.ImageValidator}";

    long maxSize();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
