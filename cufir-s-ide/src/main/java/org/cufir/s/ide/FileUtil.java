package org.cufir.s.ide;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * File 工具类
 */
public class FileUtil {

	/**
	 * 将字符串写入文件
	 * 
	 * @param path
	 * @param str
	 */
	public static void writeStrToFile(String filePath, String str) {
		FileOutputStream fos = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}

			fos = new FileOutputStream(file);
			byte[] b = str.getBytes();
			fos.write(b);
			fos.flush();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 读出文件的所有内容字符串
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readStrFromFile(String filePath) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);
			byte[] b = new byte[bis.available()];
			bis.mark(bis.read(b));
			String string = new String(b, "utf-8");

			return string;
		} catch (Exception e) {
			return "";
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获得jar包中文件内容
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassPath(Class<?> clazz, String path) {

		try {
			InputStream resourceAsStream = clazz.getClassLoader().getResourceAsStream(path);

			BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream));

			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}

			return buffer.toString();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return "";

	}

	public static void main(String[] args) {
//		writeStrToFile("C:\\Users\\ISAAC\\Desktop\\plugins\\ttt.txt", "123");
		System.out.println(readStrFromFile("C:\\Users\\ISAAC\\Desktop\\plugins\\ttt.txt"));
	}
}
