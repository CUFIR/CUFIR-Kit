package com.cfets.cufir.s.data;

import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;

public class TestDelete {

	public static void main(String[] args) throws Exception {
		boolean key1;
		EcoreMessageDefinitionDao ecoreMessageDefinitionDao=new EcoreMessageDefinitionDaoImpl();
		key1=ecoreMessageDefinitionDao.deleteEcoreMessageDefinition("_-hesiaMOEeCojJW5vEuTEQ_-1558125935");
		System.out.println(key1);
		
		EcoreBusinessComponentDao dao=new EcoreBusinessComponentDaoImpl();
		key1=dao.deleteEcoreBusinessComponent("_6rPswJI8EeOYT5ya27Izag");
		System.out.println(key1);
		
		EcoreMessageComponentDao dao2=new EcoreMessageComponentDaoImpl();
		key1=dao2.deleteEcoreMessageComponent("_--yXpzrJEeK30t5ANaHeyQ");
		System.out.println(key1);
		
		EcoreDataTypeDao d=new EcoreDataTypeDaoImpl();
		d.deleteEcoreDataType("_6rPswJI8EeOYT5ya27Izag");
		
	}

}
