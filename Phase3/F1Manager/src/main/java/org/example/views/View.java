package org.example.views;

public class View {
    /**
     * Displays an error to the console
     * @param message the error message
     */
    public void error(String message) {
        System.out.printf("An error has occurred: %s%n", message);
    }

    /**
     * Display a warning to the console
     * @param message the warning message
     */
    public void warning(String message) {
        System.out.printf("Warning: %s%n", message);
    }
}
