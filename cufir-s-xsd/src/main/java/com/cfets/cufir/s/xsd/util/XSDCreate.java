package com.cfets.cufir.s.xsd.util;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import com.cfets.cufir.s.xsd.ListToTree;
import com.cfets.cufir.s.xsd.bean.AcceptorBean;
import com.cfets.cufir.s.xsd.bean.BatchBean;
import com.cfets.cufir.s.xsd.bean.DealBean;
import com.cfets.cufir.s.xsd.bean.Headean;
import com.cfets.cufir.s.xsd.bean.OuterBean;
import com.cfets.cufir.s.xsd.bean.SecurityBean;
import com.cfets.cufir.s.xsd.bean.Test;
import com.cfets.cufir.s.xsd.bean.TypeBean;



public class XSDCreate {
	/**
	 * XSD自动生成、保存到本地
	 * @author jiangqiming_het
	 */
	public CustomSchemaOutputResolver resolver;
	
	public static void main(String[] args) throws SQLException {
		XSDCreate xsdCreate=new XSDCreate();
		
		String path="D:/XSDSchema";
		File f=new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		xsdCreate.resolver=xsdCreate.new CustomSchemaOutputResolver("D:\\XSDSchema", "cufir-s-xsd.xsd");
		//待补充
		Map<String,String> hashMap = new HashMap<String, String>();
		//Class [] classes= {ListToTree.class};
		//
		/**
		 * @TEST
		 */
		List list=new ArrayList<String>();
		String []names= {"Envt","Cntxt","Tx"};
		String []types= {"TOP1","TOP2","TOP3"};
		OuterBean ob=new OuterBean();
		for (String string :names) {
			ob.setName(string);
			
		}
		for (String string :types) {
			ob.setType(string);
			 
		}
		//ob.setDataType(2);
		

		ob.setList(list);
		Class [] classes= {OuterBean.class};
		
		xsdCreate.createXSD(classes,hashMap);
		
		System.out.print("已生成XSD文件,请查看本地目录    "+path);
	}
	
	public void createXSD(Class[] classes) {
		try {
			JAXBContext context=JAXBContext.newInstance(classes);
			context.generateSchema(resolver);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void createXSD(Class[] classes, Map<String, ?> var1) {
		try {
			JAXBContext context=JAXBContext.newInstance(classes,var1);
			context.generateSchema(resolver);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public class CustomSchemaOutputResolver extends SchemaOutputResolver{
		
		private File file;
		
		public CustomSchemaOutputResolver(String dir,String fileName) {
			try {
				file =new File(dir,fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Result createOutput(String arg0, String arg1) throws IOException {
			return new StreamResult(file);
		}
		
	}
	
        
}
