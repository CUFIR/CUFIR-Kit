package com.cfets.cufir.s.data;

import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.dao.impl.EcoreMessageComponentDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentVO;

public class TestFindVO {

	public static void main(String[] args) throws Exception {
		//_Sv8c5AEcEeCQm6a_G2yO_w_-826597608
		EcoreMessageComponentDao mc=new EcoreMessageComponentDaoImpl();
		EcoreMessageComponentVO vo=mc.findMessageComponentVO("_WMG6_dp-Ed-ak6NoX_4Aeg_609276305");
		System.out.println(vo);
		
		/*
		 * EcoreExternalSchemaDao d=new EcoreExternalSchemaDaoImpl();
		 * EcoreExternalSchemaVO v=d.findExternalSchemaVO("_O-3vQKYkEeS0NIlWNmtQyw");
		 * System.out.println(v);
		 * 
		 * EcoreBusinessComponentDao dd=new EcoreBusinessComponentDaoImpl();
		 * EcoreBusinessComponentVO
		 * lll=dd.findBusinessComponentVO("_25OkEDqgEeGZfrkwyRTYgA_-920554182");
		 * System.out.println(lll);
		 * 
		 * EcoreDataTypeDao ddsss=new EcoreDataTypeDaoImpl(); EcoreDataTypeVO
		 * ecoreDataTypeVO=ddsss.findDataTypeVO("_vvgkkMYMEeexPc-mfUU5zQ");
		 * System.out.println(ecoreDataTypeVO);
		 */
		
	}

}
