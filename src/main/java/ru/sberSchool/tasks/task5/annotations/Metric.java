package ru.sberSchool.tasks.task5.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code @Metric} annotation is used to mark methods for performance measurement.
 *
 * @see ru.sberSchool.tasks.task5.service.PerformanceProxy
 *
 * @author Elland Ilia
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Metric {

}