package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreExternalSchema;
import com.cfets.cufir.s.data.vo.EcoreExternalSchemaVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * EcoreExternalSchema数据库操作
 * @author zqh
 *
 */
public interface EcoreExternalSchemaDao {

	/**
	 * 保存ExternalSchema
	 * @param ecoreExternalSchemas
	 */
	public void addEcoreExternalSchemaList(List<EcoreExternalSchema> ecoreExternalSchemas) throws Exception;
	
	/**
	 * 获取ExternalSchemaNodes
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreExternalSchemaTreeNodes() throws Exception;
	
	/**
	 * 删除ExternalSchema
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreExternalSchema(String id) throws Exception;
	
	/**
	 * 更新ExternalSchema
	 * @param ecoreExternalSchemas
	 */
	public void updateEcoreExternalSchemaList(List<EcoreExternalSchema> ecoreExternalSchemas) throws Exception;
	
	/**
	 * 保存ExternalSchema
	 * @param ecoreExternalSchemas
	 */
	public boolean saveEcoreExternalSchemaVOList(List<EcoreExternalSchemaVO> ecoreExternalSchemaVOs) throws Exception;
	
	/**
	 * 查询ExternalSchemaVO数据
	 * @param id
	 */
	public EcoreExternalSchemaVO findExternalSchemaVO(String id) throws Exception;
}
