package com.cfets.cufir.s.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.bean.EcoreBusinessElement;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.bean.EcoreMessageSetDefinitionRL;
import com.cfets.cufir.s.data.bean.EcoreNamespaceList;
import com.cfets.cufir.s.data.dao.EcoreBusinessAreaDao;
import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.EcoreExternalSchemaDao;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.dao.EcoreMessageSetDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessAreaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreExternalSchemaDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageDefinitionDaoImpl;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageSetDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreBusinessAreaVO;
import com.cfets.cufir.s.data.vo.EcoreBusinessComponentVO;
import com.cfets.cufir.s.data.vo.EcoreBusinessElementVO;
import com.cfets.cufir.s.data.vo.EcoreCodeVO;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;
import com.cfets.cufir.s.data.vo.EcoreExternalSchemaVO;
import com.cfets.cufir.s.data.vo.EcoreMessageBuildingBlockVO;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentVO;
import com.cfets.cufir.s.data.vo.EcoreMessageDefinitionVO;
import com.cfets.cufir.s.data.vo.EcoreMessageElementVO;
import com.cfets.cufir.s.data.vo.EcoreMessageSetVO;
import com.cfets.cufir.s.data.vo.RemovedObjectVO;
import com.cfets.cufir.s.data.vo.SynonymVO;

public class TestAdd {

	public static void main(String[] args) throws Exception {
		System.out.println("开始保存EcoreDataType ");
		EcoreDataTypeDao ecoreDataTypeDao=new EcoreDataTypeDaoImpl();
		EcoreDataTypeVO ty=new EcoreDataTypeVO();
		List<EcoreDataTypeVO> l=new ArrayList<EcoreDataTypeVO>();
		EcoreDataType tyy=new EcoreDataType();
		tyy.setId("_-IZCEXltEeG7BsjMvd1mEw_1477926493");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ty.setEcoreDataType(tyy);
		
		
		//example
		List<EcoreExample> ecoreExamples =new ArrayList<EcoreExample>();
		EcoreExample a=new EcoreExample();
		a.setId("2222222222222222222222222228888i");
		a.setObj_id("111111111111111111111111111188888i");
		ecoreExamples.add(a);
		ty.setEcoreExamples(ecoreExamples);
		
		//codes
		List<EcoreCodeVO> ecoreCodeVOs =new ArrayList<EcoreCodeVO>();
		EcoreCodeVO vvv=new EcoreCodeVO();
		EcoreCode v =new EcoreCode();
		v.setId("333333333333333333333333333333388i88");
		v.setCodesetid("_-IZCEXltEeG7BsjMvd1mEw_1477926493");
		v.setCodeName("4EEEE");
		vvv.setEcoreCode(v);
		
		//example
		List<EcoreExample> ecoreExampless =new ArrayList<EcoreExample>();
		EcoreExample as=new EcoreExample();
		as.setId("444444444444444444444444i48888");
		as.setObj_id("333333333333333333333333333333388i88");
		ecoreExampless.add(as);
		vvv.setEcoreExamples(ecoreExampless);
		
		//example
		List<EcoreConstraint> ecoreConstraints =new ArrayList<EcoreConstraint>();
		EcoreConstraint cs=new EcoreConstraint();
		cs.setId("555555555555555555i558888");
		cs.setObj_id("333333333333333333333333333333388i88");
		ecoreConstraints.add(cs);
		vvv.setEcoreConstraints(ecoreConstraints);
		
		ecoreCodeVOs.add(vvv);
		ty.setEcoreCodeVOs(ecoreCodeVOs);
		
		List<RemovedObjectVO> removeds=new ArrayList<RemovedObjectVO>();
		RemovedObjectVO v1=new RemovedObjectVO();
		v1.setObj_id("aaaaaaaaaaaaaaaa");
		v1.setObj_type("1");
		removeds.add(v1);
		
		v1=new RemovedObjectVO();
		v1.setObj_id("xxxxxxxxxxxxx");
		v1.setObj_type("2");
		removeds.add(v1);
		
		v1=new RemovedObjectVO();
		v1.setObj_id("gggggggggg");
		v1.setObj_type("3");
		removeds.add(v1);
		
		ty.setRemovedObjectVOs(removeds);
		l.add(ty);
		ecoreDataTypeDao.saveEcoreDataTypeVOList(l);
		
		//saveBusiness();
		//saveExternalSchema();
		saveMessageComponent();
		//saveMessageDefinition();
		//saveMessageSet();
		//saveAERA();
	}

