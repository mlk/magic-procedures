package com.github.mlk.magic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is applied to methods that you wish to be glued to the database.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseScript {
    /**
     * @return The SQL script to execute.
     */
    String value();
}
