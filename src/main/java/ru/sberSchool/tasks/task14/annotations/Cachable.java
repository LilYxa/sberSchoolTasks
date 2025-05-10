package ru.sberSchool.tasks.task14.annotations;

import ru.sberSchool.tasks.task14.service.DataSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark methods whose results should be cached.
 * The method will be intercepted and its results will be stored in the specified cache data source.
 *
 * @author Elland Ilia
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cachable {
    /**
     * The {@link DataSource} class to be used for caching the method's results.
     * This class must be a subclass of {@link DataSource}.
     *
     * @return The {@link DataSource} implementation class.
     */
    Class<? extends DataSource> value();
}
