package annotation.annotations.keyvalue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface KeyValueStore {
    public String tableName() default "";
    public KeyType keyType();
    public String keyName() default "PrimaryKey";
}