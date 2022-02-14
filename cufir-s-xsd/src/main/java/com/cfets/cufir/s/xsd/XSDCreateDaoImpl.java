package com.cfets.cufir.s.xsd;


import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.xsd.bean.ElementNode;
import com.cfets.cufir.s.xsd.dao.XSDTreeDao;
import com.cfets.cufir.s.xsd.dao.XSDTreeSetDao;

public class XSDCreateDaoImpl implements XSDTreeSetDao {
	
	private static Logger logger = Logger.getLogger(XSDCreateDaoImpl.class);
	
	public boolean generateXsd(String id,String path){
		XSDTreeDao dao1 = new ListToTree();
		try {
			dao1.queryListToMap(id,path);
		} catch (Exception e) {
			return false;
		}

		XSDGenerate generate=new XSDGenerate();
		/**
		 * 一：初始化xsd的头部信息
		 * 1.初始化xsd标签头部信息(schema)  
		 * 2.初始化xsd element  document               
		 * 3.获取查询结果，获取报文信息，构建document节点信息，complexType 信息，name(报文名)   ElementNode LIST
		 * 4.构建报文节点下面的header（block）信息 complexType   ElementNode list
		 * 
		 * 二：获取复合和简单类型的数据
		 * a.遍历complexnoDE,构建复合类型的数据complexType List<ElementNode>
		 * b.遍历simplenoDE,构建简单类型的数据SimpleType（包含code信息） List<ElementNode>
		 */
		for(ElementNode node:ListToTree.complexnodes) {
			List<ElementNode> ls=node.getList();
			if (ls==null) {
				logger.warn("ls没有数据");
			}
			generate.addComplexType(node.getName(),node.getList());
		}
		for(ElementNode node:ListToTree.simplenodes) {
			String name=node.getName();
//			while("Max6Text".equals(name)) {
//				logger.debug("数据拿到了");
//			}
			List<ElementNode> list11=node.getCodes();
			if (list11==null) {
				logger.warn("list11没有数据");
			}
			if (list11!=null) {
				generate.addSimpleType(node.getName(),node.getCodes());
			}
			//generate.addSimpleType(node.getName(),node.getCodes());
		}
		
		/*
		 * generate.addComplexType("CancellationResponse", Arrays.asList( new
		 * ElementNode("Envt","Environment","1","1"), new
		 * ElementNode("TxRspn","TransactionResponse","1","1"), new
		 * ElementNode("Tx","Transaction","1","1")));
		 * generate.addComplexType("SecurityTrailer", Arrays.asList( new
		 * ElementNode("CnttTp","ContentType","1","1")));
		 */
		
		//generate.addSimpleType(Arrays.asList(CurrencyID.values()));
		//generate.initHeaderBody("Header", "CancellationResponse","SecurityTrailer");
		
		generate.writeToDisk(path);
		return true;
	}

	public static void main(String[] args) throws Exception {
		
		XSDTreeSetDao dao = new XSDCreateDaoImpl();
//		XSDTreeDao dao1 = new ListToTree();
		String id = "_CSk2UTJdEeO58eY-wGOnqw";
		String path = "D:\\XSDSchema";
//		dao1.queryListToMap(id,path);
		dao.generateXsd(id,path);
	}
}
