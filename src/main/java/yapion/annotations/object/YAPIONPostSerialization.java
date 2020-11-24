package yapion.annotations.object;

public @interface YAPIONPostSerialization {
    String[] context() default {};
}
