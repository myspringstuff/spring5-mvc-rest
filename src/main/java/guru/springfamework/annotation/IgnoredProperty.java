package guru.springfamework.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoredProperty {
}
