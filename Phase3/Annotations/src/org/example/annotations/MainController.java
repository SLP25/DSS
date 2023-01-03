/*

=======================================================
==     THIS IS AN AUTOMATICALLY GENERATED FILE       ==
=======================================================

                    DO NOT EDIT!

*/

package org.example.annotations;

import org.example.annotations.MetaController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * The main controller of the application.
 * Is called by the main function and runs the application loop
 */
public class MainController {

    //Warning: The following code is black magic. I have no idea how it works. Good luck debugging it.
    private static Set<Class<? extends MetaController>> getMetaControllers() throws IOException
    {
        String packageName = MainController.class.getPackageName();
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> {
                    try {
                        return Class.forName(packageName + "." + line.substring(0, line.lastIndexOf('.')));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(c -> MetaController.class.isAssignableFrom(c) && !c.equals(MetaController.class))
                .map(c -> (Class<? extends MetaController>)c)
                .collect(Collectors.toSet());
        }
    }

    private final String help;
    private final Set<MetaController> metaControllers;
    private final Map<Class<?>, Object> controllers; /*! The map of controllers indexed by their class */

    /**
     * Constructor which loads all the controllers and sets each of their respective models
     * (if multiple controllers use the same model, a single instance is created for all)
     */
    public MainController()
    {
        try {
            this.metaControllers = new HashSet<>();
            for (Class<? extends MetaController> m : getMetaControllers())
                this.metaControllers.add(m.getConstructor().newInstance());

            Map<Class<?>, Object> models = new HashMap<>();
            for (MetaController m : this.metaControllers)
                models.putIfAbsent(m.getModel(), m.getModel().getConstructor().newInstance());

            this.controllers = new HashMap<>();
            for (MetaController m : this.metaControllers) {
                Object controller = m.createController(models.get(m.getModel()));
                this.controllers.put(controller.getClass(), controller);
            }
        } catch (ReflectiveOperationException | IOException e) {
            throw new RuntimeException(e);
        }

        //TODO: generate help (use MetaController::getDescription)
        this.help = "If you are reading this, Bace is lazy and hasn't implemented the help command yet";
    }

    /**
     * The main application loop
     */
    public void run()
    {
        System.out.println("F1 MANAGER");
        System.out.println("TP DSS 2022 - Group 20");
        System.out.println("For more help, type 'help'");
        System.out.println("============================================");

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("f1manager> ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                running = false;
            } else if (!parseInput(input))
                System.out.println("Command not recognized");
            }
        }

    /**
     * Processes a command by the user.
     *
     * @param input the command string
     * @return Whether the command was found and executed
     */
    public boolean parseInput(String input) {
        if (input.equals(""))
            return true;

        if (input.equalsIgnoreCase("help")) {
            System.out.println(this.help);
            return true;
        }

        for (MetaController m : this.metaControllers)
            if (m.parseInput(input, this.controllers))
                return true;

        return false;
    }
}
