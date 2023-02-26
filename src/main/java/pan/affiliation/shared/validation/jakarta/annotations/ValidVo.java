package pan.affiliation.shared.validation.jakarta.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pan.affiliation.shared.validation.jakarta.validators.ValidVoCheck;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@SuppressWarnings("unused")
@Retention(RUNTIME)
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = { ValidVoCheck.class })
public @interface ValidVo {
    String message() default "Valor inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
