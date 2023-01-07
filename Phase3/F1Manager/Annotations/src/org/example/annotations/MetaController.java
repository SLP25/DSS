package org.example.annotations;

import java.util.Map;

abstract class MetaController {
    abstract Class<?> getModel();

    abstract Object createController(Object model);

    abstract String getDescription();

    abstract boolean parseInput(String input, Map<Class<?>, Object> controllers);
}
