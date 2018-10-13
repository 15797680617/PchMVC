package com.ch.demo;

import java.io.*;

/**
 * @Auther: pch class加解密 工具
 * @Date: 2018/10/13 15:21
 * @Description:
 */
public class FileUtils {
	public static void test(String path){
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(path+"jm");
			int b = 0;
			int b1 = 0;
			try {
				while((b = fis.read()) != -1){
					//每一个byte异或一个数字2
					fos.write(b ^ 2);
				}
				fos.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		FileUtils.test("f:/Say.class");
	}
}
