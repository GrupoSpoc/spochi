package com.spochi.controller.handler;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Esto es una anotación custom que permite identificar cualquier tipo de objetos
 * entre los parámetros de un controller
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Uid {}
