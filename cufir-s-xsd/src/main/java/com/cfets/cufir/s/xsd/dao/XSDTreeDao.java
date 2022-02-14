package com.cfets.cufir.s.xsd.dao;

import java.sql.SQLException;
import java.util.Map;

public interface XSDTreeDao {
	public  Map<String, Object> queryListToMap(String id,String path)throws SQLException;

}
