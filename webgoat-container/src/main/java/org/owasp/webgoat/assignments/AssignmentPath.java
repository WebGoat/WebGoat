package org.owasp.webgoat.assignments;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
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
//@RequestMapping
public @interface AssignmentPath {

  //  @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

   // @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default {};

 //   @AliasFor("path")
    String value() default "";
}
