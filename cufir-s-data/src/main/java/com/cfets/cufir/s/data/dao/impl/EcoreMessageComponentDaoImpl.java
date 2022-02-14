package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageElement;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkup;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;
import com.cfets.cufir.s.data.dao.EcoreMessageComponentDao;
import com.cfets.cufir.s.data.util.DerbyUtil;
import com.cfets.cufir.s.data.vo.EcoreDocumentTreeNode;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentTracesVO;
import com.cfets.cufir.s.data.vo.EcoreMessageComponentVO;
import com.cfets.cufir.s.data.vo.EcoreMessageElementVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.data.vo.RemovedObjectVO;
import com.cfets.cufir.s.data.vo.SynonymVO;

/**
 * EcoreMessageComponent数据库操作
 * @author zqh
 *
 */
public class EcoreMessageComponentDaoImpl implements EcoreMessageComponentDao{

	private static Logger logger = Logger.getLogger(EcoreMessageComponentDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement st=null;
	private ResultSet rs = null;
	private Set<String> mcSets=null;
	
	/**
	 * 获取MessageComponentNodes
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreMessageComponentTreeNodes() throws Exception{
		EcoreTreeNode ecoreTreeNode=new EcoreTreeNode();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String bsql = "SELECT T.ID,T.NAME,T.COMPONENT_TYPE,t.registration_status FROM ECORE_MESSAGE_COMPONENT T ORDER BY T.NAME";
		String eSql = "SELECT T.ID,T.NAME,T.MESSAGE_COMPONENT_ID,T.TYPE,t.registration_status FROM ECORE_MESSAGE_ELEMENT T ORDER BY T.NAME";
		Map<String,List<EcoreTreeNode>> elementMaps=new HashMap<String,List<EcoreTreeNode>>();
		List<EcoreTreeNode> messageComponents=new ArrayList<EcoreTreeNode>();
		try {
			st=conn.createStatement();
			//获取element
			rs=st.executeQuery(eSql);
			while(rs.next()) {
				EcoreTreeNode po =new EcoreTreeNode();
            	po.setId(rs.getString(1));
            	po.setName(rs.getString(2));
            	po.setType(rs.getString(4));
            	po.setLevel("3");
            	po.setRegistrationStatus(rs.getString("registration_status"));
            	String messageComponentId=rs.getString(3);
            	if(elementMaps.containsKey(messageComponentId)) {
            		elementMaps.get(messageComponentId).add(po);
            	}else {
    				List<EcoreTreeNode> nodes=new ArrayList<EcoreTreeNode>();
            		nodes.add(po);
            		elementMaps.put(messageComponentId,nodes);
            	}
            }
			//获取MESSAGE_COMPONENT
			rs=st.executeQuery(bsql);
			while(rs.next()) {
				EcoreTreeNode po =new EcoreTreeNode();
				String mId=rs.getString(1);
            	po.setId(rs.getString(1));
            	po.setName(rs.getString(2));
            	po.setType(rs.getString(3));
            	po.setLevel("2");
            	po.setRegistrationStatus(rs.getString("registration_status"));
            	po.setChildNodes(elementMaps.get(mId));
            	messageComponents.add(po);
			}
			//顶级
			ecoreTreeNode.setName("Message Components");
			ecoreTreeNode.setLevel("1");
			ecoreTreeNode.setType("4");
			ecoreTreeNode.setChildNodes(messageComponents);
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return ecoreTreeNode;
	}
	
	/**
	 * 获取MessageComponentNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreMessages() throws Exception{
		List<EcoreTreeNode> ecoreTreeNodes=new ArrayList<EcoreTreeNode>();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String bsql = "SELECT T.ID,T.NAME,T.COMPONENT_TYPE,t.registration_status FROM ECORE_MESSAGE_COMPONENT T ORDER BY T.NAME";
		try {
			st=conn.createStatement();
			//获取MESSAGE_COMPONENT
			rs=st.executeQuery(bsql);
			while(rs.next()) {
				EcoreTreeNode po =new EcoreTreeNode();
            	po.setId(rs.getString(1));
            	po.setName(rs.getString(2));
            	po.setType(rs.getString(3));
            	po.setLevel("2");
            	po.setRegistrationStatus(rs.getString("registration_status"));
            	ecoreTreeNodes.add(po);
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return ecoreTreeNodes;
	}
	
	/**
	 * 保存MessageComponent
	 * @param ecoreMessageComponents
	 */
	public void addEcoreMessageComponentList(List<EcoreMessageComponent> ecoreMessageComponents) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_MESSAGE_COMPONENT(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,component_type,version,trace,technical"
					+ ",trace_name,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreMessageComponent po : ecoreMessageComponents) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5,Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getComponentType());
				pstmt.setString(9, po.getVersion());
				pstmt.setString(10, po.getTrace());
				pstmt.setString(11, po.getTechnical());
				pstmt.setString(12, po.getTraceName());
				pstmt.setString(13, po.getCreateUser());
				pstmt.setTimestamp(14, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(15, po.getIsfromiso20022());
				pstmt.addBatch();
			}
			int[] result = pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
			if (result != null) {
				int count = 0;
				for (int i = 0; i < result.length; i++) {
					count = count + result[i];
				}
				logger.info(count + "件数据更新成功 SQL=" + sql);
			} else {
				logger.info("没有数据被更新 SQL=" + sql);
			}
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
	}
	
	private void saveRemovedInfo(String id) throws Exception{
		try {
			String sql = "insert into ECORE_REMOVED_INFO(obj_id,obj_type,create_time) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, "9");
			pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			pstmt.executeUpdate();// 执行更新语句
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		}
	}
	
	
	private void saveRemovedInfo(List<RemovedObjectVO> removeds) throws Exception {
		try {
			String sql = "insert into ECORE_REMOVED_INFO(obj_id,obj_type,create_time) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for(RemovedObjectVO v:removeds) {
				pstmt.setString(1, v.getObj_id());
				pstmt.setString(2, v.getObj_type());
				pstmt.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
				pstmt.addBatch();
			}
			pstmt.executeBatch();// 执行更新语句
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		}
	}
	
