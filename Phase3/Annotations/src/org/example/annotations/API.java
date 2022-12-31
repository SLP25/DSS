package org.example.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation which describes a class which is a controller that should be loaded on start up
 */
@Target(ElementType.TYPE)
public @interface API {
    String model();   //Fully qualified name of the model class. Must have a public empty constructor
}
