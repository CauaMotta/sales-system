package br.com.ocauamotta.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableColumn {

    String dbName();
    String setJavaName();
}
