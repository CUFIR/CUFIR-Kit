package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessProcess;

/**
 * EcoreBusinessProcess数据库操作
 * @author zqh
 *
 */
public interface EcoreBusinessProcessDao {

	public void addEcoreBusinessProcessList(List<EcoreBusinessProcess> ecoreBusinessProcesss) throws Exception;
}
