package com.cfets.cufir.s.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponentRL;
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreDoclet;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.bean.EcoreNamespaceList;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkup;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;
import com.cfets.cufir.s.data.dao.EcoreBusinessAreaDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentRLDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessElementDao;
import com.cfets.cufir.s.data.dao.EcoreCodeDao;
import com.cfets.cufir.s.data.dao.EcoreConstraintDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreDocletDao;
import com.cfets.cufir.s.data.dao.EcoreExampleDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreNamespaceListDao;
import com.cfets.cufir.s.data.dao.EcoreNextVersionsDao;
import com.cfets.cufir.s.data.dao.EcoreSemanticMarkupDao;
import com.cfets.cufir.s.data.dao.EcoreSemanticMarkupElementDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessAreaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentRLDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessElementDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreCodeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreConstraintDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDocletDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExampleDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreNamespaceListDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreNextVersionsDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreSemanticMarkupDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreSemanticMarkupElementDaoImpl;

public class TestUpdate {

	public static void main(String[] args) throws Exception {
		System.out.println("开始更新 ");
		EcoreDataTypeDao ecoreDataTypeDao=new EcoreDataTypeDaoImpl();
		EcoreDataType ty=new EcoreDataType();
		List<EcoreDataType> l=new ArrayList<EcoreDataType>();
		ty.setId("_-6cEoZSeEeeh5JjedkaA_g");
		ty.setUpdateTime(new Date());
		ty.setIsfromiso20022("1");
		l.add(ty);
		ecoreDataTypeDao.updateEcoreDataTypeList(l);
		
		EcoreBusinessAreaDao ecoreBusinessAreaDao=new EcoreBusinessAreaDaoImpl();
		EcoreBusinessArea tyy=new EcoreBusinessArea();
		List<EcoreBusinessArea> ll=new ArrayList<EcoreBusinessArea>();
		tyy.setId("_-6cEoZSeEeeh5JjedkaA_g");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ll.add(tyy);
		ecoreBusinessAreaDao.updateEcoreBusinessAreaList(ll);
		
		EcoreBusinessComponentDao ecoreBusinessComponentDao=new EcoreBusinessComponentDaoImpl();
		EcoreBusinessComponent tyyy=new EcoreBusinessComponent();
		List<EcoreBusinessComponent> lly=new ArrayList<EcoreBusinessComponent>();
		tyyy.setId("_-6cEoZSeEeeh5JjedkaA_g");
		tyyy.setUpdateTime(new Date());
		tyyy.setIsfromiso20022("1");
		lly.add(tyyy);
		ecoreBusinessComponentDao.updateEcoreBusinessComponentList(lly);
		
		EcoreBusinessComponentRLDao ecoreBusinessComponentRLDao=new EcoreBusinessComponentRLDaoImpl();
		EcoreBusinessComponentRL tyyyy=new EcoreBusinessComponentRL();
		List<EcoreBusinessComponentRL> llyy=new ArrayList<EcoreBusinessComponentRL>();
		tyyyy.setId("_-6cEoZSeEeeh5JjedkaA_g");
		tyyyy.setUpdateTime(new Date());
		tyyyy.setIsfromiso20022("1");
		llyy.add(tyyyy);
		
		EcoreExternalSchemaDao ecoreExternalSchemaDao=new EcoreExternalSchemaDaoImpl();
		EcoreExternalSchema tyyyyyyyyyy=new EcoreExternalSchema();
		List<EcoreExternalSchema> llyyyyyyyy=new ArrayList<EcoreExternalSchema>();
		tyyyyyyyyyy.setId("_-6cEoZSeEeeh5JjedkaA_g");
		tyyyyyyyyyy.setUpdateTime(new Date());
		tyyyyyyyyyy.setIsfromiso20022("1");
		llyyyyyyyy.add(tyyyyyyyyyy);
		ecoreExternalSchemaDao.updateEcoreExternalSchemaList(llyyyyyyyy);
	}

}
