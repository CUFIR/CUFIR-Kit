package org.cufir.s.data.dao.impl;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreBusinessComponent;
import org.cufir.s.ecore.bean.EcoreBusinessComponentRL;
import org.cufir.s.ecore.bean.EcoreBusinessElement;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.ecore.bean.EcoreSemanticMarkup;
import org.cufir.s.ecore.bean.EcoreSemanticMarkupElement;
import org.cufir.s.data.util.DbUtil;
import org.cufir.s.data.util.DerbyUtil;
import org.cufir.s.data.vo.EcoreBusinessComponentVO;
import org.cufir.s.data.vo.EcoreBusinessElementVO;
import org.cufir.s.data.vo.EcoreTreeNode;
import org.cufir.s.data.vo.RemovedObjectVO;
import org.cufir.s.data.vo.SynonymVO;

/**
 * EcoreBusinessComponent数据库操作
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class EcoreBusinessComponentImpl {

	private static Logger logger = Logger.getLogger(EcoreBusinessComponentImpl.class);
	
	private final static String QSQL = "SELECT * FROM ECORE_BUSINESS_COMPONENT t WHERE 1=1 ";

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement st = null;
	private ResultSet rs = null;

	public List<EcoreBusinessComponent> findByBusinessElementTypeId(String typeId) {
		String querySql = QSQL + "AND t.ID IN (SELECT t2.BUSINESS_COMPONENT_ID FROM ECORE_BUSINESS_ELEMENT t2 WHERE t2.TYPE_ID = ? ) ORDER BY t.NAME ";
		return DbUtil.get().find(querySql, EcoreBusinessComponent.class, typeId);
	}
	
	public List<EcoreBusinessComponent> findAll() {
		return DbUtil.get().find(QSQL + "ORDER BY NAME", EcoreBusinessComponent.class);
	}

	public Map<String, EcoreBusinessComponent> findMdr3() {
		String sql = "select t.id,t.name,t.definition from ecore_business_component t order by LOWER(t.name)";
		List<Map<String, Object>> maps = DbUtil.get().find(sql);
		Map<String, EcoreBusinessComponent> map = new LinkedHashMap<>();
		for(Map<String, Object> m : maps) {
			EcoreBusinessComponent bc = new EcoreBusinessComponent();
			bc.setId(m.get("id") == null ? "" : m.get("id") + "");
			bc.setName(m.get("name") == null ? "" : m.get("name") + "");
			map.put(bc.getId(), bc);
		}
		return map;
	}
	
	/**
	 * 获取Inheritance信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public EcoreTreeNode findBusinessComponentInheritances(String id) throws Exception {
		EcoreTreeNode pnode = new EcoreTreeNode();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		Map<String, EcoreTreeNode> bs = new HashMap<String, EcoreTreeNode>();
		Map<String, String> pbs = new HashMap<String, String>();
		Map<String, List<String>> pbsId = new HashMap<String, List<String>>();
		String sql = "select t.id, t.name, tr.p_id from ecore_business_component t "
				+ "  left join ecore_business_component_rl tr on t.id = tr.id";
		try {
			st = conn.createStatement();
			// 获取BUSINESS_COMPONENT
			rs = st.executeQuery(sql);
			while (rs.next()) {
				EcoreTreeNode po = new EcoreTreeNode();
				String tid = rs.getString(1);
				String pid = rs.getString(3);
				po.setId(tid);
				po.setPid(pid);
				po.setName(rs.getString(2));
				po.setType("2");
				bs.put(tid, po);
				if (pid != null) {
					pbs.put(tid, pid);
					List<String> cids = pbsId.get(pid);
					if (cids == null || cids.isEmpty()) {
						cids = new ArrayList<String>();
						pbsId.put(pid, cids);
					}
					cids.add(tid);
				}
			}
			String pnodeId = getPnodeId(id, pbs);
			pnode = bs.get(pnodeId);
			setChildNodeInfo(pnode, bs, pbsId);
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return pnode;
	}

	/**
	 * 设置子节点信息
	 * 
	 * @param pnode
	 * @param bs
	 * @param pbsId
	 */
	private void setChildNodeInfo(EcoreTreeNode pnode, Map<String, EcoreTreeNode> bs, Map<String, List<String>> pbsId) {
		String pid = pnode.getId();
		List<String> cids = pbsId.get(pid);
		if (pnode != null && cids != null && !cids.isEmpty()) {
			List<EcoreTreeNode> nodes = new ArrayList<EcoreTreeNode>();
			for (String cid : cids) {
				EcoreTreeNode cnode = bs.get(cid);
				nodes.add(cnode);
				setChildNodeInfo(cnode, bs, pbsId);
			}
			pnode.setChildNodes(nodes);
		}
	}

	/**
	 * 获取父节点ID
	 * 
	 * @param id
	 * @param ids
	 * @return
	 */
	private String getPnodeId(String id, Map<String, String> ids) {
		if (ids.containsKey(id)) {
			String tpid = ids.get(id);
			return getPnodeId(tpid, ids);
		} else {
			return id;
		}
	}

	/**
	 * 获取BusinessComponentNodes
	 * 
	 * @return List<EcoreTreeNode>
	 */
	public List<EcoreTreeNode> findAllEcoreBusinessComponents() throws Exception {
		List<EcoreTreeNode> businessComponents = new ArrayList<EcoreTreeNode>();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String bsql = "SELECT T.ID,T.NAME,t.registration_status FROM ECORE_BUSINESS_COMPONENT T ORDER BY T.NAME";
		try {
			st = conn.createStatement();
			// 获取BUSINESS_COMPONENT
			rs = st.executeQuery(bsql);
			while (rs.next()) {
				EcoreTreeNode po = new EcoreTreeNode();
				po.setId(rs.getString(1));
				po.setName(rs.getString(2));
				po.setLevel("2");
				po.setType("2");
				po.setRegistrationStatus(rs.getString("registration_status"));
				businessComponents.add(po);
			}
			conn.commit();
		} catch (Exception e) {
			logger.error("查询数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
		return businessComponents;
	}

	/**
	 * 获取BusinessComponentNodes
	 * 
	 * @return EcoreTreeNode
	 */
	public EcoreTreeNode findEcoreBusinessComponentTreeNodes() throws Exception {
		EcoreTreeNode ecoreTreeNode = new EcoreTreeNode();
		conn = DerbyUtil.getConnection(); // 获得数据库连接
		String bsql = "SELECT T.ID,T.NAME,t.registration_status FROM ECORE_BUSINESS_COMPONENT T ORDER BY T.NAME";
		String eSql = "SELECT T.ID,T.NAME,T.BUSINESS_COMPONENT_ID,T.TYPE,t.registration_status FROM ECORE_BUSINESS_ELEMENT T ORDER BY T.NAME";
		String rsql = "SELECT T.ID,T.P_ID FROM ECORE_BUSINESS_COMPONENT_RL T";
		Map<String, List<EcoreTreeNode>> elementMaps = new HashMap<String, List<EcoreTreeNode>>();
		Map<String, EcoreTreeNode> businessComponentMaps = new HashMap<String, EcoreTreeNode>();
		Map<String, List<EcoreTreeNode>> pMaps = new HashMap<String, List<EcoreTreeNode>>();
		Map<String, List<EcoreTreeNode>> sMaps = new HashMap<String, List<EcoreTreeNode>>();
		List<EcoreTreeNode> businessComponents = new ArrayList<EcoreTreeNode>();
		try {
			st = conn.createStatement();
			// 获取element
			rs = st.executeQuery(eSql);
			while (rs.next()) {
				EcoreTreeNode po = new EcoreTreeNode();
				po.setId(rs.getString(1));
				po.setName(rs.getString(2));
				po.setType(rs.getString(4));
				po.setLevel("4");
				po.setRegistrationStatus(rs.getString("registration_status"));
				String businessComponentId = rs.getString(3);
				if (elementMaps.containsKey(businessComponentId)) {
					elementMaps.get(businessComponentId).add(po);
				} else {
					List<EcoreTreeNode> nodes = new ArrayList<EcoreTreeNode>();
					nodes.add(po);
					elementMaps.put(businessComponentId, nodes);
				}
			}
			// 获取BUSINESS_COMPONENT
			rs = st.executeQuery(bsql);
			while (rs.next()) {
				EcoreTreeNode po = new EcoreTreeNode();
				po.setId(rs.getString(1));
				po.setName(rs.getString(2));
				po.setLevel("2");
				po.setRegistrationStatus(rs.getString("registration_status"));
				businessComponentMaps.put(po.getId(), po);
				businessComponents.add(po);
			}
			// 获取BUSINESS_COMPONENT_RL
			rs = st.executeQuery(rsql);
			while (rs.next()) {
				String id = rs.getString(1);
				String pid = rs.getString(2);
				EcoreTreeNode node = businessComponentMaps.get(id);
				EcoreTreeNode pnode = businessComponentMaps.get(pid);
				if (pid != null) {
					if (pMaps.containsKey(pid)) {
						pMaps.get(pid).add(node);
					} else {
						List<EcoreTreeNode> nodes = new ArrayList<EcoreTreeNode>();
						nodes.add(node);
						pMaps.put(pid, nodes);
					}
					List<EcoreTreeNode> pnodes = new ArrayList<EcoreTreeNode>();
					pnodes.add(pnode);
					sMaps.put(id, pnodes);
				}
			}
			// 整合
			for (EcoreTreeNode businessComponent : businessComponents) {
				List<EcoreTreeNode> bsPro = new ArrayList<EcoreTreeNode>();
				// 获取Properties
				String bId = businessComponent.getId();
				EcoreTreeNode propertiesPo = new EcoreTreeNode();
				propertiesPo.setName("Properties");
				propertiesPo.setLevel("3");
				propertiesPo.setChildNodes(elementMaps.get(bId));
				bsPro.add(propertiesPo);
				// 获取subtype
				EcoreTreeNode subTypePo = new EcoreTreeNode();
				subTypePo.setName("Sub-Types");
				subTypePo.setLevel("3");
				subTypePo.setPid(bId);
				subTypePo.setChildNodes(pMaps.get(bId));
				bsPro.add(subTypePo);
				// 获取supertype
				EcoreTreeNode superTypePo = new EcoreTreeNode();
				superTypePo.setName("Super-Types");
				superTypePo.setLevel("3");
				superTypePo.setId(businessComponent.getPid());
				superTypePo.setChildNodes(sMaps.get(bId));
				bsPro.add(superTypePo);
				// 设置
				businessComponent.setChildNodes(bsPro);
			}
			// 顶级
			ecoreTreeNode.setName("Business Components");
			ecoreTreeNode.setLevel("1");
			ecoreTreeNode.setType("2");
			ecoreTreeNode.setChildNodes(businessComponents);
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
	 * 保存BusinessComponents
	 * 
	 * @param ecoreBusinessComponents
	 */
	public void addEcoreBusinessComponentList(List<EcoreBusinessComponent> ecoreBusinessComponents) throws Exception {
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接

			String sql = "insert into ECORE_BUSINESS_COMPONENT(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,previous_version,version,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessComponent po : ecoreBusinessComponents) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if (po.getRemovalDate() != null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5, Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getVersion());
				pstmt.setString(9, po.getCreateUser());
				pstmt.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(11, po.getIsfromiso20022());
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

	/**
	 * 删除业务组件
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteEcoreBusinessComponent(String id) throws Exception {
		boolean key = false;
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			String presql = "select id from ECORE_BUSINESS_ELEMENT where business_component_id='" + id + "'";
			String rsql = "delete from ECORE_BUSINESS_COMPONENT_RL where id=? or p_id=?";
			String sql = "delete from ECORE_BUSINESS_COMPONENT where id=?";
			// common
			String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
			String dsql = "delete from ECORE_DOCLET where obj_id=?";
			String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
			String vsql = "delete from ECORE_NEXT_VERSIONS where id=?";
			st = conn.createStatement();
			String mbIds = "";
			// 获取block数据
			rs = st.executeQuery(presql);
			while (rs.next()) {
				String mbId = rs.getString(1);
				mbIds += "'" + mbId + "',";
			}
			if (!"".equals(mbIds)) {
				mbIds = mbIds.substring(0, mbIds.length() - 1);
				String esql = "delete from ECORE_BUSINESS_ELEMENT where id in (" + mbIds + ")";
				// common
				String bsmsql = "delete from ecore_semantic_markup  where obj_id in (" + mbIds + ")";
				String bsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
						+ " (select id from ecore_semantic_markup where obj_id in (" + mbIds + "))";
				csql += " or obj_id in (" + mbIds + ")";
				dsql += " or obj_id in (" + mbIds + ")";
				eeql += " or obj_id in (" + mbIds + ")";
				vsql += " or id in (" + mbIds + ")";

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

			pstmt = conn.prepareStatement(rsql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			pstmt.executeUpdate();

			// saveRemovedInfo(id);

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			int num = pstmt.executeUpdate();

			conn.commit();
			if (num > 0) {
				key = true;
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
	 * 更新BusinessComponents
	 * 
	 * @param ecoreBusinessComponents
	 */
	public void updateEcoreBusinessComponentList(List<EcoreBusinessComponent> ecoreBusinessComponents)
			throws Exception {
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接

			String sql = "update ECORE_BUSINESS_COMPONENT set update_time=?,name=?,definition=?,registration_status=?"
					+ ",removal_date=?,"
					+ "object_identifier=?,previous_version=?,version=?, update_user=?,is_from_iso20022=?  "
					+ " where id=?";

			// 连接失败,再次请求连接
			if (conn == null) {
				conn = DerbyUtil.getConnection(); // 获得数据库连接
			}
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessComponent po : ecoreBusinessComponents) {
				pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if (po.getRemovalDate() != null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5, Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getPreviousVersion());
				pstmt.setString(8, po.getVersion());
				pstmt.setString(9, po.getUpdateUser());
				pstmt.setString(10, po.getIsfromiso20022());
				pstmt.setString(11, po.getId());
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

	/**
	 * 保存BusinessComponent数据
	 * 
	 * @param ecoreBusinessComponentVOs
	 */
	public boolean saveEcoreBusinessComponentVOList(List<EcoreBusinessComponentVO> ecoreBusinessComponentVOs)
			throws Exception {
		boolean key = false;
		List<EcoreBusinessComponent> ecoreBusinessComponents = new ArrayList<EcoreBusinessComponent>();
		List<EcoreConstraint> ecoreConstraints = new ArrayList<EcoreConstraint>();
		List<EcoreBusinessElement> ecoreBusinessElements = new ArrayList<EcoreBusinessElement>();
		List<EcoreExample> ecoreExamples = new ArrayList<EcoreExample>();
		List<EcoreNextVersions> ecoreNextVersions = new ArrayList<EcoreNextVersions>();
		List<String> dataIds = new ArrayList<String>();
		List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs = new ArrayList<EcoreBusinessComponentRL>();
		List<EcoreSemanticMarkup> ecoreSemanticMarkups = new ArrayList<EcoreSemanticMarkup>();
		List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements = new ArrayList<EcoreSemanticMarkupElement>();
		List<RemovedObjectVO> removeds = new ArrayList<RemovedObjectVO>();
		// 解析保存的内容
		for (EcoreBusinessComponentVO vo : ecoreBusinessComponentVOs) {
			EcoreBusinessComponent ecoreBusinessComponent = vo.getEcoreBusinessComponent();
			EcoreBusinessComponentRL ecoreBusinessComponentRL = vo.getParentBusinessComponent();
			List<EcoreConstraint> constraints = vo.getEcoreConstraints();
			List<EcoreExample> examples = vo.getEcoreExamples();
			List<EcoreNextVersions> nextVersions = vo.getEcoreNextVersions();
			List<EcoreBusinessElementVO> elementVOs = vo.getEcoreBusinessElementVOs();
			List<RemovedObjectVO> rmds = vo.getRemovedObjectVOs();
			if (constraints != null && !constraints.isEmpty()) {
				ecoreConstraints.addAll(constraints);
			}
			if (examples != null && !examples.isEmpty()) {
				ecoreExamples.addAll(examples);
			}
			if (nextVersions != null && !nextVersions.isEmpty()) {
				ecoreNextVersions.addAll(nextVersions);
			}
			if (rmds != null && !rmds.isEmpty()) {
				removeds.addAll(rmds);
			}
			// 解析CODE
			if (elementVOs != null && !elementVOs.isEmpty()) {
				for (EcoreBusinessElementVO evo : elementVOs) {
					EcoreBusinessElement ecoreBusinessElement = evo.getEcoreBusinessElement();
					List<EcoreConstraint> cconstraints = evo.getEcoreConstraints();
					List<EcoreExample> cexamples = evo.getEcoreExamples();
					List<EcoreNextVersions> cnextVersions = evo.getEcoreNextVersions();
					List<SynonymVO> ss = evo.getSynonyms();
					if (cconstraints != null && !cconstraints.isEmpty()) {
						ecoreConstraints.addAll(cconstraints);
					}
					if (cexamples != null && !cexamples.isEmpty()) {
						ecoreExamples.addAll(cexamples);
					}
					if (cnextVersions != null && !cnextVersions.isEmpty()) {
						ecoreNextVersions.addAll(cnextVersions);
					}
					if (ss != null && !ss.isEmpty()) {
						for (SynonymVO sv : ss) {
							String muId = UUID.randomUUID().toString();
							EcoreSemanticMarkup up = new EcoreSemanticMarkup();
							up.setObjId(sv.getObjId());
							up.setType("Synonym");
							up.setIsfromiso20022(ecoreBusinessElement.getIsfromiso20022());
							up.setObjType("3");
							up.setId(muId);

							String muIdC = UUID.randomUUID().toString();
							EcoreSemanticMarkupElement smec = new EcoreSemanticMarkupElement();
							smec.setId(muIdC);
							smec.setIsfromiso20022(ecoreBusinessElement.getIsfromiso20022());
							smec.setName("context");
							smec.setValue(sv.getContext());
							smec.setSemanticMarkupId(muId);

							String muIdS = UUID.randomUUID().toString();
							EcoreSemanticMarkupElement smes = new EcoreSemanticMarkupElement();
							smes.setId(muIdS);
							smes.setIsfromiso20022(ecoreBusinessElement.getIsfromiso20022());
							smes.setName("value");
							smes.setValue(sv.getSynonym());
							smes.setSemanticMarkupId(muId);
							ecoreSemanticMarkups.add(up);
							ecoreSemanticMarkupElements.add(smes);
							ecoreSemanticMarkupElements.add(smec);
						}
					}
					ecoreBusinessElements.add(ecoreBusinessElement);
				}
			}
			dataIds.add(ecoreBusinessComponent.getId());
			ecoreBusinessComponents.add(ecoreBusinessComponent);
			if (ecoreBusinessComponentRL != null) {
				ecoreBusinessComponentRLs.add(ecoreBusinessComponentRL);
			}
		}
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			if (dataIds != null && !dataIds.isEmpty()) {
				for (String id : dataIds) {
					// 先删除业务组件下面的所有数据
					deleteBusinessComponentByIds(id);
				}
			}
			// 删除已有的
			if (removeds != null && !removeds.isEmpty()) {
				// saveRemovedInfo(removeds);
			}
			// 开始保存
			if (ecoreBusinessComponents != null && !ecoreBusinessComponents.isEmpty()) {
				saveBusinessComponent(ecoreBusinessComponents);
				if (ecoreBusinessElements != null && !ecoreBusinessElements.isEmpty()) {
					saveBusinessElement(ecoreBusinessElements);
				}
				if (ecoreBusinessComponentRLs != null && !ecoreBusinessComponentRLs.isEmpty()) {
					saveBusinessComponentRL(ecoreBusinessComponentRLs);
				}
				if (ecoreExamples != null && !ecoreExamples.isEmpty()) {
					saveExample(ecoreExamples);
				}
				if (ecoreConstraints != null && !ecoreConstraints.isEmpty()) {
					saveConstraint(ecoreConstraints);
				}
				if (ecoreNextVersions != null && !ecoreNextVersions.isEmpty()) {
					saveNextVersions(ecoreNextVersions);
				}
				if (ecoreSemanticMarkups != null && !ecoreSemanticMarkups.isEmpty()) {
					saveSemanticMarkup(ecoreSemanticMarkups);
					if (ecoreSemanticMarkupElements != null && !ecoreSemanticMarkupElements.isEmpty()) {
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
		key = true;
		return key;
	}

	private void saveSemanticMarkupElement(List<EcoreSemanticMarkupElement> ecoreSemanticMarkupElements)
			throws SQLException {
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

	private void saveSemanticMarkup(List<EcoreSemanticMarkup> ecoreSemanticMarkups) throws SQLException {
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

	private void saveBusinessComponentRL(List<EcoreBusinessComponentRL> ecoreBusinessComponentRLs) throws SQLException {
		String sql = "insert into ECORE_BUSINESS_COMPONENT_RL(id,p_id,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreBusinessComponentRL po : ecoreBusinessComponentRLs) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getPId());
			pstmt.setString(3, po.getCreateUser());
			pstmt.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(5, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void deleteBusinessComponentByIds(String id) throws Exception {
		String presql = "select id from ECORE_BUSINESS_ELEMENT where business_component_id='" + id + "'";
		String rsql = "delete from ECORE_BUSINESS_COMPONENT_RL where id=? or p_id=?";
		String csql = "delete from ECORE_CONSTRAINT where obj_id=?";
		String dsql = "delete from ECORE_DOCLET where obj_id=?";
		String eeql = "delete from ECORE_EXAMPLE where obj_id=?";
		String vsql = "delete from ECORE_NEXT_VERSIONS where id=?";
		st = conn.createStatement();
		String mbIds = "";
		// 获取block数据
		rs = st.executeQuery(presql);
		while (rs.next()) {
			String mbId = rs.getString(1);
			mbIds += "'" + mbId + "',";
		}
		if (!"".equals(mbIds)) {
			mbIds = mbIds.substring(0, mbIds.length() - 1);
			String esql = "delete from ECORE_BUSINESS_ELEMENT where id in (" + mbIds + ")";
			// common
			String bsmsql = "delete from ecore_semantic_markup  where obj_id in (" + mbIds + ")";
			String bsemsql = "delete from ecore_semantic_markup_element where semantic_markup_id in"
					+ " (select id from ecore_semantic_markup where obj_id in (" + mbIds + "))";
			csql += " or obj_id in (" + mbIds + ")";
			dsql += " or obj_id in (" + mbIds + ")";
			eeql += " or obj_id in (" + mbIds + ")";
			vsql += " or id in (" + mbIds + ")";

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

		pstmt = conn.prepareStatement(rsql);
		pstmt.setString(1, id);
		pstmt.setString(2, id);
		pstmt.executeUpdate();
	}

	private void saveBusinessComponent(List<EcoreBusinessComponent> ecoreBusinessComponents) throws SQLException {
		List<EcoreBusinessComponent> addEcoreBusinessComponents = new ArrayList<EcoreBusinessComponent>();
		List<EcoreBusinessComponent> updateEcoreBusinessComponents = new ArrayList<EcoreBusinessComponent>();
		for (EcoreBusinessComponent po : ecoreBusinessComponents) {
			String csql = "SELECT T.ID FROM ECORE_BUSINESS_COMPONENT T WHERE id=?";
			pstmt = conn.prepareStatement(csql);
			pstmt.setString(1, po.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				updateEcoreBusinessComponents.add(po);
			} else {
				addEcoreBusinessComponents.add(po);
			}
		}
		String sql = "insert into ECORE_BUSINESS_COMPONENT(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,version,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreBusinessComponent po : addEcoreBusinessComponents) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if (po.getRemovalDate() != null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getVersion());
			pstmt.setString(9, po.getCreateUser());
			pstmt.setTimestamp(10, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(11, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句

		String usql = "update ECORE_BUSINESS_COMPONENT set update_time=?,name=?,definition=?,registration_status=?"
				+ ",removal_date=?,"
				+ "object_identifier=?,previous_version=?,version=?, update_user=?,is_from_iso20022=?  "
				+ " where id=?";
		pstmt = conn.prepareStatement(usql);
		for (EcoreBusinessComponent po : updateEcoreBusinessComponents) {
			pstmt.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if (po.getRemovalDate() != null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getVersion());
			pstmt.setString(9, po.getUpdateUser());
			pstmt.setString(10, po.getIsfromiso20022());
			pstmt.setString(11, po.getId());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveExample(List<EcoreExample> ecoreExamples) throws SQLException {
		String sql = "insert into ecore_example(id,obj_id,obj_type,example,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreExample po : ecoreExamples) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObjId());
			pstmt.setString(3, po.getObjType());
			pstmt.setString(4, po.getExample());
			pstmt.setString(5, po.getCreateUser());
			pstmt.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(7, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveBusinessElement(List<EcoreBusinessElement> ecoreBusinessElements) throws SQLException {
		String sql = "insert into ECORE_BUSINESS_ELEMENT(id,name,definition,registration_status,removal_date,"
				+ "object_identifier,previous_version,type,type_Id,max_Occurs,min_Occurs,"
				+ "business_component_id,version,is_derived,is_Message_Association_End,create_user,create_time,is_from_iso20022) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreBusinessElement po : ecoreBusinessElements) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getName());
			pstmt.setString(3, po.getDefinition());
			pstmt.setString(4, po.getRegistrationStatus());
			if (po.getRemovalDate() != null) {
				pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(5, Types.DATE);
			}
			pstmt.setString(6, po.getObjectIdentifier());
			pstmt.setString(7, po.getPreviousVersion());
			pstmt.setString(8, po.getType());
			pstmt.setString(9, po.getTypeId());
			if (po.getMaxOccurs() != null) {
				pstmt.setInt(10, po.getMaxOccurs());
			} else {
				pstmt.setNull(10, Types.INTEGER);
			}
			if (po.getMinOccurs() != null) {
				pstmt.setInt(11, po.getMinOccurs());
			} else {
				pstmt.setNull(11, Types.INTEGER);
			}
			pstmt.setString(12, po.getBusinessComponentId());
			pstmt.setString(13, po.getVersion());
			pstmt.setString(14, po.getIsDerived());
			pstmt.setString(15, po.getIsMessageAssociationEnd());
			pstmt.setString(16, po.getCreateUser());
			pstmt.setTimestamp(17, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(18, po.getIsfromiso20022());
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveConstraint(List<EcoreConstraint> ecoreConstraints) throws SQLException {
		String sql = "insert into ecore_constraint(id,obj_id,obj_type,expression_language,definition"
				+ ",name,registration_status,removal_date,object_identifier,create_user,create_time,is_from_iso20022,expression) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = conn.prepareStatement(sql);
		for (EcoreConstraint po : ecoreConstraints) {
			pstmt.setString(1, po.getId());
			pstmt.setString(2, po.getObj_id());
			pstmt.setString(3, po.getObj_type());
			pstmt.setString(4, po.getExpressionlanguage());
			if (po.getDefinition() != null) {
				pstmt.setString(5, po.getDefinition());
			} else {
				pstmt.setString(5, "");
			}
			pstmt.setString(6, po.getName());
			pstmt.setString(7, po.getRegistrationStatus());
			if (po.getRemovalDate() != null) {
				pstmt.setDate(8, new Date(po.getRemovalDate().getTime()));
			} else {
				pstmt.setNull(8, Types.DATE);
			}
			pstmt.setString(9, po.getObjectIdentifier());
			pstmt.setString(10, po.getCreateUser());
			pstmt.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
			pstmt.setString(12, po.getIsfromiso20022());
			if (po.getExpression() != null) {
				pstmt.setString(13, po.getExpression());
			} else {
				pstmt.setString(13, "");
			}
			pstmt.addBatch();
		}
		pstmt.executeBatch();// 执行更新语句
	}

	private void saveNextVersions(List<EcoreNextVersions> ecoreNextVersionss) throws SQLException {
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
}