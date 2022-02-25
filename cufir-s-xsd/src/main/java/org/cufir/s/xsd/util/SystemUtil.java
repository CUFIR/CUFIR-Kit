package org.cufir.s.xsd.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * 系统操作工具
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class SystemUtil {
	/**
	 * 获取本地IP地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getLocalIp() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		String hostAddress = address.getHostAddress();
		return hostAddress;
	}

	/**
	 * 获取本地MAC地址
	 * 
	 * @return
	 * @throws UnknownHostException
	 * @throws Exception
	 */
	public static String getLocalMac() throws UnknownHostException {
		String result = "";
		Process process;
		BufferedReader br;
		String line = "";
		int index = -1;
		try {
			process = Runtime.getRuntime().exec("ipconfig /all");
			br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			while ((line = br.readLine()) != null) {
				index = line.toLowerCase().indexOf("物理地址");
				if (index >= 0) {// 找到了
					index = line.indexOf(":");
					if (index >= 0) {
						result = line.substring(index + 1).trim();
					}
					break;
				}
			}
			br.close();
		} catch (Exception e) {
			throw new UnknownHostException();
		}
		return result;
	}
	
	/**
	 * 获取Eclipse路径
	 * 
	 * @return
	 */
	public static String getEclipsePath() {
		return System.getProperty("user.dir").replace("\\", "//");
	}
	
	/**
	 * 获取操作系统用户名
	 * 
	 * @return
	 */
	public static String getSystemUser() {
		return System.getProperty("user.name");
	}
	
	/**
	 * 获取系统用户路径
	 * 
	 * @return
	 */
	public static String getUserPath() {
		return  System.getProperty("user.home").replace("\\", "//");
	}
	
	/**
	 * 获取Eclipse路径
	 * 
	 * @return
	 */
	public static String getEclipseVersion() {
		int i = getEclipsePath().split("//").length;
		return getEclipsePath().split("//")[i-1];
	}
}
