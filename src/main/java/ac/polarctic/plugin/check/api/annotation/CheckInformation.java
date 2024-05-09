package ac.polarctic.plugin.check.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckInformation {

    String name();
    int minVL();
}
