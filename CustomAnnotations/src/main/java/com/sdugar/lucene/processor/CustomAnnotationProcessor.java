package com.sdugar.lucene.processor;

import com.google.auto.service.AutoService;
import com.sdugar.lucene.annotation.CustomAnnotation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by sourabhdugar on 2/20/17.
 */
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = {"com.sdugar.lucene.annotation.CustomAnnotation"})
@SupportedOptions(value = "debug")
@AutoService(value = Processor.class)
public class CustomAnnotationProcessor extends AbstractProcessor {

    private volatile FileObject logFileObject = null;

    private static class RecordType {
        final ElementKind kind;
        final String name;


        private RecordType(ElementKind kind, String name) {
            this.kind = kind;
            this.name = name;
        }

        @Override
        public String toString() {
            return "RecordType{" +
                    "kind=" + kind +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private final Map<CustomAnnotation.Release, List<RecordType>> recordTypeMap = new HashMap<>();

    public CustomAnnotationProcessor() {

    }

    private synchronized FileObject getLogFileObject() {
        if (logFileObject == null) {
            try {
                logFileObject = this.processingEnv.getFiler()
                        .createResource(StandardLocation.SOURCE_OUTPUT, "", "logFile.proc.apt");
            } catch (IOException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "Failed to create log file object");
            }
        }

        return logFileObject;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement te : annotations) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                    String.format("Processing annotation of type %s", te.getQualifiedName()));
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(te);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "List of annotated elements ");

            try (BufferedWriter bw = new BufferedWriter(getLogFileObject().openWriter())) {
                for (Element e : elements) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                            String.format("element name: %s, type: %s, modifiers: %s, misc : %s",
                                    e.getSimpleName(), e.getKind(), e.getModifiers().toString(), e.getAnnotation(CustomAnnotation.class).release()));
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Logfile is " + getLogFileObject());

                    if (e.getAnnotation(CustomAnnotation.class) != null) {
                        CustomAnnotation.Release rel = e.getAnnotation(CustomAnnotation.class).release();
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "got release " + rel);
                        recordTypeMap.putIfAbsent(rel, new ArrayList<>());
                        recordTypeMap.get(rel).add(new RecordType(e.getKind(), e.getSimpleName().toString()));
                    }
                }

                bw.newLine();

                bw.append("===========================================");
                    bw.newLine();
                        bw.append("Release")
                            .append("\t\t\t\t\t\t")
                            .append("Kind")
                            .append("\t\t\t\t\t\t")
                            .append("Name");
                    bw.newLine();

                // print the recordTypeMap to the file
                for (Map.Entry<CustomAnnotation.Release, List<RecordType>> entry : recordTypeMap.entrySet()) {
                    for (RecordType rec : entry.getValue()) {
                        bw.append(entry.getKey().name())
                            .append("\t\t\t\t\t\t")
                            .append(rec.kind.toString())
                            .append("\t\t\t\t\t\t")
                            .append(rec.name);
                    bw.newLine();
                    }
                }

                bw.append("===========================================");

            } catch (IOException e1) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "Error while writing to log file object");
            }
        }

        return true;
    }
}
