package com.cfets.cufir.s.data;

import com.cfets.cufir.s.data.dao.EcoreBusinessComponentDao;
import com.cfets.cufir.s.data.dao.impl.EcoreBusinessComponentDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

public class TestBCinher {

	public static void main(String[] args) {
		try {
			//testexport();
			//testfind();
			//测试生成数据
			EcoreBusinessComponentDao mgr=new EcoreBusinessComponentDaoImpl();
			EcoreTreeNode node=mgr.findBusinessComponentInheritances("_EtLS0MTGEeChad0JzLk7QA_426571401");
			System.out.println(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
