package com.github.mlk.magic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation tells <i>the magic</i> that the transaction should before returning.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Commit {
}
