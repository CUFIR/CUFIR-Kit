package com.cfets.cufir.s.xsd.dao;

public interface XSDTreeSetDao {
//	//查询组装的数据
//	public ListToTree queryListToMap(String ...id);
	//创建生成xsd
	public boolean generateXsd(String id,String path) throws Exception;

}
