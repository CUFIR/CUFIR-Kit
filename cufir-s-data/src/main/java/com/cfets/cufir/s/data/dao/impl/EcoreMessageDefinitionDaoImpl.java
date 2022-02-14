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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreBusinessArea;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageBuildingBlock;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkup;
import com.cfets.cufir.s.data.bean.EcoreSemanticMarkupElement;
import com.cfets.cufir.s.data.dao.EcoreMessageDefinitionDao;
import com.cfets.cufir.s.data.util.DerbyUtil;
import com.cfets.cufir.s.data.util.ListUtil;
import com.cfets.cufir.s.data.vo.EcoreDocumentTreeNode;
import com.cfets.cufir.s.data.vo.EcoreMessageBuildingBlockVO;
import com.cfets.cufir.s.data.vo.EcoreMessageDefinitionVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;
import com.cfets.cufir.s.data.vo.RemovedObjectVO;
import com.cfets.cufir.s.data.vo.SynonymVO;
import com.cfets.cufir.s.data.vo.web.EcoreBusinessAreaVo;
import com.cfets.cufir.s.data.vo.web.EcoreCodeVo;
import com.cfets.cufir.s.data.vo.web.EcoreConstraintVo;
import com.cfets.cufir.s.data.vo.web.EcoreDataTypeVo;
import com.cfets.cufir.s.data.vo.web.EcoreExampleVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageBuildingBlockVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageComponentVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageDefinitionVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageElementVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageSetDefinitionRLVo;
import com.cfets.cufir.s.data.vo.web.EcoreMessageSetVo;
import com.cfets.cufir.s.data.vo.web.EcoreNextVersionsVo;
import com.cfets.cufir.s.data.vo.web.EcoreSemanticMarkupElementVo;
import com.cfets.cufir.s.data.vo.web.EcoreSemanticMarkupVo;
import com.cfets.cufir.s.data.vo.web.RemovedObjectVo;

/**
 * EcoreMessageDefinition数据库操作
 * @author zqh
 *
 */
public class EcoreMessageDefinitionDaoImpl implements EcoreMessageDefinitionDao{

