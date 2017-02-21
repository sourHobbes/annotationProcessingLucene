package com.sdugar.lucene.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import static com.sdugar.lucene.annotation.CustomAnnotation.Release.REL_1;


/**
 * Created by sourabhdugar on 2/20/17.
 */
@Target(value = {ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface CustomAnnotation {
    enum Release {
        REL_1,
        REL_2
    }

    Release release() default REL_1;
}
