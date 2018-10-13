package com.ch.annotation;

import java.lang.annotation.*;

/**
 * @Auther: pch 处理器 映射器注解
 * @Date: 2018/10/12 21:40
 * @Description:
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

	String value() default "";
}