	private static Logger logger = Logger.getLogger(EcoreMessageDefinitionDaoImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement st=null;
	private ResultSet rs = null;
	
	/**
	 * 获取MessageNodes
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findEcoreMessageTreeNodes() throws Exception{
		List<EcoreTreeNode> messageTreeNodes=new ArrayList<EcoreTreeNode>();
		EcoreTreeNode businessTreeNode=new EcoreTreeNode();
		EcoreTreeNode setTreeNode=new EcoreTreeNode();
		EcoreTreeNode definitionTreeNode=new EcoreTreeNode();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String bsql = "select B.ID, B.NAME,b.registration_status from ECORE_BUSINESS_AREA B ORDER BY B.NAME";
		String msql = "select M.ID, M.NAME,M.BUSINESS_AREA_ID,m.registration_status from ECORE_MESSAGE_DEFINITION M ORDER BY M.NAME";
		String mbsql = "select M.ID, M.NAME,M.MESSAGE_ID,M.DATA_TYPE,m.registration_status from ECORE_MESSAGE_BUILDING_BLOCK M ORDER BY M.NAME";
		String ssql = "select T.ID,T.NAME,t.registration_status from ECORE_MESSAGE_SET t ORDER BY T.NAME";
		String rsql = "select t.message_id,t.set_id from ECORE_MESSAGE_SET_DEFINTION_RL t";
		Map<String,List<EcoreTreeNode>> businessMaps=new HashMap<String,List<EcoreTreeNode>>();
		Map<String,List<EcoreTreeNode>> blockMaps=new HashMap<String,List<EcoreTreeNode>>();
		Map<String,List<EcoreTreeNode>> setMaps=new HashMap<String,List<EcoreTreeNode>>();
		List<EcoreTreeNode> businessAreas=new ArrayList<EcoreTreeNode>();
		List<EcoreTreeNode> messageSets=new ArrayList<EcoreTreeNode>();
		List<EcoreTreeNode> messageDefinitions=new ArrayList<EcoreTreeNode>();
		Map<String,EcoreTreeNode> messageMaps=new HashMap<String,EcoreTreeNode>();
		try {
			st=conn.createStatement();
			//获取block数据
			rs=st.executeQuery(mbsql);
			while(rs.next()) {
            	String mbId=rs.getString(1);
            	String mbName=rs.getString(2);
            	String mId=rs.getString(3);
            	String mbType=rs.getString(4);
            	List<EcoreTreeNode> blockList= blockMaps.get(mId);
            	if(blockList == null) {
            		blockList=new ArrayList<EcoreTreeNode>();
            		blockMaps.put(mId, blockList);
            	}
            	EcoreTreeNode node=new EcoreTreeNode();
            	node.setId(mbId);
            	node.setName(mbName);
            	node.setType(mbType);
            	node.setLevel("4");
            	node.setRegistrationStatus(rs.getString("registration_status"));
            	blockList.add(node);
            }
			//获取businessMessage数据
			rs=st.executeQuery(msql);
			while(rs.next()) {
            	String mId=rs.getString(1);
            	String mName=rs.getString(2);
            	String bId=rs.getString(3);
            	List<EcoreTreeNode> messageList=businessMaps.get(bId);
            	if(messageList == null) {
            		messageList=new ArrayList<EcoreTreeNode>();
            		businessMaps.put(bId, messageList);
            	}
            	EcoreTreeNode node=new EcoreTreeNode();
            	node.setId(mId);
            	node.setName(mName);
            	node.setLevel("3");
            	node.setRegistrationStatus(rs.getString("registration_status"));
            	node.setChildNodes(blockMaps.get(mId));
            	messageList.add(node);
            	messageDefinitions.add(node);
            	messageMaps.put(mId, node);
            }
			//获取setMessage数据
			rs=st.executeQuery(rsql);
			while(rs.next()) {
            	String mId=rs.getString(1);
            	String sId=rs.getString(2);
            	List<EcoreTreeNode> messageList=setMaps.get(sId);
            	if(messageList == null) {
            		messageList=new ArrayList<EcoreTreeNode>();
            		setMaps.put(sId, messageList);
            	}
            	EcoreTreeNode messageNode=messageMaps.get(mId);
            	messageList.add(messageNode);
            }
			//获取area数据
			rs=st.executeQuery(bsql);
			while(rs.next()) {
            	String bId=rs.getString(1);
            	String bName=rs.getString(2);
            	EcoreTreeNode node=new EcoreTreeNode();
            	node.setId(bId);
            	node.setName(bName);
            	node.setRegistrationStatus(rs.getString("registration_status"));
            	node.setChildNodes(businessMaps.get(bId));
            	node.setLevel("2");
            	businessAreas.add(node);
            }
			//获取set数据
			rs=st.executeQuery(ssql);
			while(rs.next()) {
            	String sId=rs.getString(1);
            	String sName=rs.getString(2);
            	EcoreTreeNode node=new EcoreTreeNode();
            	node.setId(sId);
            	node.setName(sName);
            	node.setChildNodes(setMaps.get(sId));
            	node.setLevel("2");
            	node.setRegistrationStatus(rs.getString("registration_status"));
            	messageSets.add(node);
            }
			//business顶级
			businessTreeNode.setName("Business Areas");
			businessTreeNode.setLevel("1");
			businessTreeNode.setType("5");
			businessTreeNode.setChildNodes(businessAreas);
			//set顶级
			setTreeNode.setName("Message Sets");
			setTreeNode.setLevel("1");
			setTreeNode.setType("6");
			setTreeNode.setChildNodes(messageSets);
			//definition顶级
			definitionTreeNode.setName("Message Definitions");
			definitionTreeNode.setLevel("1");
			definitionTreeNode.setType("7");
			List<EcoreTreeNode> copyMessageDefinitions= ListUtil.deepCopy(messageDefinitions);
			for(EcoreTreeNode n:copyMessageDefinitions) {
				n.setLevel("2");
				if(n.getChildNodes()!=null && !n.getChildNodes().isEmpty()) {
					for(EcoreTreeNode nb:n.getChildNodes()) {
						nb.setLevel("3");
					}
				}
			}
			definitionTreeNode.setChildNodes(copyMessageDefinitions);
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		messageTreeNodes.add(businessTreeNode);
		messageTreeNodes.add(setTreeNode);
		messageTreeNodes.add(definitionTreeNode);
		return messageTreeNodes;
	}
	
	@Override
	public void addEcoreMessageDefinitionList(List<EcoreMessageDefinition> ecoreMessageDefinitions) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_MESSAGE_DEFINITION(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,xml_Tag,xml_name,root_element,flavour"
					+ ",message_functionality,business_area_id,business_area,version,message_definition_identifier,"
					+ "create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreMessageDefinition po : ecoreMessageDefinitions) {
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
				pstmt.setString(9, po.getXmlName());
				pstmt.setString(10, po.getRootElement());
				pstmt.setString(11, po.getFlavour());
				pstmt.setString(12, po.getMessageFunctionality());
				pstmt.setString(13, po.getBusinessAreaId());
				pstmt.setString(14, po.getBusinessArea());
				String messageDefinitionIdentifier=po.getBusinessArea()+"."
						+po.getMessageFunctionality()+"."+po.getFlavour()+"."+po.getVersion();
				pstmt.setString(15, po.getVersion());
				pstmt.setString(16, messageDefinitionIdentifier);
				pstmt.setString(17, po.getCreateUser());
				pstmt.setTimestamp(18, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(19, po.getIsfromiso20022());
				pstmt.addBatch();
			}
			pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
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
			pstmt.setString(2, "11");
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
	 * 删除消息定义
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreMessageDefinition(String id) throws Exception{
		boolean key=false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			String presql="select id from ECORE_MESSAGE_BUILDING_BLOCK where message_id='"+id+"'";
			String sql = "delete from ECORE_MESSAGE_DEFINITION where id=?" ;
			String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
			String dsql = "delete from ECORE_DOCLET where obj_id=?";
			String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
			String vsql = "delete from ECORE_NEXT_VERSIONS where id=?";
			String rsql="delete from ECORE_MESSAGE_SET_DEFINTION_RL where MESSAGE_ID=?";
			String mbsmsql = "delete from ecore_semantic_markup  where obj_id =?";
			String mbsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
					+ " (select id from ecore_semantic_markup where obj_id =?)";
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
				String esql = "delete from ECORE_MESSAGE_BUILDING_BLOCK where id in ("+mbIds+")";
				//common
				String bsmsql = "delete from ecore_semantic_markup  where obj_id in ("+mbIds+")";
				String bsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
						+ " (select id from ecore_semantic_markup where obj_id in ("+mbIds+"))";
				csql+=" or obj_id in ("+mbIds+")";
				dsql+=" or obj_id in ("+mbIds+")";
				eeql+=" or obj_id in ("+mbIds+")";
				
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
			
			pstmt = conn.prepareStatement(mbsemsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(mbsmsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(rsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			pstmt.executeUpdate();
			
			saveRemovedInfo(id);
			
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
	 * 保存MessageDefinition数据
	 * @param ecoreMessageDefinitionVOs
	 */
	public boolean saveMessageDefinitionVOList(List<EcoreMessageDefinitionVO> messageDefinitionVOs) throws Exception{
		boolean key=false;
		List<EcoreMessageDefinition> ecoreMessageDefinitions=new ArrayList<EcoreMessageDefinition>();
		List<EcoreConstraint> ecoreConstraints=new ArrayList<EcoreConstraint>();
		List<EcoreMessageBuildingBlock> ecoreBlocks=new ArrayList<EcoreMessageBuildingBlock>();
		List<EcoreExample> ecoreExamples=new ArrayList<EcoreExample>();
		List<EcoreNextVersions> ecoreNextVersions=new ArrayList<EcoreNextVersions>();
		List<String> dataIds=new ArrayList<String>();
		List<EcoreBusinessArea> ecoreBusinessAreas=new ArrayList<EcoreBusinessArea>();
		List<EcoreSemanticMarkup> ecoreSemanticMarkups=new ArrayList<EcoreSemanticMarkup>();
		List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements=new ArrayList<EcoreSemanticMarkupElement>();
		List<RemovedObjectVO> removeds=new ArrayList<RemovedObjectVO>();
		//解析保存的内容
		for(EcoreMessageDefinitionVO vo: messageDefinitionVOs) {
			EcoreMessageDefinition ecoreMessageDefinition=vo.getEcoreMessageDefinition();
			List<EcoreConstraint> constraints=vo.getEcoreConstraints();
			List<EcoreExample> examples=vo.getEcoreExamples();
			List<EcoreNextVersions> nextVersions=vo.getEcoreNextVersions();
			List<EcoreMessageBuildingBlockVO> eVOs=vo.getEcoreMessageBuildingBlockVOs();
			List<SynonymVO> ssv=vo.getSynonyms();
			List<RemovedObjectVO> rmds=vo.getRemovedObjectVOs();
			if(constraints!=null && !constraints.isEmpty()) {
				ecoreConstraints.addAll(constraints);
			}
			if(examples!=null && !examples.isEmpty()) {
				ecoreExamples.addAll(examples);
			}
			if(nextVersions!=null && !nextVersions.isEmpty()) {
				ecoreNextVersions.addAll(nextVersions);
			}
			if(ssv!=null && !ssv.isEmpty()) {
				for(SynonymVO sv:ssv) {
					String muId=UUID.randomUUID().toString();
					EcoreSemanticMarkup up=new EcoreSemanticMarkup();
					up.setObjId(sv.getObjId());
					up.setType("Synonym");
					up.setIsfromiso20022(ecoreMessageDefinition.getIsfromiso20022());
					up.setObjType("11");
					up.setId(muId);
					
					String muIdC=UUID.randomUUID().toString();
					EcoreSemanticMarkupElement smec=new EcoreSemanticMarkupElement();
					smec.setId(muIdC);
					smec.setIsfromiso20022(ecoreMessageDefinition.getIsfromiso20022());
					smec.setName("context");
					smec.setValue(sv.getContext());
					smec.setSemanticMarkupId(muId);

					String muIdS=UUID.randomUUID().toString();
					EcoreSemanticMarkupElement smes=new EcoreSemanticMarkupElement();
					smes.setId(muIdS);
					smes.setIsfromiso20022(ecoreMessageDefinition.getIsfromiso20022());
					smes.setName("value");
					smes.setValue(sv.getSynonym());
					smes.setSemanticMarkupId(muId);
					ecoreSemanticMarkups.add(up);
					ecoreSemanticMarkupElements.add(smes);
					ecoreSemanticMarkupElements.add(smec);
				}
			}
			//解析CODE
			if(eVOs!=null && !eVOs.isEmpty()) {
				for(EcoreMessageBuildingBlockVO cvo:eVOs) {
					EcoreMessageBuildingBlock ecoreMessageBuildingBlock=cvo.getEcoreMessageBuildingBlock();
					List<EcoreConstraint> cconstraints=cvo.getEcoreConstraints();
					List<EcoreExample> cexamples=cvo.getEcoreExamples();
					List<EcoreNextVersions> cnextVersions=cvo.getEcoreNextVersions();
					List<SynonymVO> ss=cvo.getSynonyms();
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
							up.setIsfromiso20022(ecoreMessageBuildingBlock.getIsfromiso20022());
							up.setObjType("12");
							up.setId(muId);
							
							String muIdC=UUID.randomUUID().toString();
							EcoreSemanticMarkupElement smec=new EcoreSemanticMarkupElement();
							smec.setId(muIdC);
							smec.setIsfromiso20022(ecoreMessageBuildingBlock.getIsfromiso20022());
							smec.setName("context");
							smec.setValue(sv.getContext());
							smec.setSemanticMarkupId(muId);

							String muIdS=UUID.randomUUID().toString();
							EcoreSemanticMarkupElement smes=new EcoreSemanticMarkupElement();
							smes.setId(muIdS);
							smes.setIsfromiso20022(ecoreMessageBuildingBlock.getIsfromiso20022());
							smes.setName("value");
							smes.setValue(sv.getSynonym());
							smes.setSemanticMarkupId(muId);
							ecoreSemanticMarkups.add(up);
							ecoreSemanticMarkupElements.add(smes);
							ecoreSemanticMarkupElements.add(smec);
						}
					}
					ecoreBlocks.add(ecoreMessageBuildingBlock);
				}
			}
			if(rmds!=null && !rmds.isEmpty()) {
				removeds.addAll(rmds);
			}
			dataIds.add(ecoreMessageDefinition.getId());
			ecoreMessageDefinitions.add(ecoreMessageDefinition);
			if(vo.getEcoreBusinessArea()!=null) {
				ecoreBusinessAreas.add(vo.getEcoreBusinessArea());
			}
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if(dataIds!=null && !dataIds.isEmpty()) {
				for(String id:dataIds) {
					//先删除数据类型下面的所有数据
					deleteMessageDefinitionByIds(id);
				}
			}
			//删除已有的
			if(removeds!=null && !removeds.isEmpty()) {
				saveRemovedInfo(removeds);
			}
			//开始保存
			if(ecoreMessageDefinitions!=null && !ecoreMessageDefinitions.isEmpty()) {
				saveMessageDefinition(ecoreMessageDefinitions);
				if(ecoreBusinessAreas!=null && !ecoreBusinessAreas.isEmpty()) {
					saveBusinessArea(ecoreBusinessAreas);
				}
				if(ecoreBlocks!=null && !ecoreBlocks.isEmpty()) {
					saveMessageBuildingBlock(ecoreBlocks);
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
	
	private void saveBusinessArea(List<EcoreBusinessArea> ecoreBusinessAreas) throws Exception{
		List<EcoreBusinessArea> addEcoreBusinessAreas=new ArrayList<EcoreBusinessArea>();
		List<EcoreBusinessArea> updateEcoreBusinessAreas=new ArrayList<EcoreBusinessArea>();
		for(EcoreBusinessArea po : ecoreBusinessAreas) {
			String csql = "SELECT T.ID FROM ECORE_BUSINESS_AREA T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreBusinessAreas.add(po);
			}else {
				addEcoreBusinessAreas.add(po);
			}
		}
		String sql = "insert into ecore_business_area(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,code,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreBusinessArea po : addEcoreBusinessAreas) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getCode());
			pstmt.setString(8, po.getCreateUser());
			pstmt.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(10, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		
		String usql = "update ecore_business_area set update_time=?,name=?,definition=?,registration_status=?,removal_date=?,"
				+ "object_identifier=?,code=?, update_user=?,is_from_iso20022=? where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreBusinessArea po : updateEcoreBusinessAreas) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if(po.getRemovalDate()!=null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getCode());
			pstmt.setString(8, po.getUpdateUser());
			pstmt.setString(9, po.getIsfromiso20022());
			pstmt.setString(10, po.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void deleteMessageDefinitionByIds(String id) throws Exception{
		String presql="select id from ECORE_MESSAGE_BUILDING_BLOCK where message_id='"+id+"'";
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
		String dsql = "delete from ECORE_DOCLET where obj_id=?";
		String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
		String vsql = "delete from ECORE_NEXT_VERSIONS where id=?";
		String mbsmsql = "delete from ecore_semantic_markup  where obj_id =?";
		String mbsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
				+ " (select id from ecore_semantic_markup where obj_id =?)";
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
			String esql = "delete from ECORE_MESSAGE_BUILDING_BLOCK where id in ("+mbIds+")";
			//common
			String bsmsql = "delete from ecore_semantic_markup  where obj_id in ("+mbIds+")";
			String bsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
					+ " (select id from ecore_semantic_markup where obj_id in ("+mbIds+"))";
			csql+=" or obj_id in ("+mbIds+")";
			dsql+=" or obj_id in ("+mbIds+")";
			eeql+=" or obj_id in ("+mbIds+")";
			
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
		
		pstmt = conn.prepareStatement(mbsemsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(mbsmsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(vsql);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
	}
	
	private void saveMessageDefinition(List<EcoreMessageDefinition> ecoreMessageDefinitions) throws Exception{
		List<EcoreMessageDefinition> addEcoreMessageDefinitions=new ArrayList<EcoreMessageDefinition>();
		List<EcoreMessageDefinition> updateEcoreMessageDefinitions=new ArrayList<EcoreMessageDefinition>();
		for(EcoreMessageDefinition po : ecoreMessageDefinitions) {
			String csql = "SELECT T.ID FROM ECORE_MESSAGE_DEFINITION T WHERE id=?";
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs=pstmt.executeQuery();
			if(rs.next()) {
				updateEcoreMessageDefinitions.add(po);
			}else {
				addEcoreMessageDefinitions.add(po);
			}
		}
		String sql = "insert into ECORE_MESSAGE_DEFINITION(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,xml_Tag,xml_name,root_element,flavour"
				+ ",message_functionality,business_area_id,business_area,version,message_definition_identifier"
				+ ",create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreMessageDefinition po : addEcoreMessageDefinitions) {
			String messageDefinitionIdentifier=po.getMessageDefinitionIdentifier();
			if(messageDefinitionIdentifier!=null && !"".equals(messageDefinitionIdentifier)) {
				String[] str=messageDefinitionIdentifier.split("\\.");
				po.setBusinessArea(str[0]);
				po.setMessageFunctionality(str[1]);
				po.setFlavour(str[2]);
				po.setVersion(str[3]);
				po.setMessageDefinitionIdentifier(messageDefinitionIdentifier);
			}
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
			pstmt.setString(9, po.getXmlName());
			pstmt.setString(10, po.getRootElement());
			pstmt.setString(11, po.getFlavour());
			pstmt.setString(12, po.getMessageFunctionality());
			pstmt.setString(13, po.getBusinessAreaId());
			pstmt.setString(14, po.getBusinessArea());
			pstmt.setString(15, po.getVersion());
			pstmt.setString(16, po.getMessageDefinitionIdentifier());
			pstmt.setString(17, po.getCreateUser());
			pstmt.setTimestamp(18, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(19, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
		
		String usql = "update ECORE_MESSAGE_DEFINITION set update_time=?,name=?,definition=?,registration_status=?"
				+ ",removal_date=?,object_identifier=?,previous_version=?,xml_Tag=?,xml_name=?,root_element=?,flavour=?"
				+ ",message_functionality=?,business_area_id=?,business_area=?,version=?,message_definition_identifier=?,"
				+ "update_user=?,is_from_iso20022=?"
				+ "  where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreMessageDefinition po : updateEcoreMessageDefinitions) {
			String messageDefinitionIdentifier=po.getMessageDefinitionIdentifier();
			if(messageDefinitionIdentifier!=null && !"".equals(messageDefinitionIdentifier)) {
				String[] str=messageDefinitionIdentifier.split("\\.");
				po.setBusinessArea(str[0]);
				po.setMessageFunctionality(str[1]);
				po.setFlavour(str[2]);
				po.setVersion(str[3]);
				po.setMessageDefinitionIdentifier(messageDefinitionIdentifier);
			}
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
			pstmt.setString(8, po.getXmlTag());
			pstmt.setString(9, po.getXmlName());
			pstmt.setString(10, po.getRootElement());
			pstmt.setString(11, po.getFlavour());
			pstmt.setString(12, po.getMessageFunctionality());
			pstmt.setString(13, po.getBusinessAreaId());
			pstmt.setString(14, po.getBusinessArea());
			pstmt.setString(15, po.getVersion());
			pstmt.setString(16, po.getMessageDefinitionIdentifier());
			pstmt.setString(17, po.getUpdateUser());
			pstmt.setString(18, po.getIsfromiso20022());
			pstmt.setString(19, po.getId()); 
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
	
	private void saveMessageBuildingBlock(List<EcoreMessageBuildingBlock> ecoreBlocks) throws Exception{
		String sql = "insert into ECORE_MESSAGE_BUILDING_BLOCK(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,xml_Tag,data_type,data_type_id,max_occurs"
				+ ",min_occurs,message_id,version,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreMessageBuildingBlock po : ecoreBlocks) {
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
			pstmt.setString(9, po.getDataType());
			pstmt.setString(10, po.getDataTypeId());
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
			pstmt.setString(13, po.getMessageId());
			pstmt.setString(14, po.getVersion());
			pstmt.setString(15, po.getCreateUser());
			pstmt.setTimestamp(16, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(17, po.getIsfromiso20022());
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
	 * 按照id获取消息相关的数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<Object> findMessageDataById(String id) throws Exception{
		List<Object> objects=new ArrayList<Object>();
		Set<String> objectIds=new HashSet<String>();
		EcoreMessageDefinitionVo md = null;
		String baId="";
		List<EcoreMessageBuildingBlockVo> mbs=new ArrayList<EcoreMessageBuildingBlockVo>();
		String dsql="select * from ECORE_MESSAGE_DEFINITION where id=?";
		String bsql="select * from ECORE_MESSAGE_BUILDING_BLOCK where message_id=?";
		String esql="select * from ecore_example where obj_id=?";
		String csql="select * from ecore_constraint where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String basql="select * from ECORE_BUSINESS_AREA where id=?";
		String mssql="select * from ECORE_MESSAGE_SET  where id in"
				+ "(select set_id from ECORE_MESSAGE_SET_DEFINTION_RL  where message_id=?)";
		String mrlsql="select * from ECORE_MESSAGE_SET_DEFINTION_RL  where message_id=?";
		String mksql="select * from ecore_semantic_markup where obj_id=?";
		String mkesql="select * from ecore_semantic_markup_element where semantic_markup_id"
				+ " in (select id from ecore_semantic_markup where obj_id=?) ";
		String rmsql="select * from ecore_removed_info";
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		pstmt=conn.prepareStatement(dsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			md = new EcoreMessageDefinitionVo();
			md.setId(rs.getString("ID"));
			md.setName(rs.getString("NAME"));
			md.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
			md.setDefinition(rs.getString("DEFINITION"));
			md.setRegistration_status(rs.getString("registration_status"));
			md.setRemoval_date(rs.getDate("removal_date"));
			md.setCreate_time(rs.getDate("create_time"));
			md.setCreate_user(rs.getString("create_user"));
			md.setUpdate_time(rs.getDate("update_time"));
			md.setUpdate_user(rs.getString("update_user"));
			md.setIs_from_iso20022(rs.getString("is_from_iso20022"));
			md.setPrevious_version(rs.getString("previous_version"));
			md.setVersion(rs.getString("version"));
			md.setBusiness_area(rs.getString("business_Area"));
			md.setBusiness_area_id(rs.getString("business_Area_Id"));
			md.setFlavour(rs.getString("flavour"));
			md.setVersion(rs.getString("version"));
			md.setMessage_functionality(rs.getString("message_Functionality"));
			md.setXml_name(rs.getString("xml_Name"));
			md.setXml_tag(rs.getString("xml_Tag"));
			md.setRoot_element(rs.getString("root_Element"));
			md.setOwner(rs.getString("owner"));
			md.setMessage_definition_identifier(rs.getString("message_definition_identifier"));
			baId=md.getBusiness_area_id();
			objects.add(md);
			objectIds.add(md.getId());
			break;
		}
		if(md!=null) {
			pstmt=conn.prepareStatement(rmsql);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				RemovedObjectVo ec=new RemovedObjectVo();
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("OBJ_TYPE"));
				ec.setCreate_time(rs.getDate("create_time"));
				objects.add(ec);
			}
        	pstmt=conn.prepareStatement(bsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageBuildingBlockVo ec=new EcoreMessageBuildingBlockVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setData_type(rs.getString("data_Type"));
				ec.setData_type_id(rs.getString("data_Type_Id"));
				ec.setMax_occurs(rs.getInt("max_Occurs"));
				ec.setMessage_id(rs.getString("message_Id"));
				ec.setMin_occurs(rs.getInt("min_Occurs"));
				ec.setVersion(rs.getString("version"));
				ec.setXml_tag(rs.getString("xml_Tag"));
				ec.setOwner(rs.getString("owner"));
				ec.setXml_member_type(rs.getString("xml_member_type"));
				objects.add(ec);
				mbs.add(ec);
			}
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraintVo ec=new EcoreConstraintVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpression_language(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(esql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExampleVo ec=new EcoreExampleVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersionsVo ec=new EcoreNextVersionsVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setNext_version_id(rs.getString("next_version_id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			if(baId!=null) {
				pstmt=conn.prepareStatement(basql);
				pstmt.setString(1, baId);
				//获取block数据
				rs=pstmt.executeQuery();
				while(rs.next()) {
					EcoreBusinessAreaVo ec=new EcoreBusinessAreaVo();
					ec.setId(rs.getString("ID"));
					ec.setCode(rs.getString("code"));
					ec.setName(rs.getString("NAME"));
					ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
					ec.setDefinition(rs.getString("DEFINITION"));
					ec.setRegistration_status(rs.getString("registration_status"));
					ec.setRemoval_date(rs.getDate("removal_date"));
					ec.setCreate_time(rs.getDate("create_time"));
					ec.setCreate_user(rs.getString("create_user"));
					ec.setUpdate_time(rs.getDate("update_time"));
					ec.setUpdate_user(rs.getString("update_user"));
					ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
					ec.setOwner(rs.getString("owner"));
					objects.add(ec);
				}
			}
			pstmt=conn.prepareStatement(mrlsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageSetDefinitionRLVo ec=new EcoreMessageSetDefinitionRLVo();
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setSet_id(rs.getString("set_Id"));
				ec.setMessage_id(rs.getString("message_Id"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(mssql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreMessageSetVo ec=new EcoreMessageSetVo();
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setId(rs.getString("id"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(mksql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreSemanticMarkupVo ec=new EcoreSemanticMarkupVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setType(rs.getString("type"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setObj_id(rs.getString("obj_Id"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(mkesql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreSemanticMarkupElementVo ec=new EcoreSemanticMarkupElementVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("name"));
				ec.setValue(rs.getString("value"));
				ec.setSemantic_markup_id(rs.getString("semantic_Markup_Id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			for(EcoreMessageBuildingBlockVo b:mbs) {
				if(b.getData_type_id()!=null) {
					if("1".equals(b.getData_type())) {
						findDataType(b.getData_type_id(),objects,objectIds);
					}else {
						findMessageComponent(b.getData_type_id(), objects,objectIds);
					}
				}
			}
		}
		return objects;
	}
	
	/**
	 * 查询DataTypeVO数据
	 * @param id
	 */
	public void findDataType(String id,List<Object> objects, Set<String> objectIds) throws Exception{
		EcoreDataTypeVo dataType=null;
		Map<String,String> idMap=new HashMap<String,String>();
		String presql="select id from ecore_code where code_set_id='"+id+"'";
		String mcsql = "select * FROM ecore_data_type where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String dstrSql="select id,name from ecore_data_type";
		st=conn.createStatement();
		rs=st.executeQuery(dstrSql);
		while(rs.next()) {
			idMap.put(rs.getString(1), rs.getString(2));
		}
		pstmt=conn.prepareStatement(mcsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			dataType = new EcoreDataTypeVo();
			dataType.setRemoval_date(rs.getDate("removal_date"));
			dataType.setCreate_time(rs.getDate("create_time"));
			dataType.setCreate_user(rs.getString("create_user"));
			dataType.setUpdate_time(rs.getDate("update_time"));
			dataType.setUpdate_user(rs.getString("update_user"));
			dataType.setIs_from_iso20022(rs.getString("is_from_iso20022"));
			dataType.setPrevious_version(rs.getString("previous_version"));
			dataType.setVersion(rs.getString("version"));
			dataType.setId(rs.getString("ID"));
			dataType.setName(rs.getString("NAME"));
			dataType.setType(rs.getString("TYPE"));
			dataType.setDefinition(rs.getString("DEFINITION"));
			dataType.setNamespace(rs.getString("NAMESPACE"));
			dataType.setNamespace_list(rs.getString("NAMESPACE_LIST"));
			dataType.setProcess_contents(rs.getString("PROCESS_CONTENTS"));
			dataType.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
			dataType.setRegistration_status(rs.getString("REGISTRATION_STATUS"));
			dataType.setPrevious_version(rs.getString("PREVIOUS_VERSION"));
			dataType.setMin_length(rs.getInt("MIN_LENGTH"));
			dataType.setMax_length(rs.getInt("MAX_LENGTH"));
			dataType.setLength(rs.getInt("LENGTH"));
			dataType.setPattern(rs.getString("PATTERN"));
			dataType.setMeaning_when_false(rs.getString("MEANING_WHEN_FALSE"));
			dataType.setMeaning_when_true(rs.getString("MEANING_WHEN_TRUE"));
			dataType.setMin_inclusive(rs.getString("MIN_INCLUSIVE"));
			dataType.setMin_exclusive(rs.getString("MIN_EXCLUSIVE"));
			dataType.setMax_inclusive(rs.getString("MAX_INCLUSIVE"));
			dataType.setMax_exclusive(rs.getString("MAX_EXCLUSIVE"));
			dataType.setTotal_digits(rs.getInt("TOTAL_DIGITS"));
			dataType.setFraction_digits(rs.getInt("FRACTION_DIGITS"));
			dataType.setIdentification_scheme(rs.getString("IDENTIFICATION_SCHEME"));
			dataType.setBase_value(rs.getDouble("BASE_VALUE"));
			dataType.setBase_unit_code(rs.getString("BASE_UNIT_CODE"));
			dataType.setTrace(rs.getString("TRACE"));
			dataType.setOwner(rs.getString("owner"));
			objectIds.add(dataType.getId());
			objects.add(dataType);
			break;
		}
		if(dataType!=null) {
        	pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraintVo ec=new EcoreConstraintVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpression_language(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExampleVo ec=new EcoreExampleVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersionsVo ec=new EcoreNextVersionsVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setNext_version_id(rs.getString("next_version_id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
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
		        	findEcoreCode(eid,idMap,objects);
				}
			}
		}
	}
	
	private void findEcoreCode(String id, Map<String, String> idMap,List<Object> objects) throws SQLException{
		EcoreCodeVo me=null;
		String codeSql="select * from ECORE_CODE WHERE id=?";
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";

		pstmt=conn.prepareStatement(codeSql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			me = new EcoreCodeVo();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setRegistration_status(rs.getString("registration_status"));
			me.setRemoval_date(rs.getDate("removal_date"));
			me.setCreate_time(rs.getDate("create_time"));
			me.setCreate_user(rs.getString("create_user"));
			me.setUpdate_time(rs.getDate("update_time"));
			me.setUpdate_user(rs.getString("update_user"));
			me.setIs_from_iso20022(rs.getString("is_from_iso20022"));
			me.setCode_name(rs.getString("code_name"));
			me.setCode_set_id(rs.getString("code_set_id"));
			me.setOwner(rs.getString("owner"));
			objects.add(me);
			break;
        }
		if(me!=null) {
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraintVo ec=new EcoreConstraintVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpression_language(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExampleVo ec=new EcoreExampleVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersionsVo ec=new EcoreNextVersionsVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setNext_version_id(rs.getString("next_version_id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
		}
	}
	
	private void findMessageComponent(String id,List<Object> objects, Set<String> objectIds) throws Exception{
		EcoreMessageComponentVo me=null;
		Map<String,String> idMap=new HashMap<String,String>();
		List<EcoreMessageElementVo> melist=new ArrayList<EcoreMessageElementVo>();
		String presql="select id,type,type_id from ECORE_MESSAGE_ELEMENT where MESSAGE_COMPONENT_ID='"+id+"'";
		String mcsql = "select * FROM ECORE_MESSAGE_COMPONENT where id=?" ;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String dstrSql="select id,name from ecore_data_type";
		String estrSql="select id,name from ecore_external_schema";
		String mstrSql="select id,name from ecore_message_component";
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
			me = new EcoreMessageComponentVo();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setTrace(rs.getString("TRACE"));
			me.setRegistration_status(rs.getString("registration_status"));
			me.setRemoval_date(rs.getDate("removal_date"));
			me.setCreate_time(rs.getDate("create_time"));
			me.setCreate_user(rs.getString("create_user"));
			me.setUpdate_time(rs.getDate("update_time"));
			me.setUpdate_user(rs.getString("update_user"));
			me.setIs_from_iso20022(rs.getString("is_from_iso20022"));
			me.setPrevious_version(rs.getString("previous_version"));
			me.setVersion(rs.getString("version"));
			me.setComponent_type(rs.getString("component_type"));
			me.setTrace(rs.getString("trace"));
			me.setOwner(rs.getString("owner"));
			objectIds.add(me.getId());
			objects.add(me);
			break;
		}
		if(me!=null) {
        	pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraintVo ec=new EcoreConstraintVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpression_language(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExampleVo ec=new EcoreExampleVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersionsVo ec=new EcoreNextVersionsVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setNext_version_id(rs.getString("next_version_id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			//elment
			st=conn.createStatement();
			rs=st.executeQuery(presql);
			List<String> ids=new ArrayList<String>();
			while(rs.next()) {
				EcoreMessageElementVo el=new EcoreMessageElementVo();
	        	String mbId=rs.getString(1);
	        	el.setId(rs.getString(1));
	        	el.setType(rs.getString(2));
	        	el.setType_id(rs.getString(3));
	        	melist.add(el);
	        	ids.add(mbId);
	        }
			if(melist!=null && !melist.isEmpty()) {
				for(EcoreMessageElementVo eid:melist) {
		        	findEcoreMessageElement(eid.getId(),idMap,objects);
		        	if(eid.getType_id()!=null && !objectIds.contains(eid.getType_id())) {
						if("1".equals(eid.getType()) || "3".equals(eid.getType())) {
							findDataType(eid.getType_id(),objects,objectIds);
						}else if("2".equals(eid.getType())){
							findMessageComponent(eid.getType_id(), objects,objectIds);
						}else if("4".equals(eid.getType())){
							//TODO
						}
					}
				}
			}
		}
	}
	
	private void findEcoreMessageElement(String id, Map<String, String> idMap,List<Object> objects) throws SQLException{
		EcoreMessageElementVo me=null;
		String csql = "select * from ECORE_CONSTRAINT where obj_id=?";
		String eeql = "select * from ECORE_EXAMPLE where obj_id=?";
		String vsql="select * from ECORE_NEXT_VERSIONS where id=?";
		String mesql="select * from ECORE_MESSAGE_ELEMENT where id=?";
		String mksql="select * from ecore_semantic_markup where obj_id=?";
		String mkesql="select * from ecore_semantic_markup_element where semantic_markup_id"
				+ " in (select id from ecore_semantic_markup where obj_id=?) ";

		pstmt=conn.prepareStatement(mesql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			me = new EcoreMessageElementVo();
			me.setId(rs.getString("ID"));
			me.setName(rs.getString("NAME"));
			me.setType(rs.getString("TYPE"));
			me.setType_id(rs.getString("TYPE_ID"));
			me.setMin_occurs(rs.getInt("MIN_OCCURS"));
			me.setMax_occurs(rs.getInt("MAX_OCCURS"));
			me.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
			me.setXml_tag(rs.getString("XML_TAG"));
			me.setDefinition(rs.getString("DEFINITION"));
			me.setTrace_type(rs.getString("TRACE_TYPE"));
			me.setTrace(rs.getString("TRACE"));
			me.setRegistration_status(rs.getString("registration_status"));
			me.setRemoval_date(rs.getDate("removal_date"));
			me.setCreate_time(rs.getDate("create_time"));
			me.setCreate_user(rs.getString("create_user"));
			me.setUpdate_time(rs.getDate("update_time"));
			me.setUpdate_user(rs.getString("update_user"));
			me.setIs_from_iso20022(rs.getString("is_from_iso20022"));
			me.setMessage_component_id(rs.getString("message_component_id"));
			me.setIs_derived(rs.getString("is_derived"));
			me.setIs_message_association_end(rs.getString("is_Message_Association_End"));
			me.setPrevious_version(rs.getString("previous_version"));
			me.setVersion(rs.getString("version"));
			me.setOwner(rs.getString("owner"));
			me.setXml_member_type(rs.getString("xml_member_type"));
			if(me.getType_id()!=null) {
				me.setType_name(idMap.get(me.getType_id()));
			}
			objects.add(me);
			break;
        }
		if(me!=null) {
			pstmt=conn.prepareStatement(csql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreConstraintVo ec=new EcoreConstraintVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("NAME"));
				ec.setObject_identifier(rs.getString("OBJECT_IDENTIFIER"));
				ec.setDefinition(rs.getString("DEFINITION"));
				ec.setExpression(rs.getString("expression"));
				ec.setExpression_language(rs.getString("expression_language"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setRegistration_status(rs.getString("registration_status"));
				ec.setRemoval_date(rs.getDate("removal_date"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(eeql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreExampleVo ec=new EcoreExampleVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_id(rs.getString("OBJ_ID"));
				ec.setObj_type(rs.getString("Obj_type"));
				ec.setExample(rs.getString("example"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(vsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreNextVersionsVo ec=new EcoreNextVersionsVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setNext_version_id(rs.getString("next_version_id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(mksql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreSemanticMarkupVo ec=new EcoreSemanticMarkupVo();
				ec.setId(rs.getString("ID"));
				ec.setObj_type(rs.getString("obj_type"));
				ec.setType(rs.getString("type"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setObj_id(rs.getString("obj_Id"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
			pstmt=conn.prepareStatement(mkesql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			while(rs.next()) {
				EcoreSemanticMarkupElementVo ec=new EcoreSemanticMarkupElementVo();
				ec.setId(rs.getString("ID"));
				ec.setName(rs.getString("name"));
				ec.setValue(rs.getString("value"));
				ec.setSemantic_markup_id(rs.getString("semantic_Markup_Id"));
				ec.setCreate_time(rs.getDate("create_time"));
				ec.setCreate_user(rs.getString("create_user"));
				ec.setUpdate_time(rs.getDate("update_time"));
				ec.setUpdate_user(rs.getString("update_user"));
				ec.setIs_from_iso20022(rs.getString("is_from_iso20022"));
				ec.setOwner(rs.getString("owner"));
				objects.add(ec);
			}
		}
	}
	
	
	/**
	 * 按照id获取消息相关的数据
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public EcoreDocumentTreeNode findMessageDocumentDataById(String id) throws Exception{
		EcoreDocumentTreeNode ecoreDocumentTreeNode=null;
		String dsql="select * from ECORE_MESSAGE_DEFINITION where id=?";
		String bsql="select * from ECORE_MESSAGE_BUILDING_BLOCK where message_id=?";
		Map<String,String> idMap=new HashMap<String,String>();
		Map<String,List<String>> csMap=new HashMap<String, List<String>>();
		String dstrSql="select id,name from ecore_data_type";
		String estrSql="select id,name from ecore_external_schema";
		String mstrSql="select id,name from ecore_message_component";
		String csql="select obj_id,name from ecore_constraint";
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
		st=conn.createStatement();
		rs=st.executeQuery(csql);
		while(rs.next()) {
			String csid=rs.getString(1);
			String csname=rs.getString(2);
			List<String> clist=null;
			if(csMap.containsKey(csid)) {
				clist=csMap.get(csid);
			}else {
				clist =new ArrayList<String>();
				csMap.put(csid, clist);
			}
			clist.add(csname);
		}
		pstmt=conn.prepareStatement(dsql);
		pstmt.setString(1, id);
		//获取block数据
		rs=pstmt.executeQuery();
		while(rs.next()) {
			ecoreDocumentTreeNode = new EcoreDocumentTreeNode();
			ecoreDocumentTreeNode.setLvl(1);
			ecoreDocumentTreeNode.setName(rs.getString("NAME"));
			ecoreDocumentTreeNode.setXmlTag(rs.getString("xml_Tag"));
			List<String> ctemp=csMap.get(rs.getString("id"));
			if(ctemp!=null && !ctemp.isEmpty()) {
				String ctr="";
				for(String s:ctemp) {
					ctr+=s+",";
				}
				ctr=ctr.substring(0, ctr.length()-1);
				ecoreDocumentTreeNode.setConstraint(ctr);
			}
			break;
		}
		if(ecoreDocumentTreeNode!=null) {
        	pstmt=conn.prepareStatement(bsql);
			pstmt.setString(1, id);
			//获取block数据
			rs=pstmt.executeQuery();
			List<EcoreDocumentTreeNode> bnodes=ecoreDocumentTreeNode.getChildNodes();
			while(rs.next()) {
				EcoreDocumentTreeNode bnode=new EcoreDocumentTreeNode();
				bnode.setLvl(2);
				bnode.setName(rs.getString("NAME"));
				bnode.setXmlTag(rs.getString("xml_Tag"));
				bnode.setDataType(rs.getString("data_Type"));
				bnode.setDataTypeId(rs.getString("data_Type_Id"));
				String min_Occurs="".equals(rs.getInt("min_Occurs")+"")?"*":(rs.getInt("min_Occurs")+"");
				String max_Occurs="".equals(rs.getInt("max_Occurs")+"")?"*":(rs.getInt("max_Occurs")+"");
				bnode.setMult("["+min_Occurs+","+max_Occurs+"]");
				bnode.setType(idMap.get(rs.getString("data_Type_Id")));
				List<String> ctemp=csMap.get(rs.getString("id"));
				if(ctemp!=null && !ctemp.isEmpty()) {
					String ctr="";
					for(String s:ctemp) {
						ctr+=s+",";
					}
					ctr=ctr.substring(0, ctr.length()-1);
					bnode.setConstraint(ctr);
				}
				bnodes.add(bnode);
			}
			if(bnodes!=null && !bnodes.isEmpty()) {
				for(EcoreDocumentTreeNode b:bnodes) {
					if(b.getDataTypeId()!=null) {
						if("2".equals(b.getDataType())) {
							findMessageComponent(b.getDataTypeId(),b,idMap,csMap);
						}
					}
				}
			}
		}
		return ecoreDocumentTreeNode;
	}

	/**
	 * 获取组件信息
	 * @param id
	 * @param pnode
	 * @param idMap
	 * @throws Exception
	 */
	private void findMessageComponent(String id,EcoreDocumentTreeNode pnode,Map<String,String> idMap,
			Map<String,List<String>> csMap) throws Exception{
		String presql="select * from ECORE_MESSAGE_ELEMENT where MESSAGE_COMPONENT_ID='"+id+"'";
		//elment
		st=conn.createStatement();
		rs=st.executeQuery(presql);
		List<EcoreDocumentTreeNode> nodes = pnode.getChildNodes();
		while(rs.next()) {
			EcoreDocumentTreeNode elnode=new EcoreDocumentTreeNode();
			elnode.setLvl(pnode.getLvl()+1);
			elnode.setName(rs.getString("NAME"));
			elnode.setDataType(rs.getString("TYPE"));
			elnode.setDataTypeId(rs.getString("TYPE_ID"));
			String min_Occurs="".equals(rs.getInt("min_Occurs")+"")?"*":(rs.getInt("min_Occurs")+"");
			String max_Occurs="".equals(rs.getInt("max_Occurs")+"")?"*":(rs.getInt("max_Occurs")+"");
			elnode.setMult("["+min_Occurs+","+max_Occurs+"]");
			elnode.setXmlTag(rs.getString("XML_TAG"));
			elnode.setType(idMap.get(rs.getString("TYPE_ID")));
			List<String> ctemp=csMap.get(rs.getString("id"));
			if(ctemp!=null && !ctemp.isEmpty()) {
				String ctr="";
				for(String s:ctemp) {
					ctr+=s+",";
				}
				ctr=ctr.substring(0, ctr.length()-1);
				elnode.setConstraint(ctr);
			}
			nodes.add(elnode);
        }
		if(nodes!=null && !nodes.isEmpty()) {
			for(EcoreDocumentTreeNode b:nodes) {
				if(b.getDataTypeId()!=null) {
					if("2".equals(b.getDataType())) {
						findMessageComponent(b.getDataTypeId(),b,idMap,csMap);
					}
				}
			}
		}
	}
}