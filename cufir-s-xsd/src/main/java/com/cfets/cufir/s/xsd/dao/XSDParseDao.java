package com.cfets.cufir.s.xsd.dao;

import java.util.Map;

public interface XSDParseDao {
	Map<String, Object>doXSDParse(String strxml) throws Exception;

}
