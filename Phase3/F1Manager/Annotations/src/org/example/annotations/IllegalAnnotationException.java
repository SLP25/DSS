package org.example.annotations;

/**
 * An exception meaning there was an annotation in an illegal place
 */
public class IllegalAnnotationException extends Exception {
    /**
     * Default constructor
     *
     * @param message the exception message
     */
    public IllegalAnnotationException(String message) {
        super(message);
    }
}
