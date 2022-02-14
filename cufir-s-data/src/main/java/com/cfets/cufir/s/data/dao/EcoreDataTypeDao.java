package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * EcoreDataType数据库操作
 * @author zqh
 *
 */
public interface EcoreDataTypeDao {

	/**
	 * 保存EcoreDataType数据
	 * @param ecoreDataTypes
	 */
	public void addEcoreDataTypeList(List<EcoreDataType> ecoreDataTypes) throws Exception;
	
	/**
	 * 获取datatypeNodes
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreDataTypeTreeNodes() throws Exception;
	
	/**
	 * 判断是否已经导入过数据
	 * @return boolean
	 */
	public boolean isImported() throws Exception;
	
	/**
	 * 删除数据类型
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreDataType(String id) throws Exception;
	
	/**
	 * 更新EcoreDataType数据
	 * @param ecoreDataTypes
	 */
	public void updateEcoreDataTypeList(List<EcoreDataType> ecoreDataTypes) throws Exception;

	/**
	 * 保存DataType数据
	 * @param ecoreDataTypeVOs
	 */
	public boolean saveEcoreDataTypeVOList(List<EcoreDataTypeVO> ecoreDataTypeVOs) throws Exception;
	
	/**
	 * 获取datatypeNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreDataTypes() throws Exception;
	
	/**
	 * 查询DataTypeVO数据
	 * @param id
	 */
	public EcoreDataTypeVO findDataTypeVO(String id) throws Exception;
	
	/**
	 * 复制版本
	 * @return
	 * @throws Exception
	 */
	public EcoreDataTypeVO newCodeSetVersion(String id) throws Exception;
	
	/**
	 * 复制Derive
	 * @return
	 * @throws Exception
	 */
	public EcoreDataTypeVO newCodeSetDerive(String id) throws Exception;
}
