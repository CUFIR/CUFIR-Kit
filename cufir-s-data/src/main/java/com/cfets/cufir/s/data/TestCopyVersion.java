package com.cfets.cufir.s.data;

import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;

public class TestCopyVersion {

	public static void main(String[] args) throws Exception {
		EcoreDataTypeDao d=new EcoreDataTypeDaoImpl();
		EcoreDataTypeVO v=d.newCodeSetVersion("_-6cEoZSeEeeh5JjedkaA_g");
		System.out.println(v);
		
		EcoreDataTypeVO vs=d.newCodeSetDerive("_t8lCD4cSEeavwKddCbm3hg");
		System.out.println(vs);
	}

}
