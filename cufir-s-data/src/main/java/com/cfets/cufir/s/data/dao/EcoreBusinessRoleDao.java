package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessRole;

/**
 * EcoreBusinessRole数据库操作
 * @author zqh
 *
 */
public interface EcoreBusinessRoleDao {

	public void addEcoreBusinessRoleList(List<EcoreBusinessRole> ecoreBusinessRoles) throws Exception;
}
