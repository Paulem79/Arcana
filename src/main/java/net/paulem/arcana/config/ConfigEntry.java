package net.paulem.arcana.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark a field as a config entry.
 * @author Paulem
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigEntry {
    /**
     * The path of this entry in the config section, relative to the loaded section.
     * If left empty, the annotated field's name is used as the path.
     * @return the config path for this entry, or an empty string to use the field's name
     */
    String path() default "";
}
