package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.vo.EcoreMessageSetVO;

/**
 * EcoreMessageSet数据库操作
 * @author zqh
 *
 */
public interface EcoreMessageSetDao {

	/**
	 * 保存消息集
	 * @param ecoreMessageSets
	 * @throws Exception
	 */
	public void addEcoreMessageSetList(List<EcoreMessageSet> ecoreMessageSets) throws Exception;
	
	/**
	 * 删除消息集
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreMessageSet(String id) throws Exception;
	
	/**
	 * 保存EcoreMessageSet数据
	 * @param messageSetVOs
	 */
	public boolean saveMessageSetVOList(List<EcoreMessageSetVO> messageSetVOs) throws Exception;
}