	private static void saveAERA() throws Exception {
		EcoreBusinessAreaDao dddd=new EcoreBusinessAreaDaoImpl();
		
		System.out.println("开始保存BA ");
		EcoreBusinessAreaVO ty=new EcoreBusinessAreaVO();
		List<EcoreBusinessAreaVO> businessAreaVOs=new ArrayList<EcoreBusinessAreaVO>();
		EcoreBusinessArea tyy=new EcoreBusinessArea();
		tyy.setId("aaaaaaaa");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ty.setEcoreBusinessArea(tyy);
		
		//example
		List<EcoreExample> ecoreExampless =new ArrayList<EcoreExample>();
		EcoreExample as=new EcoreExample();
		as.setId("ffffff");
		as.setObj_id("aaaaaaaa");
		ecoreExampless.add(as);
		ty.setEcoreExamples(ecoreExampless);
		
		//example
		List<EcoreConstraint> ecoreConstraints =new ArrayList<EcoreConstraint>();
		EcoreConstraint cs=new EcoreConstraint();
		cs.setId("xxxxxxxxxxxxxxxxxx");
		cs.setObj_id("aaaaaaaa");
		ecoreConstraints.add(cs);
		ty.setEcoreConstraints(ecoreConstraints);
		
		businessAreaVOs.add(ty);
		dddd.saveBusinessAreaVOList(businessAreaVOs);
	}

	private static void saveExternalSchema() throws Exception {
		System.out.println("开始保存saveExternalSchema ");
		EcoreExternalSchemaDao ecoreExternalSchemaDao=new EcoreExternalSchemaDaoImpl();
		EcoreExternalSchemaVO ty=new EcoreExternalSchemaVO();
		List<EcoreExternalSchemaVO> ecoreBusinessComponentVOs=new ArrayList<EcoreExternalSchemaVO>();
		EcoreExternalSchema tyy=new EcoreExternalSchema();
		tyy.setId("aaaaaaaa");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ty.setEcoreExternalSchema(tyy);
		
		
		//example
		List<EcoreExample> ecoreExamples =new ArrayList<EcoreExample>();
		EcoreExample a=new EcoreExample();
		a.setId("ssssssss");
		ecoreExamples.add(a);
		ty.setEcoreExamples(ecoreExamples);
		
		//codes
		List<EcoreNamespaceList> ecoreCodeVOs =new ArrayList<EcoreNamespaceList>();
		EcoreNamespaceList v =new EcoreNamespaceList();
		v.setId("aaaaaaaa");
		v.setObj_id("aaaaaaaa");
		ecoreCodeVOs.add(v);
		ty.setEcoreNamespaceList(ecoreCodeVOs);
		
		//example
		List<EcoreExample> ecoreExampless =new ArrayList<EcoreExample>();
		EcoreExample as=new EcoreExample();
		as.setId("ffffff");
		ecoreExampless.add(as);
		ty.setEcoreExamples(ecoreExampless);
		
		//example
		List<EcoreConstraint> ecoreConstraints =new ArrayList<EcoreConstraint>();
		EcoreConstraint cs=new EcoreConstraint();
		cs.setId("xxxxxxxxxxxxxxxxxx");
		ecoreConstraints.add(cs);
		ty.setEcoreConstraints(ecoreConstraints);
		
		ecoreBusinessComponentVOs.add(ty);
		ecoreExternalSchemaDao.saveEcoreExternalSchemaVOList(ecoreBusinessComponentVOs);
	}

	private static void saveBusiness() throws Exception {
		System.out.println("开始保存saveBusiness ");
		EcoreBusinessComponentDao ecoreBusinessComponentDao=new EcoreBusinessComponentDaoImpl();
		EcoreBusinessComponentVO ty=new EcoreBusinessComponentVO();
		List<EcoreBusinessComponentVO> ecoreBusinessComponentVOs=new ArrayList<EcoreBusinessComponentVO>();
		EcoreBusinessComponent tyy=new EcoreBusinessComponent();
		tyy.setId("bbbbbbbbbbbbbbbb");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ty.setEcoreBusinessComponent(tyy);
		
		
		//example
		List<EcoreExample> ecoreExamples =new ArrayList<EcoreExample>();
		EcoreExample a=new EcoreExample();
		a.setId("nnnnnnnnnnnnnnnn");
		a.setObj_id("mmmmmmmmmmmmmmmmmm");
		ecoreExamples.add(a);
		ty.setEcoreExamples(ecoreExamples);
		
		//codes
		List<EcoreBusinessElementVO> ecoreCodeVOs =new ArrayList<EcoreBusinessElementVO>();
		EcoreBusinessElementVO vvv=new EcoreBusinessElementVO();
		EcoreBusinessElement v =new EcoreBusinessElement();
		v.setId("mmmmmmmmmmmmmmmmmm");
		v.setBusinessComponentId("bbbbbbbbbbbbbbbb");
		v.setIsDerived("true");
		vvv.setEcoreBusinessElement(v);
		
		List<SynonymVO> sss=new ArrayList<SynonymVO>();
		SynonymVO sv=new SynonymVO();
		sv.setObjId("mmmmmmmmmmmmmmmmmm");
		sv.setContext("ddddd");
		sv.setSynonym("ffffff");
		
		SynonymVO svv=new SynonymVO();
		svv.setObjId("mmmmmmmmmmmmmmmmmm");
		svv.setContext("33333333333333");
		svv.setSynonym("77777777777777");
		
		sss.add(sv);
		sss.add(svv);
		vvv.setSynonyms(sss);
		
		//example
		List<EcoreExample> ecoreExampless =new ArrayList<EcoreExample>();
		EcoreExample as=new EcoreExample();
		as.setId("zzzzzzzzzzzzzz");
		as.setObj_id("mmmmmmmmmmmmmmmmmm");
		ecoreExampless.add(as);
		vvv.setEcoreExamples(ecoreExampless);
		
		//example
		List<EcoreConstraint> ecoreConstraints =new ArrayList<EcoreConstraint>();
		EcoreConstraint cs=new EcoreConstraint();
		cs.setId("xxxxxxxxxxxxxxxxxx");
		cs.setObj_id("mmmmmmmmmmmmmmmmmm");
		ecoreConstraints.add(cs);
		vvv.setEcoreConstraints(ecoreConstraints);
		
		ecoreCodeVOs.add(vvv);
		ty.setEcoreBusinessElementVOs(ecoreCodeVOs);
		
		ecoreBusinessComponentVOs.add(ty);
		ecoreBusinessComponentDao.saveEcoreBusinessComponentVOList(ecoreBusinessComponentVOs);
	}
	
