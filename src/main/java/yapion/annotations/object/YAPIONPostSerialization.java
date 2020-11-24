package yapion.annotations.object;

import yapion.serializing.YAPIONSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation describes one of the three steps in the process
 * of serialization. You can use this annotation for a method
 * that add the data or object back after serialized by YAPION. This
 * can be useful to evaluate {@code null} pointer to their excepted
 * value after serialization.
 *
 * <br><br>Thr four Steps are:
 * <br>- PreSerialization method call
 * <br>- Serializing all fields ({@link YAPIONSerializer})
 * <br>- PostSerialization method call
 *
 * <br><br>The context describes the state in which the {@link YAPIONSerializer}
 * should be for this annotation to take effect.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface YAPIONPostSerialization {
    String[] context() default {};
}
