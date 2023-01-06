package br.com.attornatus.pessoas.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JustOnePrimaryValidator.class)
@Documented
public @interface JustOnePrimary {

    String message() default "{br.com.attornatus.pessoas.annotation.JustOnePrimary.message}: ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