	private static void saveMessageComponent() throws Exception {
		System.out.println("开始保存saveMessageComponent ");
		EcoreMessageComponentDao ecoreMessageComponentDao=new EcoreMessageComponentDaoImpl();
		EcoreMessageComponentVO ty=new EcoreMessageComponentVO();
		List<EcoreMessageComponentVO> ecoreBusinessComponentVOs=new ArrayList<EcoreMessageComponentVO>();
		EcoreMessageComponent tyy=new EcoreMessageComponent();
		tyy.setId("rrrrrrrrrrrrrr");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ty.setEcoreMessageComponent(tyy);
		
		
		//example
		List<EcoreExample> ecoreExamples =new ArrayList<EcoreExample>();
		EcoreExample a=new EcoreExample();
		a.setId("ttttttttttt");
		ecoreExamples.add(a);
		ty.setEcoreExamples(ecoreExamples);
		
		//codes
		List<EcoreMessageElementVO> ecoreCodeVOs =new ArrayList<EcoreMessageElementVO>();
		EcoreMessageElementVO vvv=new EcoreMessageElementVO();
		EcoreMessageElement v =new EcoreMessageElement();
		v.setId("yyyyyyyyyyyy");
		v.setMessageComponentId("rrrrrrrrrrrrrr");
		vvv.setEcoreMessageElement(v);
		
		List<SynonymVO> sss=new ArrayList<SynonymVO>();
		SynonymVO sv=new SynonymVO();
		sv.setObjId("yyyyyyyyyyyy");
		sv.setContext("111111111111111");
		sv.setSynonym("2222222222222222");
		
		SynonymVO svv=new SynonymVO();
		svv.setObjId("yyyyyyyyyyyy");
		svv.setContext("33333333333");
		svv.setSynonym("4444444444444");
		
		sss.add(sv);
		sss.add(svv);
		vvv.setSynonyms(sss);
		
		//example
		List<EcoreExample> ecoreExampless =new ArrayList<EcoreExample>();
		EcoreExample as=new EcoreExample();
		as.setId("zzzzzzzzzzzzzz");
		ecoreExampless.add(as);
		vvv.setEcoreExamples(ecoreExampless);
		
		//example
		List<EcoreConstraint> ecoreConstraints =new ArrayList<EcoreConstraint>();
		EcoreConstraint cs=new EcoreConstraint();
		cs.setId("xxxxxxxxxxxxxxxxxx");
		ecoreConstraints.add(cs);
		vvv.setEcoreConstraints(ecoreConstraints);
		
		ecoreCodeVOs.add(vvv);
		ty.setEcoreMessageElementVOs(ecoreCodeVOs);
		
		ecoreBusinessComponentVOs.add(ty);
		ecoreMessageComponentDao.saveMessageComponentVOList(ecoreBusinessComponentVOs);
	}
	