	/**
	 * 删除消息组件
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreMessageComponent(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			String presql="select id from ECORE_MESSAGE_ELEMENT where MESSAGE_COMPONENT_ID='"+id+"'";
			String sql = "delete from ECORE_MESSAGE_COMPONENT where id=?" ;
			//common
			String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
			String dsql = "delete from ECORE_DOCLET where obj_id=?";
			String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
			String vsql="delete from ECORE_NEXT_VERSIONS where id=?";
			st=conn.createStatement();
			String mbIds="";
			//获取block数据
			rs=st.executeQuery(presql);
			while(rs.next()) {
            	String mbId=rs.getString(1);
            	mbIds+="'"+mbId+"',";
            }
			if(!"".equals(mbIds)) {
				mbIds=mbIds.substring(0, mbIds.length()-1);
				String esql = "delete from ECORE_MESSAGE_ELEMENT where id in ("+mbIds+")";
				//common
				String bsmsql = "delete from ecore_semantic_markup  where obj_id in ("+mbIds+")";
				String bsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
						+ " (select id from ecore_semantic_markup where obj_id in ("+mbIds+"))";
				csql+=" or obj_id in ("+mbIds+")";
				dsql+=" or obj_id in ("+mbIds+")";
				eeql+=" or obj_id in ("+mbIds+")";
				vsql+=" or id in ("+mbIds+")";
				
				pstmt = conn.prepareStatement(bsemsql);
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(bsmsql);
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(esql);
				pstmt.executeUpdate();
			}
			pstmt = conn.prepareStatement(csql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(dsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			//saveRemovedInfo(id);
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int num=pstmt.executeUpdate();
			
			conn.commit();
			if(num>0) {
				key=true;
			}
		} catch (Exception e) {
			logger.error("删除数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return key;
	}
	
	
	/**
	 * 保存MessageComponent数据
	 * @param ecoreMessageComponentVOs
	 */
	public boolean saveMessageComponentVOList(List<EcoreMessageComponentVO> messageComponentVOs) throws Exception{
		boolean key=false;
		List<EcoreMessageComponent> ecoreMessageComponents=new ArrayList<EcoreMessageComponent>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreMessageElement> ecoreElements=new ArrayList<EcoreMessageElement>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		List<EcoreSemanticMarkup> ecoreSemanticMarkups=new ArrayList<EcoreSemanticMarkup>();
		List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements=new ArrayList<EcoreSemanticMarkupElement>();
		List<String> dataIds=new ArrayList<String>();
		List<RemovedObjectVO> removeds=new ArrayList<RemovedObjectVO>();
		//解析保存的内容
		for(EcoreMessageComponentVO vo: messageComponentVOs) {
			EcoreMessageComponent ecoreMessageComponent=vo.getEcoreMessageComponent();
			List<EcoreConstraint> constraints=vo.getEcoreConstraints();
			List<EcoreExample> examples=vo.getEcoreExamples();
			List<EcoreNextVersions> nextVersions=vo.getEcoreNextVersions();
			List<EcoreMessageElementVO> eVOs=vo.getEcoreMessageElementVOs();
			if(constraints!=null) {
				ecoreConstraints.addAll(constraints);
			}
			if(examples!=null) {
				ecoreExamples.addAll(examples);
			}
			if(nextVersions!=null) {
				ecoreNextVersions.addAll(nextVersions);
			}
			//解析CODE
			if(eVOs!=null) {
				for(EcoreMessageElementVO cvo:eVOs) {
					EcoreMessageElement ecoreMessageElement=cvo.getEcoreMessageElement();
					List<EcoreConstraint> cconstraints=cvo.getEcoreConstraints();
					List<EcoreExample> cexamples=cvo.getEcoreExamples();
					List<EcoreNextVersions> cnextVersions=cvo.getEcoreNextVersions();
					List<SynonymVO> ss=cvo.getSynonyms();
					List<RemovedObjectVO> rmds=vo.getRemovedObjectVOs();
					if(cconstraints!=null && !cconstraints.isEmpty()) {
						ecoreConstraints.addAll(cconstraints);
					}
					if(cexamples!=null && !cexamples.isEmpty()) {
						ecoreExamples.addAll(cexamples);
					}
					if(cnextVersions!=null && !cnextVersions.isEmpty()) {
						ecoreNextVersions.addAll(cnextVersions);
					}
					if(ss!=null && !ss.isEmpty()) {
						for(SynonymVO sv:ss) {
							String muId=UUID.randomUUID().toString();
							EcoreSemanticMarkup up=new EcoreSemanticMarkup();
							up.setObjId(sv.getObjId());
							up.setType("Synonym");
							up.setIsfromiso20022(ecoreMessageElement.getIsfromiso20022());
							up.setObjType("10");
							up.setId(muId);
							
							String muIdC=UUID.randomUUID().toString();
							EcoreSemanticMarkupElement smec=new EcoreSemanticMarkupElement();
							smec.setId(muIdC);
							smec.setIsfromiso20022(ecoreMessageElement.getIsfromiso20022());
							smec.setName("context");
							smec.setValue(sv.getContext());
							smec.setSemanticMarkupId(muId);

							String muIdS=UUID.randomUUID().toString();
							EcoreSemanticMarkupElement smes=new EcoreSemanticMarkupElement();
							smes.setId(muIdS);
							smes.setIsfromiso20022(ecoreMessageElement.getIsfromiso20022());
							smes.setName("value");
							smes.setValue(sv.getSynonym());
							smes.setSemanticMarkupId(muId);
							ecoreSemanticMarkups.add(up);
							ecoreSemanticMarkupElements.add(smes);
							ecoreSemanticMarkupElements.add(smec);
						}
					}
					if(rmds!=null && !rmds.isEmpty()) {
						removeds.addAll(rmds);
					}
					ecoreElements.add(ecoreMessageElement);
				}
			}
			dataIds.add(ecoreMessageComponent.getId());
			ecoreMessageComponents.add(ecoreMessageComponent);
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if(dataIds!=null && !dataIds.isEmpty()) {
				for(String id:dataIds) {
					//先删除数据类型下面的所有数据
					deleteMessageComponentByIds(id);
				}
			}
			//删除已有的
			if(removeds!=null && !removeds.isEmpty()) {
				//saveRemovedInfo(removeds);
			}
			//开始保存
			if(ecoreMessageComponents!=null && !ecoreMessageComponents.isEmpty()) {
				saveMessageComponent(ecoreMessageComponents);
				if(ecoreElements!=null && !ecoreElements.isEmpty()) {
					saveMessageElement(ecoreElements);
				}
				if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
					saveExample(ecoreExamples);
				}
				if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
					saveConstraint(ecoreConstraints);
				}
				if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
					saveNextVersions(ecoreNextVersions);
				}
				if(ecoreSemanticMarkups!=null && !ecoreSemanticMarkups.isEmpty()) {
					saveSemanticMarkup(ecoreSemanticMarkups);
					if(ecoreSemanticMarkupElements!=null && !ecoreSemanticMarkupElements.isEmpty()) {
						saveSemanticMarkupElement(ecoreSemanticMarkupElements);
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		key=true;
		return key;
	}
	
	private void deleteMessageComponentByIds(String id) throws Exception{
		String presql="select id from ECORE_MESSAGE_ELEMENT where MESSAGE_COMPONENT_ID='"+id+"'";
		//common
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
		String dsql = "delete from ECORE_DOCLET where obj_id=?";
		String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
		String vsql="delete from ECORE_NEXT_VERSIONS where id=?";
		st=conn.createStatement();
		String mbIds="";
		//获取block数据
		rs=st.executeQuery(presql);
		while(rs.next()) {
        	String mbId=rs.getString(1);
        	mbIds+="'"+mbId+"',";
        }
		if(!"".equals(mbIds)) {
			mbIds=mbIds.substring(0, mbIds.length()-1);
			String esql = "delete from ECORE_MESSAGE_ELEMENT where id in ("+mbIds+")";
			//common
			String bsmsql = "delete from ecore_semantic_markup  where obj_id in ("+mbIds+")";
			String bsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
					+ " (select id from ecore_semantic_markup where obj_id in ("+mbIds+"))";
			csql+=" or obj_id in ("+mbIds+")";
			dsql+=" or obj_id in ("+mbIds+")";
			eeql+=" or obj_id in ("+mbIds+")";
			vsql+=" or id in ("+mbIds+")";
			
			pstmt = conn.prepareStatement(bsemsql);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(bsmsql);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(esql);
			pstmt.executeUpdate();
		}
		pstmt = conn.prepareStatement(csql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(dsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(eeql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(vsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
	}
	
	private void saveMessageComponent(List<EcoreMessageComponent> ecoreMessageComponents) throws Exception{
		List<EcoreMessageComponent> addEcoreMessageComponents=new ArrayList<EcoreMessageComponent>();
		List<EcoreMessageComponent> updateEcoreMessageComponents=new ArrayList<EcoreMessageComponent>();
		for(EcoreMessageComponent po : ecoreMessageComponents) {
			String csql = "SELECT T.ID FROM ECORE_MESSAGE_COMPONENT T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreMessageComponents.add(po);
			}else {
				addEcoreMessageComponents.add(po);
			}
		}
		String sql = "insert into ECORE_MESSAGE_COMPONENT(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,component_type,version,trace,technical"
				+ ",trace_name,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreMessageComponent po : addEcoreMessageComponents) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getComponentType());
			pstmt.setString(9, po.getVersion());
			pstmt.setString(10, po.getTrace());
			pstmt.setString(11, po.getTechnical());
			pstmt.setString(12, po.getTraceName());
			pstmt.setString(13, po.getCreateUser());
			pstmt.setTimestamp(14, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(15, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		
		String usql = "update ECORE_MESSAGE_COMPONENT set update_time=?,name=?,definition=?,registration_status=?"
				+ ",removal_date=?,object_identifier=?,previous_version=?,component_type=?,version=?,trace=?"
				+ ",technical=?,trace_name=?,update_user=?,is_from_iso20022=?  where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreMessageComponent po : updateEcoreMessageComponents) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getComponentType());
			pstmt.setString(9, po.getVersion());
			pstmt.setString(10, po.getTrace());
			pstmt.setString(11, po.getTechnical());
			pstmt.setString(12, po.getTraceName());
			pstmt.setString(13, po.getUpdateUser());
			pstmt.setString(14, po.getIsfromiso20022());
			pstmt.setString(15, po.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveExample(List<EcoreExample> ecoreExamples) throws Exception{
		String sql = "insert into ecore_example(id,obj_id,obj_type,example,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreExample po : ecoreExamples) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getObj_type());
			pstmt.setString(4, po.getExample());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveMessageElement(List<EcoreMessageElement> ecoreMessageElements) throws Exception{
		String sql = "insert into ECORE_MESSAGE_ELEMENT(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,xml_Tag,type,type_Id,max_Occurs,min_Occurs,"
				+ "message_Component_Id,version,is_Message_Association_End,is_derived,trace_type,trace"
				+ ",technical,traces_to,type_of_traces_to,trace_path,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreMessageElement po : ecoreMessageElements) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5,Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getXmlTag());
			pstmt.setString(9, po.getType());
			pstmt.setString(10, po.getTypeId());
			if(po.getMaxOccurs()!=null) {
				pstmt.setInt(11, po.getMaxOccurs());
			} else {
				pstmt.setNull(11,Types.INTEGER);
			}
			if(po.getMinOccurs()!=null) {
				pstmt.setInt(12, po.getMinOccurs());
			} else {
				pstmt.setNull(12,Types.INTEGER);
			}
			pstmt.setString(13, po.getMessageComponentId());
			pstmt.setString(14, po.getVersion());
			pstmt.setString(15, po.getIsMessageAssociationEnd());
			pstmt.setString(16, po.getIsDerived());
			pstmt.setString(17, po.getTraceType());
			pstmt.setString(18, po.getTrace());
			pstmt.setString(19, po.getTechnical());
			pstmt.setString(20, po.getTracesTo());
			pstmt.setString(21, po.getTypeOfTracesTo());
			pstmt.setString(22, po.getTracePath());
			pstmt.setString(23, po.getCreateUser());
			pstmt.setTimestamp(24, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(25, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveConstraint(List<EcoreConstraint> ecoreConstraints) throws Exception{
		String sql = "insert into ecore_constraint(id,obj_id,obj_type,expression_language,definition"
				+ ",name,registration_status,removal_date,object_identifier,create_user,create_time,is_from_iso20022,expression) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreConstraint po : ecoreConstraints) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getObj_type());
			pstmt.setString(4,po.getExpressionlanguage());
			if(po.getDefinition()!=null) {
				pstmt.setString(5, po.getDefinition());
			} else {
				pstmt.setString(5, "");
			}
			pstmt.setString(6, po.getName());
			pstmt.setString(7, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(8, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(8,Types.DATE);
			}
			pstmt.setString(9, po.getObjectIdentifier());
			pstmt.setString(10, po.getCreateUser());
			pstmt.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(12, po.getIsfromiso20022());
			if(po.getExpression()!=null) {
				pstmt.setString(13,po.getExpression());
			}else {
				pstmt.setString(13, "");
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveNextVersions(List<EcoreNextVersions> ecoreNextVersionss) throws Exception{
		String sql = "insert into ECORE_NEXT_VERSIONS(id,obj_type,next_version_id,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreNextVersions po : ecoreNextVersionss) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObjType());
			pstmt.setString(3, po.getNextVersionId());
			pstmt.setString(4, po.getCreateUser());
			pstmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(6, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	private void saveSemanticMarkupElement(List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements) throws SQLException {
		String sql = "insert into ECORE_SEMANTIC_MARKUP_ELEMENT(id,name,value,semantic_markup_id,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreSemanticMarkupElement po : ecoreSemanticMarkupElements) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getValue());
			pstmt.setString(4, po.getSemanticMarkupId());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveSemanticMarkup(List<EcoreSemanticMarkup> ecoreSemanticMarkups) throws SQLException{
		String sql = "insert into ecore_semantic_markup(id,obj_id,obj_type,type,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreSemanticMarkup po : ecoreSemanticMarkups) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObjId());
			pstmt.setString(3, po.getObjType());
			pstmt.setString(4, po.getType());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}
	
	/**
	 * 查询MessageComponentVO数据
	 * @param id
	 */
	public EcoreMessageComponentVO findMessageComponentVO(String id) throws Exception{
		EcoreMessageComponentVO vo=null;
		List<EcoreMessageElementVO> ecoreMessageElementVOs=new ArrayList<EcoreMessageElementVO>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		Map<String,String> idMap=new HashMap<String,String>();
		String presql="select id from ECORE_MESSAGE_ELEMENT where MESSAGE_COMPONENT_ID='"+id+"'";
		String mcsql = "select * FROM ECORE_MESSAGE_COMPONENT where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String dstrSql="select id,name from ecore_data_type";
		String estrSql="select id,name from ecore_external_schema";
		String mstrSql="select id,name from ecore_message_component";
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		st=conn.createStatement();
		rs=st.executeQuery(dstrSql);
		while(rs.next()) {
			idMap.put(rs.getString(1), rs.getString(2));
		}
		st=conn.createStatement();
		rs=st.executeQuery(estrSql);
		while(rs.next()) {
			idMap.put(rs.getString(1), rs.getString(2));
		}
		st=conn.createStatement();
		rs=st.executeQuery(mstrSql);
		while(rs.next()) {
			idMap.put(rs.getString(1), rs.getString(2));
		}
		pstmt=conn.prepareStatement(mcsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreMessageComponent me = new EcoreMessageComponent();
			vo=new EcoreMessageComponentVO();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setTrace(rs.getString("TRACE"));
			me.setRegistrationStatus(rs.getString("registration_status"));
			me.setRemovalDate(rs.getDate("removal_date"));
			me.setCreateTime(rs.getDate("create_time"));
			me.setCreateUser(rs.getString("create_user"));
			me.setUpdateTime(rs.getDate("update_time"));
			me.setUpdateUser(rs.getString("update_user"));
			me.setPreviousVersion(rs.getString("previous_version"));
			me.setIsfromiso20022(rs.getString("is_from_iso20022"));
			me.setVersion(rs.getString("version"));
			me.setComponentType(rs.getString("component_type"));
			me.setTrace(rs.getString("trace"));
			me.setTechnical(rs.getString("technical"));
			me.setTraceName(rs.getString("trace_name"));
			vo.setEcoreMessageComponent(me);
			break;
		}
		if(vo!=null) {
        	pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint ec=new EcoreConstraint();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpressionlanguage(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistrationStatus(rs.getString("registration_status"));
				ec.setRemovalDate(rs.getDate("removal_date"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreConstraints.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExample ec=new EcoreExample();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreExamples.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersions ec=new EcoreNextVersions();
				ec.setId(rs.getString("ID"));
				ec.setObjType(rs.getString("obj_type"));
				ec.setNextVersionId(rs.getString("next_version_id"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreNextVersions.add(ec);
			}
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
				vo.setEcoreNextVersions(ecoreNextVersions);
			}
			//elment
			st=conn.createStatement();
			rs=st.executeQuery(presql);
			List<String> ids=new ArrayList<String>();
			while(rs.next()) {
	        	String mbId=rs.getString(1);
	        	ids.add(mbId);
	        }
			if(ids!=null && !ids.isEmpty()) {
				for(String eid:ids) {
		        	EcoreMessageElementVO evo=findEcoreMessageElementVO(eid,idMap);
		        	if(evo!=null) {
		            	ecoreMessageElementVOs.add(evo);
		        	}
				}
				vo.setEcoreMessageElementVOs(ecoreMessageElementVOs);
			}
		}
		conn.commit();
		return vo;
	}
	
	private EcoreMessageElementVO findEcoreMessageElementVO(String id, Map<String, String> idMap) throws SQLException{
		EcoreMessageElementVO vo=null;
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		List<SynonymVO> svos=new ArrayList<SynonymVO>();
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String mesql="select * from ECORE_MESSAGE_ELEMENT where id=?";
		String mkesql="select * from ecore_semantic_markup_element where semantic_markup_id"
				+ " in (select id from ecore_semantic_markup where obj_id=? and type='Synonym') ";

		pstmt=conn.prepareStatement(mesql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			EcoreMessageElement me = new EcoreMessageElement();
			vo=new EcoreMessageElementVO();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setType(rs.getString("TYPE"));
			me.setTypeId(rs.getString("TYPE_ID"));
			me.setMinOccurs(rs.getInt("MIN_OCCURS"));
			me.setMaxOccurs(rs.getInt("MAX_OCCURS"));
			me.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setXmlTag(rs.getString("XML_TAG"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setTraceType(rs.getString("TRACE_TYPE"));
			me.setTrace(rs.getString("TRACE"));
			me.setRegistrationStatus(rs.getString("registration_status"));
			me.setRemovalDate(rs.getDate("removal_date"));
			me.setCreateTime(rs.getDate("create_time"));
			me.setCreateUser(rs.getString("create_user"));
			me.setUpdateTime(rs.getDate("update_time"));
			me.setUpdateUser(rs.getString("update_user"));
			me.setMessageComponentId(id);
			me.setIsDerived(rs.getString("is_derived"));
			me.setIsMessageAssociationEnd(rs.getString("is_Message_Association_End"));
			me.setPreviousVersion(rs.getString("previous_version"));
			me.setIsfromiso20022(rs.getString("is_from_iso20022"));
			me.setVersion(rs.getString("version"));
			me.setTechnical(rs.getString("technical"));
			me.setTracesTo(rs.getString("traces_to"));
			me.setTypeOfTracesTo(rs.getString("type_of_traces_to"));
			me.setTracePath(rs.getString("trace_path"));
			if(me.getTypeId()!=null) {
				me.setTypeName(idMap.get(me.getTypeId()));
			}
			vo.setEcoreMessageElement(me);
			break;
        }
		if(vo!=null) {
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraint ec=new EcoreConstraint();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObjectIdentifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpressionlanguage(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistrationStatus(rs.getString("registration_status"));
				ec.setRemovalDate(rs.getDate("removal_date"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreConstraints.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExample ec=new EcoreExample();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreExamples.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersions ec=new EcoreNextVersions();
				ec.setId(rs.getString("ID"));
				ec.setObjType(rs.getString("obj_type"));
				ec.setNextVersionId(rs.getString("next_version_id"));
				ec.setCreateTime(rs.getDate("create_time"));
				ec.setCreateUser(rs.getString("create_user"));
				ec.setUpdateTime(rs.getDate("update_time"));
				ec.setUpdateUser(rs.getString("update_user"));
				ec.setIsfromiso20022(rs.getString("is_from_iso20022"));
				ecoreNextVersions.add(ec);
			}
			pstmt=conn.prepareStatement(mkesql);
			pstmt.setString(1, id);
			//获取mke数据
			rs=pstmt.executeQuery();
			HashMap<String,Map<String, String>> mkeMap=new HashMap<String,Map<String, String>>();
			while(rs.next()) {
				String semantic_markup_id=rs.getString("semantic_markup_id");
				String name=rs.getString("name");
				String value=rs.getString("value");
				if(mkeMap.containsKey(semantic_markup_id)) {
					Map<String, String> mp=mkeMap.get(semantic_markup_id);
					if(mp!=null && !mp.isEmpty()) {
						mp.put(name, value);
					}
				}else {
					Map<String, String> mp=new HashMap<String, String>();
					mp.put(name, value);
					mkeMap.put(semantic_markup_id, mp);
				}
			}
			if(ecoreExamples!=null && !ecoreExamples.isEmpty()) {
				vo.setEcoreExamples(ecoreExamples);
			}
			if(ecoreConstraints!=null && !ecoreConstraints.isEmpty()) {
				vo.setEcoreConstraints(ecoreConstraints);
			}
			if(ecoreNextVersions!=null && !ecoreNextVersions.isEmpty()) {
				vo.setEcoreNextVersions(ecoreNextVersions);
			}
			if(mkeMap!=null  && !mkeMap.isEmpty()) {
				for(String eid:mkeMap.keySet()) {
					Map<String, String> mt=mkeMap.get(eid);
					SynonymVO v=new SynonymVO();
					v.setObjId(id);
					v.setContext(mt.get("context"));
					v.setSynonym(mt.get("value"));
					svos.add(v);
				}
				vo.setSynonyms(svos);
			}
		}
		return vo;
	}
	
	/**
	 * 查询所有mc的名称
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> findAllMessageComponentName() throws Exception{
		Map<String,String> map=new HashMap<String, String>();
		String bsql = "SELECT T.ID,T.NAME,T.COMPONENT_TYPE FROM ECORE_MESSAGE_COMPONENT T";
		conn = DerbyUtil.getConnection(); 
		st=conn.createStatement();
		rs=st.executeQuery(bsql);
		while(rs.next()) {
        	map.put(rs.getString(1), rs.getString(2));
		}
		conn.commit();
		return map;
	}
	
	
	/**
	 * 获取model信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<EcoreMessageComponentTracesVO> findMessageComponentTrace(String id) throws Exception{
		List<EcoreMessageComponentTracesVO> alllist=new ArrayList<EcoreMessageComponentTracesVO>();
		mcSets = new HashSet<String>();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		List<EcoreMessageComponentTracesVO> clist=new ArrayList<EcoreMessageComponentTracesVO>();
		Map<String, String> names = new HashMap<String, String>();
		Map<String, String> bids = new HashMap<String, String>();
		Map<String,List<EcoreMessageComponentTracesVO>> allelemntList =
				new HashMap<String,List<EcoreMessageComponentTracesVO>>();
		String sql="select distinct t.* " + 
				"  from (SELECT MB.DATA_TYPE_ID" + 
				"          FROM ECORE_MESSAGE_DEFINITION       MD," + 
				"               ECORE_MESSAGE_SET_DEFINTION_RL MR," + 
				"               ECORE_MESSAGE_BUILDING_BLOCK   MB" + 
				"         WHERE MD.ID = MR.MESSAGE_ID" + 
				"           AND MR.SET_ID = ? " + 
				"           AND MD.ID = MB.MESSAGE_ID" + 
				"           AND MB.DATA_TYPE = '2'" + 
				"         ORDER BY MD.NAME) t";
		String csql="select t.id,t.name from ecore_business_component t";
		String msql="select t.id,t.name,t.business_component_id from ecore_business_element t";
		pstmt=conn.prepareStatement(csql);
		rs=pstmt.executeQuery();
		while(rs.next()) {
			names.put(rs.getString(1), rs.getString(2));
		}
		pstmt=conn.prepareStatement(msql);
		rs=pstmt.executeQuery();
		while(rs.next()) {
			names.put(rs.getString(1), rs.getString(2));
			bids.put(rs.getString(1), rs.getString(3));
		}
		pstmt=conn.prepareStatement(sql);
		pstmt.setString(1, id);
		//获取block对应的MC的数据
		ResultSet mblockrs=pstmt.executeQuery();
		while(mblockrs.next()) {
			String mid=mblockrs.getString(1);
			if(!mcSets.contains(mid)) {
				mcSets.add(mid);
				findMessageComponentElement(mid,clist,allelemntList,names,bids);
			}
		}
		//排序
		if(clist!=null && !clist.isEmpty()) {
			clist.sort(new Comparator<EcoreMessageComponentTracesVO>() {

				@Override
				public int compare(EcoreMessageComponentTracesVO o1, EcoreMessageComponentTracesVO o2) {
					return o1.getMessageComponentName().compareTo(o2.getMessageComponentName());
				}
				
			});
			//获取MC下面的ME
			for(EcoreMessageComponentTracesVO v:clist) {
				alllist.add(v);
				String tempid=v.getcId();
				List<EcoreMessageComponentTracesVO> l=allelemntList.get(tempid);
				if(l!=null && !l.isEmpty()) {
					alllist.addAll(l);
				}
			}
		}
		conn.commit();
		return alllist;
	}
	
	/**
	 * 获取消息组件下面的元素
	 * @param id
	 * @param clist
	 * @param allelemntList
	 * @param names
	 * @param bids
	 * @throws Exception
	 */
	private void findMessageComponentElement(String id,List<EcoreMessageComponentTracesVO> clist
			,Map<String,List<EcoreMessageComponentTracesVO>> allelemntList,Map<String, String> names
			,Map<String, String> bids) throws Exception {
		EcoreMessageComponentTracesVO comp=new EcoreMessageComponentTracesVO();
		List<EcoreMessageComponentTracesVO> elemntList = new ArrayList<EcoreMessageComponentTracesVO>();
		String cname="";
		String isT="";
		String pcsql="SELECT T.NAME, T.TECHNICAL, T.TRACE FROM ECORE_MESSAGE_COMPONENT T where id='"+id+"'";
		String presql="select t.name,t.type_of_traces_to,t.technical,t.traces_to,t.trace_type,t.trace,t.trace_path,"
				+ "t.type,t.type_id from ECORE_MESSAGE_ELEMENT t where MESSAGE_COMPONENT_ID='"+id+"' order by t.name ";
		pstmt=conn.prepareStatement(pcsql);
		rs=pstmt.executeQuery();
		while(rs.next()) {
			cname=rs.getString(1);
			isT=rs.getString(2);
			comp.setMessageComponentName(cname);
			comp.setTechnical(isT);
			if(rs.getString(3)!=null) {
				comp.setTraceToBusinessComponent(names.get(rs.getString(3)));
			}
			comp.setcId(id);
			clist.add(comp);
		}
		//elment
		st=conn.createStatement();
		rs=st.executeQuery(presql);
		while(rs.next()) {
			EcoreMessageComponentTracesVO elemnt=new EcoreMessageComponentTracesVO();
			elemnt.setMessageComponentName(cname);
			elemnt.setMessageElementName(rs.getString(1));
			elemnt.setTechnical(rs.getString(3));
			elemnt.setTracePath(rs.getString(7));
			elemnt.setTraceToElement(rs.getString(4));
			elemnt.setTypeTracTo(rs.getString(2));
			String tp=rs.getString(8);
			if("2".equals(tp)) {
				elemnt.setcId(rs.getString(9));
			}
			if("1".equals(rs.getString(5))) {
				String cpid=bids.get(rs.getString(6));
				elemnt.setTraceToBusinessComponent(names.get(cpid));
			}else {
				if(rs.getString(6)!=null) {
					elemnt.setTraceToBusinessComponent(names.get(rs.getString(6)));
				}
			}
			elemntList.add(elemnt);
        }
		//如果elment下面还有MC
		if(elemntList!=null && !elemntList.isEmpty()) {
			allelemntList.put(id, elemntList);
			if("false".equals(isT)) {
				for(EcoreMessageComponentTracesVO v:elemntList) {
					if("false".equals(v.getTechnical())) {
						if(v.getcId()!=null && !mcSets.contains(v.getcId())) {
							mcSets.add(v.getcId());
							findMessageComponentElement(v.getcId(), clist, allelemntList,names,bids);
						}
					}
				}
			}
		}
	}
}
