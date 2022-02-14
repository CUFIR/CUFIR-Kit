package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreConstraint;

/**
 * EcoreConstraint数据库操作
 * @author zqh
 *
 */
public interface EcoreConstraintDao {

	/**
	 * 保存Constraint
	 * @param ecoreConstraints
	 * @throws Exception
	 */
	public void addEcoreConstraintList(List<EcoreConstraint> ecoreConstraints) throws Exception;
}
