package org.example.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Annotation processor for the creation of the main application controller.
 */
public class EndpointProcessor extends AbstractProcessor {
    private Messager messager; /*! The messager used to display error messages */
    private Filer filer; /*! The filer containing the previously processed files */

    private static <T> Stream<Tuple<Integer, T>> enumerate(Stream<T> stream) {
        AtomicInteger i = new AtomicInteger(0);
        return stream.map(e -> new Tuple<>(i.getAndAdd(1), e));
    }

    /**
     * Initializes the processor
     *
     * @param processingEnv environment to access facilities the tool framework
     *                      provides to the processor
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Gets the supported version of java of this processor
     *
     * @return Latest
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    /**
     * Processes all the annotations and creates the meta controller files
     *
     * @param set              the annotation interfaces requested to be processed
     * @param roundEnvironment environment for information about the current and prior round
     * @return Whether the processing finished successfully
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(API.class)) {
                TypeElement e = (TypeElement) annotatedElement;
                String metaController = processClass(e, roundEnvironment);

                //TODO: not die with classes outside org.example.controllers
                JavaFileObject builderFile = filer.createSourceFile(
                        String.format("org.example.annotations.Meta%s", e.getSimpleName()));

                try (PrintWriter writer = new PrintWriter(builderFile.openWriter())) {
                    writer.write(metaController);
                }
            }
        } catch (org.example.annotations.IllegalAnnotationException | IOException e) {
            //Compilation will fail anyway
            error(null, "EndpointProcessor threw %s: %s", e.getClass().getName(), e.getMessage());
        }

        return true;
    }

    private String processClass(TypeElement clazz, RoundEnvironment roundEnvironment) throws IllegalAnnotationException, IOException {
        API annotation = clazz.getAnnotation(API.class);
        Name controllerName = clazz.getQualifiedName();

        Map<String, String> replacements = new TreeMap<>();
        replacements.put("${model}", annotation.model());
        replacements.put("${name}", clazz.getSimpleName().toString());
        replacements.put("${controller}", controllerName.toString());
        replacements.put("${methods}", processMethods(controllerName, roundEnvironment));
        return fromTemplate("MetaController.template", replacements);
    }

    /**
     * Processes all the method-level annotations and generates the Java code used in the parsing of the user input and
     * processing of such commands by the MainController.java
     * in the MainController.java class
     *
     * @param roundEnvironment environment for information about the current and prior round
     * @return the Java code which will process all user input
     * @throws IllegalAnnotationException If there is an annotation @Endpoint applied to something other than a method
     */
    private String processMethods(Name controllerName, RoundEnvironment roundEnvironment) throws IllegalAnnotationException, IOException {
        StringBuilder ans = new StringBuilder();

        for (Element e : roundEnvironment.getElementsAnnotatedWith(Endpoint.class)) {
            if (((TypeElement) e.getEnclosingElement()).getQualifiedName().equals(controllerName)) {
                ans.append(processMethod((ExecutableElement) e, roundEnvironment));
                ans.append("\n");
            }
        }

        return ans.toString();
    }

    private String processMethod(ExecutableElement method, RoundEnvironment roundEnvironment) throws IOException {
        Endpoint annotation = method.getAnnotation(Endpoint.class);
        String escapedRegex = annotation.regex().replace("\\", "\\\\");
        String className = ((TypeElement) method.getEnclosingElement()).getQualifiedName().toString();
        String methodName = method.getSimpleName().toString();

        String args = String.join(",", enumerate(method.getParameters().stream()
                .map(ve -> ((DeclaredType) ve.asType()).asElement().toString()))
                .map(p -> String.format("%s.valueOf(m.group(%d))", p.second, p.first + 1))
                .toList());

        Map<String, String> replacements = new TreeMap<>();
        replacements.put("${regex}", escapedRegex);
        replacements.put("${controller}", className);
        replacements.put("${methodName}", methodName);
        replacements.put("${methodArgs}", args);
        return fromTemplate("method.template", replacements);
    }

    private String fromTemplate(String resourceName, Map<String, String> replacements) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            String result = new String(inputStream.readAllBytes());

            for (var r : replacements.entrySet())
                result = result.replace(r.getKey(), r.getValue());

            return result;
        }
    }

    /**
     * Prints an error to the messager
     *
     * @param e    the Element which caused the error
     * @param msg  the error message (format string)
     * @param args the error message arguments
     */
    private void error(Element e, String msg, Object... args) {
        this.messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    /**
     * Prints a message to the messager
     *
     * @param e    the Element which caused the message
     * @param msg  the message (format string)
     * @param args the message arguments
     */
    private void msg(Element e, String msg, Object... args) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args), e);
    }

    /**
     * Gets the set of all supported annotations
     *
     * @return a set with all supported annotations (@Endpoint and @API)
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Endpoint.class.getCanonicalName());
        annotations.add(API.class.getCanonicalName());
        return annotations;
    }

    private static class Tuple<U, T> {
        public U first;
        public T second;

        Tuple(U first, T second) {
            this.first = first;
            this.second = second;
        }
    }
}
