package ru.sberSchool.tasks.task5.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that the result of a method should be cached.
 *
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.Target
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {

}
