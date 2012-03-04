package com.blogspot.ihaztehcodez.magic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/** This annotation tells <i>the magic</i> that the transaction should 
 * before returning.  
 * NOTE: More advanced transaction control I currently see as the DI frameworks job. 
 * But we will see.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Commit { }
