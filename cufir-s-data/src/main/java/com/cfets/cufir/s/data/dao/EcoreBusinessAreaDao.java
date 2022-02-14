package com.cfets.cufir.s.data.dao;

import java.util.List;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.vo.EcoreBusinessAreaVO;

/**
 * EcoreBusinessArea数据库操作
 * @author zqh
 *
 */
public interface EcoreBusinessAreaDao {

	/**
	 * 保存业务领域信息
	 * @param ecoreBusinessAreas
	 * @throws Exception
	 */
	public void addEcoreBusinessAreaList(List<EcoreBusinessArea> ecoreBusinessAreas) throws Exception;
	
	/**
	 * 删除业务领域
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreBusinessArea(String id) throws Exception;
	
	/**
	 * 更新业务领域信息
	 * @param ecoreBusinessAreas
	 * @throws Exception
	 */
	public void updateEcoreBusinessAreaList(List<EcoreBusinessArea> ecoreBusinessAreas) throws Exception;

	/**
	 * 保存EcoreBusinessArea数据
	 * @param BusinessAreaVOs
	 */
	public boolean saveBusinessAreaVOList(List<EcoreBusinessAreaVO> businessAreaVOs) throws Exception;
}
