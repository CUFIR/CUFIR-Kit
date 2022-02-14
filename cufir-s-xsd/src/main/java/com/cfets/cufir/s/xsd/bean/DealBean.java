package com.cfets.cufir.s.xsd.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class DealBean {
	//将查询结果与对象属性绑定,返回一个hashMap对象
	public static Map<String,Object>setMap(Object obj,ResultSet rs){
		HashMap<String,Object>hm=new HashMap<>();
		Field[]fields=obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				hm.put(fields[i].getName(),rs.getObject(fields[i].getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hm;
	}
	
	
	//将查询结果set到对象里
	public static void setT(Object obj,Map<String,Object>map1) {
		Class<? extends Object> c=obj.getClass();
		Method[] methods=c.getMethods();
		for (Method m : methods) {
			if (m.getName().startsWith("set")) {
				String name=m.getName();
				name=name.substring(3,4).toLowerCase()+name.substring(4,name.length());
				
				if (map1.containsKey(name)) {
					try {
						m.invoke(obj, map1.get(name));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
