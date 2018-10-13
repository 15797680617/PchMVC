package com.ch.core;

import com.ch.annotation.Controller;
import com.ch.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: pch
 * @Date: 2018/10/12 22:11
 * @Description:
 */
@Controller
@RequestMapping("/hello")
public class HelloController {

	@RequestMapping("/java")
	public void java(String javaName,String name,int java, HttpServletRequest request){
		System.err.println("==============hello Java==============");
	}

	@RequestMapping("/android")
	public void android(){
		System.err.println("==============hello Android==============");
	}

}
