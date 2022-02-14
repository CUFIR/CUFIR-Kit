package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.vo.EcoreDocumentTreeNode;
import com.cfets.cufir.s.data.vo.EcoreMessageDefinitionVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * EcoreMessageDefinition数据库操作
 * @author zqh
 *
 */
public interface EcoreMessageDefinitionDao {

	/**
	 * 保存Message
	 * @param ecoreMessageDefinitions
	 */
	public void addEcoreMessageDefinitionList(List<EcoreMessageDefinition> ecoreMessageDefinitions) throws Exception;

	/**
	 * 获取MessageNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findEcoreMessageTreeNodes() throws Exception;
	
	/**
	 * 删除消息定义
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreMessageDefinition(String id) throws Exception;
	
	/**
	 * 保存MessageDefinition数据
	 * @param ecoreMessageDefinitionVOs
	 */
	public boolean saveMessageDefinitionVOList(List<EcoreMessageDefinitionVO> messageDefinitionVOs) throws Exception;
	
	/**
	 * 按照id获取消息相关的数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Object> findMessageDataById(String id) throws Exception;
	
	/**
	 * 按照id获取消息相关的数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public EcoreDocumentTreeNode findMessageDocumentDataById(String id) throws Exception;
}
