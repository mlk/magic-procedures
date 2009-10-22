package com.blogspot.ihaztehcodez.magic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseScript {
	String value() ;
}
