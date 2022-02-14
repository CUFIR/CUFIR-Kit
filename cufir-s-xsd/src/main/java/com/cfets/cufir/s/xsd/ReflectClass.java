package com.cfets.cufir.s.xsd;

import java.lang.reflect.Field;

import com.cfets.cufir.s.xsd.bean.XsdBean;

public class ReflectClass {
	
	private final static String TAG="peter.log.ReflectClass";
	
	//创建对象
	public static void reflectNewInstance() {
	try {
		Class<?>classBook=Class.forName("com.cfets.cufir.s.xsd.bean.XsdBean");
		Object objectBean=classBook.newInstance();
		XsdBean xs=(XsdBean)objectBean;
		xs.setName("new world");
		xs.setCode("10010");
	}catch(Exception ex) {
		ex.printStackTrace();
	}
	
	}
	
	//反射私有属性
	public static void reflectPrivateField() {
		try {
			Class<?>classBean=Class.forName("com.cfets.cufir.s.xsd.bean.XsdBean");
			Object objectBean=classBean.newInstance();
			Field fieldTag=classBean.getDeclaredField("TAG");
			fieldTag.setAccessible(true);
			String tag=(String)fieldTag.get(objectBean);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
