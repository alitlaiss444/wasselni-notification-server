package com.wasselni.common.model.dualauth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IncludeDualAuthDialog {

	public boolean enabled() default true;

	public String name() default "";

}
