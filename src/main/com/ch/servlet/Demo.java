package com.ch.servlet;

import sun.misc.Launcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * @Auther: pch
 * @Date: 2018/10/13 14:06
 * @Description:
 */
public class Demo extends DemoParent {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ClassLoader c1 = Demo.class.getClassLoader();
		System.out.println("Demo.class = " + c1);
		ClassLoader c2 = String.class.getClassLoader();
		System.out.println("String.class = " + c2);
		ClassLoader c3 = Object.class.getClassLoader();
		System.out.println("Object.class = " + c3);
		System.out.println("AppClassLoader父加载器 = "+c1.getParent());
		System.out.println("ExtClassLoader父加载器 = "+c1.getParent().getParent());
		URL resource = Demo.class.getClassLoader().getResource("");
		Class<?> aClass = Class.forName("com.ch.servlet.Demo");
		System.out.println(1);

	}
}
