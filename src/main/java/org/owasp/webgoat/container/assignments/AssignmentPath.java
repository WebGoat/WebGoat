package org.owasp.webgoat.container.assignments;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nbaars on 1/14/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssignmentPath {

    String[] path() default {};

    RequestMethod[] method() default {};

    String value() default "";
}
