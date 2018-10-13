package com.ch.annotation;

import java.lang.annotation.*;

/**
 * @Auther: pch 参数
 * @Date: 2018/10/12 21:42
 * @Description:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

	/**
	 * 参数的别名  必填
	 * @return
	 */
	String value();
}
