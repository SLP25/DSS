package org.example.controllers;

import org.example.business.systems.SystemFacade;
import org.example.views.View;

public abstract class Controller {

    private final SystemFacade model; /*! The model of the application*/

    private final View view; /*! The view handled by this controller */


    protected Controller(SystemFacade model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Gets the model of the application
     *
     * @return the model of the application
     */
    protected SystemFacade getModel() {
        return this.model;
    }

    /**
     * Gets the view the controller handles
     *
     * @return the view the controller handles\
     */
    protected View getView() {
        return this.view;
    }
}