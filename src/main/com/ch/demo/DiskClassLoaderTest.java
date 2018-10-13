package com.ch.demo;

import java.lang.reflect.Method;

/**
 * @Auther: pch
 * @Date: 2018/10/13 15:08
 * @Description:
 */
public class DiskClassLoaderTest {
	public static void main(String[] args) throws Exception {
		DiskClassLoader diskClassLoader = new DiskClassLoader("f:\\");
		Class<?> aClass = diskClassLoader.findClass("com.ch.demo.Say");
		Object instance = aClass.newInstance();
		Method say = aClass.getMethod("say");
		say.invoke(instance);
		System.out.println(aClass.getClassLoader().getParent());
	}
}
