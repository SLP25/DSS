package org.example.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation which describes a controller endpoint, i.e, a controller method that can be called by the user
 * by inputting a string which follows the given regex pattern
 */
@Target(ElementType.METHOD)
public @interface Endpoint {
    /**
     * @return the Regex of a command to run the annotated method
     */
    String regex();

    String description() default "";
}
