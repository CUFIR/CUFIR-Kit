package com.cfets.cufir.s.data;

import java.util.List;

import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

public class TestFind {

	public static void main(String[] args) throws Exception {
		EcoreDataTypeDao ecoreDataTypeDao=new EcoreDataTypeDaoImpl();
		EcoreTreeNode vo=ecoreDataTypeDao.findEcoreDataTypeTreeNodes();
		System.out.println(vo.getName());
		//BUSINESS_COMPONENT
		EcoreBusinessComponentDao ecoreBusinessComponentDao =new EcoreBusinessComponentDaoImpl();
		vo=ecoreBusinessComponentDao.findEcoreBusinessComponentTreeNodes();
		System.out.println(vo.getName());
		//EcoreExternalSchema
		EcoreExternalSchemaDao ecoreExternalSchemaDao =new EcoreExternalSchemaDaoImpl();
		vo=ecoreExternalSchemaDao.findEcoreExternalSchemaTreeNodes();
		System.out.println(vo.getName());
		//Message_COMPONENT
		EcoreMessageComponentDao ecoreMessageComponentDao =new EcoreMessageComponentDaoImpl();
		vo=ecoreMessageComponentDao.findEcoreMessageComponentTreeNodes();
		System.out.println(vo.getName());
		//EcoreMessageDefinition
		EcoreMessageDefinitionDao ecoreMessageDefinitionDao=new EcoreMessageDefinitionDaoImpl();
		List<EcoreTreeNode> vos=ecoreMessageDefinitionDao.findEcoreMessageTreeNodes();
		for(EcoreTreeNode voa :vos) {
			System.out.println(voa.getName());
		}
		
		List<EcoreTreeNode> ss=ecoreDataTypeDao.findAllEcoreDataTypes();
		ss.clear();
		List<EcoreTreeNode> sss=ecoreBusinessComponentDao.findAllEcoreBusinessComponents();
		sss.clear();
		List<EcoreTreeNode> ssds=ecoreMessageComponentDao.findAllEcoreMessages();
		ssds.clear();
	}

}
