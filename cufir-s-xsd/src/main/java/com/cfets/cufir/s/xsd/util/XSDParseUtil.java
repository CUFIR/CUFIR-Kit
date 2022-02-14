package com.cfets.cufir.s.xsd.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.cfets.cufir.s.xsd.bean.ComplexBean;
import com.cfets.cufir.s.xsd.bean.SimpleBean;
import com.cfets.cufir.s.xsd.dao.XSDParseDao;



public class XSDParseUtil implements XSDParseDao{
	
	private static Logger log =Logger.getLogger(XSDParseUtil.class);
	public Map<String, Object>doXSDParse(String strxml) throws Exception{
		strxml=strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
		if (null==strxml||"".equals(strxml)) {
			return null;
		}
		//Map<String, String>m=new HashMap<String, String>();
		InputStream in=new ByteArrayInputStream(strxml.getBytes("UTF-8"));
		SAXBuilder builder=new SAXBuilder();
		Document doc=builder.build(in);
		Element root=doc.getRootElement();
		/**
		 * 1.test
		 */
//		List<Element>nodeList=root.getChildren();
//		for (Element element : nodeList) {
//			if (element.getContent().contains("name")) {
//				
//			System.out.println(element);
//			String k=root.getAttributeValue("name");//读取该节点的某个属性
//			List<String>list=root.getAttributes();//获取所有节点
//			String v=root.getText();//获取该节点的值
//			m.put(k, v);
//			Iterator it=list.iterator();
//			while (it.hasNext()) {
//				
//			}
//		}
//		
//		}
		Map<String,Object> map=new HashMap<String,Object>();
//		List<Object> simpleType	= null;
//		List<Object> complexType = null;
		/**
		 * 2.test
		 */
		List<Element> list=root.getChildren();
		List<ComplexBean>complexnodes=new ArrayList<ComplexBean>();
		List<SimpleBean>simplenodes=new ArrayList<SimpleBean>();
		for(Element e:list) {
			String type=e.getName();
			if("complexType".equals(type)) {
				ComplexBean b=new ComplexBean();
				System.out.println(e.getName()+" :   "+e.getAttributeValue("name")+"======================");
				if(e.getChildren()!=null) {
					List<Element> childs=e.getChildren();
					for(Element ce:childs) {
						List<Element> childss=ce.getChildren();
						if(!childss.isEmpty() ) {
						for(Element cee:childss) {
							//System.out.println(cee.getAttributes());
							b.setName(cee.getAttributeValue("name"));
							b.setType(cee.getAttributeValue("type"));
							b.setMinOccurs(cee.getAttributeValue("minOccurs"));
							b.setMaxOccurs(cee.getAttributeValue("maxOccurs"));
							System.out.println(cee.getName()+" :   "+cee.getAttributeValue("name")+" "+
									cee.getAttributeValue("type")+" "+
									cee.getAttributeValue("minOccurs")+" "+
									cee.getAttributeValue("maxOccurs"));
						}
					}}
				}
				complexnodes.add(b);
				//List<ComplexBean>complexnodes=new ArrayList<ComplexBean>();
				System.out.println("");
			}else if("simpleType".equals(type)){
				SimpleBean s=new SimpleBean();
				s.setName(e.getAttributeValue("name"));
				System.out.println(e.getName()+" :   "+e.getAttributeValue("name"));
				if(e.getChildren()!=null) {
					List<Element> childs=e.getChildren();
					for(Element ce:childs) {
						List<Element> childss=ce.getChildren();
						for(Element cee:childss) {
							s.setValue(cee.getAttributeValue("value"));
							System.out.println(cee.getName()+" :   "+cee.getAttributeValue("value"));
						}
					}
				}
				//List<SimpleBean>simplenodes=new ArrayList<SimpleBean>();
				simplenodes.add(s);
				//simpleType.add(s);
				System.out.println();
			}
		}
//		complexType.add(complexnodes);
//		simpleType.add(simplenodes);
		in.close();
		map.put("1", complexnodes);
		map.put("2", simplenodes);
		return map;
//		Iterator it=list.iterator();
//		while(it.hasNext()) {
//			Element e=(Element) it.next();
//			String k=e.getName();
//			String v="";
//			/**
//			List<Element> children=e.getChildren();
//			for (Element element : children) {
//			if (children.isEmpty()) {
//				v=e.getTextNormalize();
//			}else {
//				v=XSDParseUtil.getChildrenText(children);
//			}
//			m.put(k, v);
//			
//			}*/
//		}
//		in.close();
//		System.out.println(m);
//		return m;
		
	}
//	/**
//	 * 获取子节点
//	 */
//	public static String getChildrenText(List children) {
//		StringBuffer sb=new StringBuffer();
//		if (!children.isEmpty()) {
//			Iterator it=children.iterator();
//			while (it.hasNext()) {  
//				Element e=(Element) it.next();
//				String name=e.getName();
//				//String value=e.getTextNormalize();
//				List value=e.getAttributes();
//				List list=e.getChildren();
//				sb.append("<"+name+">");
//				if (!list.isEmpty()) {
//					sb.append(XSDParseUtil.getChildrenText(list));
//				}
//				sb.append(value);
//				sb.append("</"+name+">");
//			}
//		}
//		return sb.toString();
//		
//	}



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
//		int b;
//		while ((b=InSteam.read())!=-1){
//			System.out.println((char)b);
//		}
//		InSteam.close();
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
//		while (it.hasNext()) {
//			Map.Entry<String, String>entry=it.next();
//			System.out.println("key="+entry.getKey()+"and value="+entry.getValue());
//		}
		System.out.println(map);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

}//待补充
}