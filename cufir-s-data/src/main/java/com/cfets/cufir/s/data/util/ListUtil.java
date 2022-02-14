package com.cfets.cufir.s.data.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ListUtil {

	@SuppressWarnings("unchecked")
	public static<T> List<T> deepCopy(List<T> srcList){
		try {
			ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
			ObjectOutputStream out =new ObjectOutputStream(byteOut);
			out.writeObject(srcList);
			out.close();
			
			ByteArrayInputStream byteIn=new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream in=new ObjectInputStream(byteIn);
			return (List<T>)in.readObject();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
