package com.ch.demo;

import java.lang.reflect.Method;

/**
 * @Auther: pch
 * @Date: 2018/10/13 15:28
 * @Description:
 */
public class JiaMiClassLoaderText {
	public static void main(String[] args) throws Exception {
		/*JiaMiClassLoader jiaMiClassLoader = new JiaMiClassLoader("F:");
		Class<?> aClass = jiaMiClassLoader.findClass("com.ch.demo.Say");
		Object o = aClass.newInstance();
		Method say = aClass.getMethod("say");
		say.invoke(o);
		System.out.println(1);*/
		DiskClassLoader diskClassLoader = new DiskClassLoader("f:\\");
		Class<?> aClass = diskClassLoader.findClass("com.ch.demo.Say");
		Object instance = aClass.newInstance();
		Method say = aClass.getMethod("say");
		say.invoke(instance);
		System.out.println(aClass.getClassLoader().getParent());
	}
}
