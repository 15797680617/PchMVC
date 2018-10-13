package com.ch.annotation;

import java.lang.annotation.*;

/**
 * @Auther: pch 控制层 注解
 * @Date: 2018/10/12 21:38
 * @Description:
 */
@Target(ElementType.TYPE)
// 运行时
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

	String value() default "";
}
