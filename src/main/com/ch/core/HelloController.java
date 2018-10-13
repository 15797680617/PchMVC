package com.ch.core;

import com.ch.annotation.Controller;
import com.ch.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Auther: pch
 * @Date: 2018/10/12 22:11
 * @Description:
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

	@RequestMapping("/java")
	public void java(String javaName, String name, int java, HttpServletRequest request, HttpServletResponse response){
		System.err.println("==============hello Java==============");
		System.out.println(javaName);
		System.out.println(name);
		System.out.println(java);
		System.out.println(request);
		System.out.println(response);
	}

	@RequestMapping("/android")
	public void android(){
		System.err.println("==============hello Android==============");
	}

}
