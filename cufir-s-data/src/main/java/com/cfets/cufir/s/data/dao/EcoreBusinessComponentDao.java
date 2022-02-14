package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.vo.EcoreBusinessComponentVO;
import com.cfets.cufir.s.data.vo.EcoreFullBusinessModelVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * EcoreBusinessComponent数据库操作
 * @author zqh
 *
 */
public interface EcoreBusinessComponentDao {

	/**
	 * 保存BusinessComponents
	 * @param ecoreBusinessComponents
	 */
	public void addEcoreBusinessComponentList(List<EcoreBusinessComponent> ecoreBusinessComponents) throws Exception;
	
	/**
	 * 获取BusinessComponentNodes
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreBusinessComponentTreeNodes() throws Exception;
	
	/**
	 * 删除业务组件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreBusinessComponent(String id) throws Exception;
	
	/**
	 * 更新BusinessComponents
	 * @param ecoreBusinessComponents
	 */
	public void updateEcoreBusinessComponentList(List<EcoreBusinessComponent> ecoreBusinessComponents) throws Exception;

	/**
	 * 保存BusinessComponent数据
	 * @param ecoreBusinessComponentVOs
	 */
	public boolean saveEcoreBusinessComponentVOList(List<EcoreBusinessComponentVO> ecoreBusinessComponentVOs) throws Exception;
	
	/**
	 * 获取BusinessComponentNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreBusinessComponents() throws Exception;
	
	/**
	 * 查询BusinessComponentVO数据
	 * @param id
	 */
	public EcoreBusinessComponentVO findBusinessComponentVO(String id) throws Exception;
	
	/**
	 * 获取Inheritance信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public EcoreTreeNode findBusinessComponentInheritances(String id) throws Exception;
	
	/**
	 * 获取model信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<EcoreFullBusinessModelVO> findFullBusinessModel(String id) throws Exception;
}
