package com.cfets.cufir.s.data.dao;

import java.util.List;
import java.util.Map;

import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.vo.EcoreFullBusinessModelVO;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentTracesVO;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * EcoreMessageComponent数据库操作
 * @author zqh
 *
 */
public interface EcoreMessageComponentDao {

	/**
	 * 保存MessageComponent
	 * @param ecoreMessageComponents
	 */
	public void addEcoreMessageComponentList(List<EcoreMessageComponent> ecoreMessageComponents) throws Exception;

	/**
	 * 获取MessageComponentNodes
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreMessageComponentTreeNodes() throws Exception;
	
	/**
	 * 获取MessageComponentNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreMessages() throws Exception;
	
	/**
	 * 删除消息组件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreMessageComponent(String id) throws Exception;
	
	/**
	 * 保存MessageComponent数据
	 * @param ecoreMessageComponentVOs
	 */
	public boolean saveMessageComponentVOList(List<EcoreMessageComponentVO> messageComponentVOs) throws Exception;
	
	/**
	 * 查询MessageComponentVO数据
	 * @param id
	 */
	public EcoreMessageComponentVO findMessageComponentVO(String id) throws Exception;
	
	/**
	 * 查询所有mc的名称
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> findAllMessageComponentName() throws Exception;
	
	/**
	 * 获取model信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<EcoreMessageComponentTracesVO> findMessageComponentTrace(String id) throws Exception;
	
}
