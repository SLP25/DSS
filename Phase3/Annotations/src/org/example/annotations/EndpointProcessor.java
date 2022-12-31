package org.example.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Annotation processor for the creation of the main application controller.
 */
public class EndpointProcessor extends AbstractProcessor {
    private Messager messager; /*! The messager used to display error messages */
    private Filer filer; /*! The filer containing the previously processed files */

    /**
     * Initializes the processor
     * @param processingEnv environment to access facilities the tool framework
     * provides to the processor
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Gets the supported version of java (17) of this processor
     * @return 17
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_16;
    }

    /**
     * Processes all the annotations and creates the MainController file
     * @param set the annotation interfaces requested to be processed
     * @param roundEnvironment  environment for information about the current and prior round
     * @return Whether the processing went successfully
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //if(roundEnvironment.processingOver()) {
            try {
                String constructors = processClasses(roundEnvironment);
                String parseInput = processMethods(roundEnvironment);

                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Creating");
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "JAVA: " + constructors);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "MEMBRO: " + parseInput);

                if (constructors.equals(""))
                    return true;

                JavaFileObject builderFile = filer
                        .createSourceFile("org.example.controllers.MainController");

                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Built");

                PrintWriter writer = new PrintWriter(builderFile.openWriter());

                ClassLoader classLoader = getClass().getClassLoader();
                InputStream inputStream = classLoader.getResourceAsStream("MainController.template");

                String result = new String(inputStream.readAllBytes());
                inputStream.close();

                result = result.replace("${controllers}", constructors);
                result = result.replace("${input}", parseInput);

                writer.write(result);

                writer.close();
            } catch(IllegalAnnotationException | IOException e) {
                //Compilation will fail anyways
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "FOK");
                error(null, "EndpointProcessor threw %s: %s", e.getClass().getName(), e.getMessage());
            }

        //}

        return true;
    }

    /**
     * Processes all the class-level annotations and generates the Java code used in the loading and initialization of controllers
     * in the MainController class
     * @param roundEnvironment environment for information about the current and prior round
     * @return  the Java code which will load and initialize all controllers in the MainController
     * @throws IllegalAnnotationException If there is an annotation @API applied to something other than a class
     */
    private String processClasses(RoundEnvironment roundEnvironment) throws IllegalAnnotationException {

        StringBuilder builder = new StringBuilder();

        for(Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(API.class))
        {
            TypeElement classElement = (TypeElement)annotatedElement;
            String name = classElement.getSimpleName().toString();
            String modelName = annotatedElement.getAnnotation(API.class).model();

            builder.append(String.format("\t\tcontrollers.put(\"%s\", new %s(new %s()));\n", name, name, modelName));
        }

        return builder.toString();
    }

    /**
     * Processes all the method-level annotations and generates the Java code used in the parsing of the user input and
     * processing of such commands by the MainController
     * in the MainController class
     * @param roundEnvironment environment for information about the current and prior round
     * @return  the Java code which will process all user input
     * @throws IllegalAnnotationException If there is an annotation @Endpoint applied to something other than a method
     */
    private String processMethods(RoundEnvironment roundEnvironment) throws IllegalAnnotationException {

        StringBuilder builder = new StringBuilder();

        for(Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(Endpoint.class)) {
            //if(annotatedElement.getKind() != ElementKind.METHOD) {
            //    error(annotatedElement, "@Endpoint must be applied to methods");
            //    throw new IllegalAnnotationException("@Endpoint must be applied to methods");
            //}

            String regex = (annotatedElement.getAnnotation(Endpoint.class)).regex();
            //We add an extra \ as there needs to be two of them in the compiled file. One to escape and one to
            // represent a \
            String escapedRegex = regex.replace("\\", "\\\\");
            builder.append(String.format("\t\tif(input.matches(\"(?i)%s\")) {\n",
                    escapedRegex));

            //We add an extra \ as there needs to be two of them in the compiled file. One to escape and one to
            // represent a \
            builder.append(String.format("\t\t\tPattern p = Pattern.compile(\"%s\", Pattern.CASE_INSENSITIVE);\n",
                    escapedRegex));
            builder.append("\t\t\tMatcher m = p.matcher(input);\n");
            builder.append("\t\t\tif(m.find()) {\n");

            builder.append(methodCall(annotatedElement));
            builder.append("\t\t\t}\n");
            builder.append("\t\t\treturn true;\n\t\t}\n");
        }

        return builder.toString();
    }

    /**
     * Gets the Java code string used to call a method in the MainController class
     * @param element the annotated element
     * @return the Java code string used to call a method in the MainController class
     */
    private String methodCall(Element element) {

        String methodName = element.getSimpleName().toString();
        String className = element.getEnclosingElement().getSimpleName().toString();

        StringBuilder callBuilder = new StringBuilder("\t\t\t\t");

        if(((ExecutableElement) element).getReturnType().getKind() != TypeKind.VOID) {
            callBuilder.append("String ret = ");
        }

        callBuilder.append(String.format("((%s)controllers.get(\"%s\"))", className, className));
        callBuilder.append(String.format(".%s(", methodName));

        int i = 0;
        for(Element e : ((ExecutableElement)element).getParameters()) {
            String type = ((DeclaredType) e.asType()).asElement().toString();

            if(i != 0)
                callBuilder.append(",");
            callBuilder.append(String.format("%s.valueOf(m.group(%d))", type, i + 1));

            i++;
        }

        callBuilder.append(");\n");

        if(((ExecutableElement) element).getReturnType().getKind() != TypeKind.VOID) {
            callBuilder.append("\t\t\t\tthis.parseInput(ret.toString().split(\"\\n\"));\n");
        }

        return callBuilder.toString();
    }

    /**
     * Prints an error to the messager
     * @param e the Element which caused the error
     * @param msg the error message (format string)
     * @param args the error message arguments
     */
    private void error(Element e, String msg, Object... args) {
        this.messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

    /**
     * Gets the set of all supported annotations
     * @return a set with all supported annotations (@Endpoint and @Controller)
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<String>();
        annotations.add(Endpoint.class.getCanonicalName());
        annotations.add(API.class.getCanonicalName());
        return annotations;
    }

}
