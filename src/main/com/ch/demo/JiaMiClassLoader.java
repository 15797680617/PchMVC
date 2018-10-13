package com.ch.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Auther: pch 加密的 类加载器
 * @Date: 2018/10/13 15:24
 * @Description:
 */
public class JiaMiClassLoader extends ClassLoader {

	private String basePath;

	public JiaMiClassLoader(String basePath) {
		this.basePath = basePath;
	}

	@Override
	protected Class<?> findClass(String s) throws ClassNotFoundException {
		File file = new File(basePath,getClassName(s));
		try {
			FileInputStream is = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0;
			try {
				while ((len = is.read()) != -1) {
					bos.write((byte) (len^2));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] data = bos.toByteArray();
			is.close();
			bos.close();
			return defineClass(s,data,0,data.length);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.findClass(s);
	}

	private String getClassName(String name){
		int index = name.lastIndexOf('.');
		if(index == -1){
			return name+".classjm";
		}else{
			return name.substring(index+1)+".classjm";
		}
	}

}