	private static void saveMessageDefinition() throws Exception {
		System.out.println("开始保存saveMessageDefinition ");
		EcoreMessageDefinitionDao ecoreMessageDefinitionDao=new EcoreMessageDefinitionDaoImpl();
		EcoreMessageDefinitionVO ty=new EcoreMessageDefinitionVO();
		List<EcoreMessageDefinitionVO> ecoreMessageDefinitionVOs=new ArrayList<EcoreMessageDefinitionVO>();
		EcoreMessageDefinition tyy=new EcoreMessageDefinition();
		tyy.setId("uuuuuuuuuuuuuuuuu");
		tyy.setUpdateTime(new Date());
		tyy.setMessageDefinitionIdentifier("sssss.222.333.444");
		tyy.setIsfromiso20022("1");
		ty.setEcoreMessageDefinition(tyy);
		
		List<SynonymVO> sss=new ArrayList<SynonymVO>();
		SynonymVO sv=new SynonymVO();
		sv.setObjId("uuuuuuuuuuuuuuuuu");
		sv.setContext("555555555555");
		sv.setSynonym("6666666666666666");
		
		SynonymVO svv=new SynonymVO();
		svv.setObjId("uuuuuuuuuuuuuuuuu");
		svv.setContext("7777777777777777");
		svv.setSynonym("88888888888888");
		
		sss.add(sv);
		sss.add(svv);
		ty.setSynonyms(sss);
		
		
		//example
		List<EcoreExample> ecoreExamples =new ArrayList<EcoreExample>();
		EcoreExample a=new EcoreExample();
		a.setId("iiiiiiiiiiiiiiiiiiiiii");
		ecoreExamples.add(a);
		ty.setEcoreExamples(ecoreExamples);
		
		//codes
		List<EcoreMessageBuildingBlockVO> ecoreCodeVOs =new ArrayList<EcoreMessageBuildingBlockVO>();
		EcoreMessageBuildingBlockVO vvv=new EcoreMessageBuildingBlockVO();
		EcoreMessageBuildingBlock v =new EcoreMessageBuildingBlock();
		v.setId("oooooooooooooooooooo");
		v.setMessageId("uuuuuuuuuuuuuuuuu");
		vvv.setEcoreMessageBuildingBlock(v);
		
		List<SynonymVO> ssss=new ArrayList<SynonymVO>();
		SynonymVO svs=new SynonymVO();
		svs.setObjId("oooooooooooooooooooo");
		svs.setContext("99999999999999");
		svs.setSynonym("999999999999999");
		
		SynonymVO svvs=new SynonymVO();
		svvs.setObjId("oooooooooooooooooooo");
		svvs.setContext("000000000000");
		svvs.setSynonym("0000000000000");
		
		ssss.add(svs);
		ssss.add(svvs);
		vvv.setSynonyms(ssss);
		
		//example
		List<EcoreExample> ecoreExampless =new ArrayList<EcoreExample>();
		EcoreExample as=new EcoreExample();
		as.setId("pppppppppppppppppp");
		ecoreExampless.add(as);
		vvv.setEcoreExamples(ecoreExampless);
		
		//example
		List<EcoreConstraint> ecoreConstraints =new ArrayList<EcoreConstraint>();
		EcoreConstraint cs=new EcoreConstraint();
		cs.setId("[[[[[[[[[[[[[[[[[[[[");
		ecoreConstraints.add(cs);
		vvv.setEcoreConstraints(ecoreConstraints);
		
		ecoreCodeVOs.add(vvv);
		ty.setEcoreMessageBuildingBlockVOs(ecoreCodeVOs);
		
		EcoreBusinessArea aa=new EcoreBusinessArea();
		aa.setId("ddddddddddddd");
		aa.setName("dddddddd");
		ty.setEcoreBusinessArea(aa);
		
		ecoreMessageDefinitionVOs.add(ty);
		ecoreMessageDefinitionDao.saveMessageDefinitionVOList(ecoreMessageDefinitionVOs);
	}
	
	private static void saveMessageSet() throws Exception {
		System.out.println("开始保存saveMessageSet ");
		EcoreMessageSetDao ecoreMessageSetDao=new EcoreMessageSetDaoImpl();
		EcoreMessageSetVO ty=new EcoreMessageSetVO();
		List<EcoreMessageSetVO> ecoreMessageSetVOs=new ArrayList<EcoreMessageSetVO>();
		EcoreMessageSet tyy=new EcoreMessageSet();
		tyy.setId("cccccccccccccc");
		tyy.setUpdateTime(new Date());
		tyy.setIsfromiso20022("1");
		ty.setEcoreMessageSet(tyy);
		
		//codes
		List<EcoreMessageSetDefinitionRL> ecoreCodeVOs =new ArrayList<EcoreMessageSetDefinitionRL>();
		EcoreMessageSetDefinitionRL vvv=new EcoreMessageSetDefinitionRL();
		vvv.setSetId("cccccccccccccc");
		vvv.setMessageId("ddddddddddddddd");
		ecoreCodeVOs.add(vvv);
		ty.setEcoreMessageSetDefinitionRLs(ecoreCodeVOs);
		
		ecoreMessageSetVOs.add(ty);
		ecoreMessageSetDao.saveMessageSetVOList(ecoreMessageSetVOs);
	}
}
