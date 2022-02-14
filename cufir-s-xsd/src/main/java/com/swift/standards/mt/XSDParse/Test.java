package com.swift.standards.mt.XSDParse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.xsd.util.XSDParseUtil;


public class Test {
	private static Logger log =Logger.getLogger(Test.class); 
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		
		try {
			//List<XSDNode>nodes=xsdReader.paserXSD(realPath+"/LightEditor_caaa.xsd");
			String realPath="D:\\XSchema";
			XSDParseUtil xsdReader=new XSDParseUtil();
			File f=new File(realPath+"/LightEditor_caaa.xsd");
			if (!f.exists()) {
				System.out.println("文件不存在");
			}
			//将文件从目录中读取出来，并存入字节数组
			FileInputStream InSteam=new FileInputStream(f);
			ByteArrayOutputStream outSteam=new ByteArrayOutputStream();
//			int b;
//			while ((b=InSteam.read())!=-1){
//				System.out.println((char)b);
//			}
//			InSteam.close();
			byte[]b=new byte[1024];
			int len=0;
			while ((len=InSteam.read(b))!=-1 ){
				outSteam.write(b,0,len);
			}
			System.out.println(b);
			System.out.println("===ok");
			InSteam.close();
			outSteam.close();
			//将字节数组中的内容转化成字符串形式输出
			String str2=new String(outSteam.toByteArray(),"utf-8");
			System.out.println(str2);
			Map<String, Object>map=xsdReader.doXSDParse(str2);
			if (map!=null&&map.keySet()!=null) {
				for (Object keyValue : map.keySet()) {
				log.debug(keyValue+"="+map.get(keyValue));
				}
			}
			//Iterator<Map.Entry<String, String>>it=map.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry<String, String>entry=it.next();
//				System.out.println("key="+entry.getKey()+"and value="+entry.getValue());
//			}
			System.out.println(map);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
	}//待补充
    
	
}
