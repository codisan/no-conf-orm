package com.codisan.no_conf_orm.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	String name() default "";
	String type() default "";
	String defaultValue() default "";
	boolean primaryKey() default false;
	boolean autoIncrement() default false;
}
