package is.rares.kumo.security.annotation;

import is.rares.kumo.security.enums.TokenType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HasTokenType {
    TokenType[] value() default {TokenType.NORMAL_TOKEN};
}
