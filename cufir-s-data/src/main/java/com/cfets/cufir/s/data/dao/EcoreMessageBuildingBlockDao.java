package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;

/**
 * EcoreMessageBuildingBlock数据库操作
 * @author zqh
 *
 */
public interface EcoreMessageBuildingBlockDao {

	/**
	 * 保存MessageBuildingBlock
	 * @param ecoreMessageBuildingBlocks
	 * @throws Exception
	 */
	public void addEcoreMessageBuildingBlockList(List<EcoreMessageBuildingBlock> ecoreMessageBuildingBlocks) throws Exception;
}
