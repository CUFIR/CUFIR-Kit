package com.cfets.cufir.plugin.mr.bean;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * 树的视图
 * @author jin.c.li
 *
 */
public class TreeListItem extends TreeItem{
	
	@Override
	protected void checkSubclass() {
	}

	/**
	 * 一级菜单展示
	 * @param parent
	 * @param parentTreeItemDataBean
	 */
	public TreeListItem(Tree parent, EcoreTreeNode ecoreTreeNode) {
		
		super(parent, SWT.NULL);
		if(ecoreTreeNode.getLevel().equals(TreeLevelEnum.LEVEL_1.getLevel())) {
			//根节点
			this.setText(ecoreTreeNode.getName());
			List<EcoreTreeNode> secondChildNodes = ecoreTreeNode.getChildNodes();
			//防止大对象占用内存
			ecoreTreeNode.setChildNodes(null);
			this.setData("EcoreTreeNode", ecoreTreeNode);
			this.setData("type", TreeParentEnum.DATA_TYPES.getName());
			
			if("1".equals(ecoreTreeNode.getType())) {		//区分DataType, Business Component, Message Component等树的根
				//数据类型
				this.setImage(ImgUtil.createImg(TreeParentEnum.DATA_TYPES.getImgPath()));
				//填充第二级
				DataTypesEnum[] values = DataTypesEnum.values();
				
				//存在就不需要再次加载
				if(this.getItemCount() != secondChildNodes.size()) {
					for (DataTypesEnum typeEnum : values) {
						TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean("", typeEnum.getName(), typeEnum.getImgPath(), typeEnum.getType(), TreeLevelEnum.LEVEL_2.getLevel());
						TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);	// Code Set固定节点
						EcoreTreeNode secondEcoreTreeNode = new EcoreTreeNode();
						secondEcoreTreeNode.setName(typeEnum.getName());
						secondEcoreTreeNode.setLevel(TreeLevelEnum.LEVEL_2.getLevel());
						secondEcoreTreeNode.setType("1");
						secondTreeListItem.setData("EcoreTreeNode", secondEcoreTreeNode);	
						secondTreeListItem.setData("type", TreeParentEnum.DATA_TYPES.getName());
						if("1".equals(typeEnum.getType())) {
								//填充第三级菜单
								List<EcoreTreeNode> thirdEcoreTreeNodes = secondChildNodes.get(0).getChildNodes();

								//防止大对象占用内存
								secondChildNodes.get(0).setChildNodes(null);
								if(thirdEcoreTreeNodes != null && thirdEcoreTreeNodes.size() > 0) {
									thirdEcoreTreeNodes.stream().forEach(thirdTreeNode ->{
										String imgPath = "";
										if("1".equals(thirdTreeNode.getObjType())){			// 可衍生的Code Set子节点
//											if ("Registered".equals(thirdTreeNode.getRegistrationStatus())) {
//												imgPath = ImgUtil.CODE_SET_SUB1_2_LOCK;
//											} else {
//												imgPath = ImgUtil.CODE_SET_SUB1_2;
//											}
											imgPath = ImgUtil.CODE_SET_SUB1_2;
										}else if("2".equals(thirdTreeNode.getObjType())) {	// 不可衍生的Code Set子节点
											if ("Registered".equals(thirdTreeNode.getRegistrationStatus())) {
												imgPath = ImgUtil.CODE_SET_SUB1_LOCK;
											} else {
												imgPath = ImgUtil.CODE_SET_SUB1;
											}
											
										}
										
										TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdTreeNode.getId(), thirdTreeNode.getName(), imgPath, thirdTreeNode.getType(), thirdTreeNode.getLevel());
										TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
										
										//填充第四级菜单
										List<EcoreTreeNode> fourthChildNodes = thirdTreeNode.getChildNodes();
										
										//防止大对象占用内存
										thirdTreeNode.setChildNodes(null);
										
										thirdTreeNode.setLevel(TreeLevelEnum.LEVEL_3.getLevel());
										thirdTreeListItem.setData("EcoreTreeNode", thirdTreeNode);
										thirdTreeListItem.setData("type", DataTypesEnum.CODE_SETS.getName());
										
										if (fourthChildNodes != null && fourthChildNodes.size() > 0) {
											fourthChildNodes.stream().forEach(fourthChildNode -> {
												TreeListItem fourthTreeListItem = new TreeListItem(thirdTreeListItem, new TreeItemDataBean(fourthChildNode.getId(), fourthChildNode.getName(), ImgUtil.CODE_SET_SUB2, fourthChildNode.getType(), fourthChildNode.getLevel()));
												String childId = fourthChildNode.getId();
												fourthChildNode.setId(thirdTreeNode.getId());
												fourthChildNode.setLevel(TreeLevelEnum.LEVEL_4.getLevel());
												fourthTreeListItem.setData("EcoreTreeNode", fourthChildNode);
												fourthTreeListItem.setData("type", DataTypesEnum.CODE.getName());
												fourthTreeListItem.setData("childId", childId);
											});
										}
									});
								}
							}else if("2".equals(typeEnum.getType())) {
								//填充第三级菜单
								fixThirdItem(secondChildNodes, secondTreeListItem, 1, typeEnum, DataTypesEnum.TEXT.getName());
							}else if("3".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.BOOLEAN_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 2, typeEnum, DataTypesEnum.BOOLEAN.getName());
							}else if("4".equals(typeEnum.getType())) {
								//填充第三级菜单 	ImgUtil.INDICATOR_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 3, typeEnum, DataTypesEnum.INDICATOR.getName());
							}else if("5".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.DECIMAL_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 4, typeEnum, DataTypesEnum.DECIMAL.getName());
							}else if("6".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.RATE_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 5, typeEnum, DataTypesEnum.RATE.getName());
							}else if("7".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.AMOUNT_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 6, typeEnum, DataTypesEnum.AMOUNT.getName());
							}else if("8".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.QUANTITY_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 7, typeEnum, DataTypesEnum.QUANTITY.getName());
							}else if("9".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.TIME_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 8, typeEnum, DataTypesEnum.TIME.getName());
							}else if("10".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.BINARY_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 9, typeEnum, DataTypesEnum.BINARY.getName());
							}else if("11".equals(typeEnum.getType())) {
								//填充第三级菜单	 ImgUtil.SCHEMA_TYPES_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 10, typeEnum, DataTypesEnum.SCHEMA_TYPES.getName());
							}else if("12".equals(typeEnum.getType())) {
								//填充第三级菜单	ImgUtil.USER_DEFINED_SUB1
								fixThirdItem(secondChildNodes, secondTreeListItem, 11, typeEnum, DataTypesEnum.USER_DEFINED.getName());
							}else if("13".equals(typeEnum.getType())) {
								secondTreeListItem.dispose();
							}
					}
				}
				
			}else if("2".equals(ecoreTreeNode.getType())) {
				//???
				this.setImage(ImgUtil.createImg(TreeParentEnum.BUSINESS_COMPONENTS.getImgPath()));
				this.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
				
				secondChildNodes.stream().forEach(secondChildNode -> {
					TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean(secondChildNode.getId(), secondChildNode.getName(), ImgUtil.BC, secondChildNode.getType(), secondChildNode.getLevel());
					TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);
					List<EcoreTreeNode> thirdChildNodes = secondChildNode.getChildNodes();
					
					//防止大对象占用内存
					secondChildNode.setChildNodes(null);
					secondTreeListItem.setData("EcoreTreeNode", secondChildNode);
					secondTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
					
					thirdChildNodes.stream().forEach(thirdChildNode -> {
						if("Properties".equals(thirdChildNode.getName())) {
							//第三级菜单
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), ImgUtil.BC_BE, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							//第四级菜单
							List<EcoreTreeNode> fourthChildNodes = thirdChildNode.getChildNodes();
							//防止大对象占用内存
							thirdChildNode.setChildNodes(null);
							thirdChildNode.setId(secondChildNode.getId());
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNode);
							thirdTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
							
							if (fourthChildNodes != null && fourthChildNodes.size() > 0) {
								fourthChildNodes.stream().forEach(fourthChildNode -> {
									TreeListItem fourthTreeListItem = new TreeListItem(thirdTreeListItem, new TreeItemDataBean(fourthChildNode.getId(), fourthChildNode.getName(), ImgUtil.BC_BE, fourthChildNode.getType(), fourthChildNode.getLevel()));
									String childId = fourthChildNode.getId();
									fourthChildNode.setId(secondChildNode.getId());
									fourthTreeListItem.setData("childId", childId);
									fourthTreeListItem.setData("EcoreTreeNode", fourthChildNode);
									fourthTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
								});
							}
						}else if("Sub-Types".equals(thirdChildNode.getName())) {
							//第三级菜单
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), ImgUtil.BC_SUB_TYPES, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							//第四级菜单
							List<EcoreTreeNode> fourthChildNodes = thirdChildNode.getChildNodes();
							//防止大对象占用内存
							thirdChildNode.setChildNodes(null);
							thirdChildNode.setId(secondChildNode.getId());
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNode);
							thirdTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
							
							if (fourthChildNodes != null && fourthChildNodes.size() > 0) {
								fourthChildNodes.stream().forEach(fourthChildNode -> {
									TreeListItem fourthTreeListItem = new TreeListItem(thirdTreeListItem, new TreeItemDataBean(fourthChildNode.getId(), fourthChildNode.getName(), ImgUtil.BC, fourthChildNode.getType(), fourthChildNode.getLevel()));
//									String childId = fourthChildNode.getId();
//									fourthChildNode.setId(secondChildNode.getId());
//									fourthTreeListItem.setData("childId", childId);
									fourthTreeListItem.setData("EcoreTreeNode", fourthChildNode);
									fourthTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
								});
							}
						}else if("Super-Types".equals(thirdChildNode.getName())) {
							//第三级菜单
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), ImgUtil.BC_SUPER_TYPES, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							//第四级菜单
							List<EcoreTreeNode> fourthChildNodes = thirdChildNode.getChildNodes();
							//防止大对象占用内存
							thirdChildNode.setChildNodes(null);
							thirdChildNode.setId(secondChildNode.getId());
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNode);
							thirdTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
							
							if (fourthChildNodes != null && fourthChildNodes.size() > 0) {
								fourthChildNodes.stream().forEach(fourthChildNode -> {
									TreeListItem fourthTreeListItem = new TreeListItem(thirdTreeListItem, new TreeItemDataBean(fourthChildNode.getId(), fourthChildNode.getName(), ImgUtil.BC, fourthChildNode.getType(), fourthChildNode.getLevel()));
//									String childId = fourthChildNode.getId();
//									fourthChildNode.setId(secondChildNode.getId());
//									fourthTreeListItem.setData("childId", childId);
									fourthTreeListItem.setData("EcoreTreeNode", fourthChildNode);
									fourthTreeListItem.setData("type", TreeParentEnum.BUSINESS_COMPONENTS.getName());
								});
							}
						}
					});
				});
			}else if("3".equals(ecoreTreeNode.getType())){
				//自定义元素
				this.setImage(ImgUtil.createImg(TreeParentEnum.EXTERNAL_SCHEMAS.getImgPath()));
				this.setData("type", TreeParentEnum.EXTERNAL_SCHEMAS.getName());
				secondChildNodes.stream().forEach(secondChildNode -> {
					TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean(secondChildNode.getId(), secondChildNode.getName(), ImgUtil.EXTERNAL_SCHEMAS, secondChildNode.getType(), secondChildNode.getLevel());
					TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);
					secondTreeListItem.setData("EcoreTreeNode", secondChildNode);
					secondTreeListItem.setData("type", TreeParentEnum.EXTERNAL_SCHEMAS.getName());
				});
			}else if("4".equals(ecoreTreeNode.getType())) {
				//元素组件
				this.setImage(ImgUtil.createImg(TreeParentEnum.MESSAGE_COMPONENTS.getImgPath()));
				this.setData("type", TreeParentEnum.MESSAGE_COMPONENTS.getName());
				secondChildNodes.stream().forEach(secondChildNode -> {
					String secondImgPath = "";
					String status=secondChildNode.getRegistrationStatus();
					if (status==null||status.equals("Provisionally Registered")||status.equals("Added Registered")) {
						if("1".equals(secondChildNode.getType())) {
						secondImgPath = ImgUtil.MC_SUB3_COMPONENT;
						}else if("2".equals(secondChildNode.getType())){
						secondImgPath = ImgUtil.MC_SUB1_CHOICE1;
						}
					}
					
					else {
						if("1".equals(secondChildNode.getType())) {
						secondImgPath = ImgUtil.MC_SUB1_COMPONENT;
						}else if("2".equals(secondChildNode.getType())){
						secondImgPath = ImgUtil.MC_SUB1_CHOICE;
						}
					}
					//二级菜单
					TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean(secondChildNode.getId(), secondChildNode.getName(), secondImgPath, secondChildNode.getType(), secondChildNode.getLevel());
					TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);
					
					//三级菜单
					List<EcoreTreeNode> thirdChildNodes = secondChildNode.getChildNodes();
					
					//防止大对象占用内存
					secondChildNode.setChildNodes(null);
					secondTreeListItem.setData("EcoreTreeNode", secondChildNode);
					secondTreeListItem.setData("type", TreeParentEnum.MESSAGE_COMPONENTS.getName());
					
					if (thirdChildNodes != null && thirdChildNodes.size() > 0) {
						thirdChildNodes.stream().forEach(thirdChildNode -> {
							String thirdImgPath = "";
							String status1=secondChildNode.getRegistrationStatus();
							if("1".equals(thirdChildNode.getType())) {
								if (status1==null||status1.equals("Provisionally Registered")||status.equals("Added Registered")) {
									thirdImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;	
								}else {
									thirdImgPath = ImgUtil.MC_SUB2_DATA_TYPE;
								}
							}else if("2".equals(thirdChildNode.getType())){
								if (status1==null||status1.equals("Provisionally Registered")||status.equals("Added Registered")) {
									thirdImgPath = ImgUtil.MC_SUB2_COMPONENT1;	
								}else {
									thirdImgPath = ImgUtil.MC_SUB2_COMPONENT;
								}
							}
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), thirdImgPath, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							
							String childId = thirdChildNode.getId();
							thirdTreeListItem.setData("childId", childId);
							thirdChildNode.setId(secondChildNode.getId());
							
							//id传输的是二级菜单的id，方便详细信息展示
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNode);
							thirdTreeListItem.setData("type", TreeParentEnum.MESSAGE_COMPONENTS.getName());
						});
					}
					
				});
			}else if("5".equals(ecoreTreeNode.getType())) {
				//业务域
				this.setImage(ImgUtil.createImg(TreeParentEnum.BUSINESS_AREAS.getImgPath()));
				this.setData("type", TreeParentEnum.BUSINESS_AREAS.getName());
				secondChildNodes.stream().forEach(secondChildNode -> {
					//二级菜单
					TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean(secondChildNode.getId(), secondChildNode.getName(), ImgUtil.BUSINESS_AREAS, secondChildNode.getType(), secondChildNode.getLevel());
					TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);
					
					//三级菜单
					List<EcoreTreeNode> thirdChildNodes = secondChildNode.getChildNodes();
					//防止大对象占用内存
					secondChildNode.setChildNodes(null);
					secondTreeListItem.setData("type", TreeParentEnum.BUSINESS_AREAS.getName());
					secondTreeListItem.setData("EcoreTreeNode", secondChildNode);
					
					if (thirdChildNodes != null && thirdChildNodes.size() > 0) {
						thirdChildNodes.stream().forEach(thirdChildNode -> {
							
							String thirdLevelImgPath = "";
							if (RegistrationStatus.Registered.getStatus().equals(thirdChildNode.getRegistrationStatus())) {
								thirdLevelImgPath = ImgUtil.MD_SUB1;
							} else {
								thirdLevelImgPath = ImgUtil.MESSAGE_DEFINITIONS;
							}
							
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), thirdLevelImgPath, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							
							//四级菜单
							List<EcoreTreeNode> fourthChildNodes = thirdChildNode.getChildNodes();
							
							//防止大对象占用内存
							//thirdChildNode.setChildNodes(null);
							thirdTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS.getName());
							thirdChildNode.setImgPath(thirdLevelImgPath);
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNode);
							if (fourthChildNodes != null && fourthChildNodes.size() > 0) {
								fourthChildNodes.stream().forEach(fourthChildNode -> {
									String fourthLevelImgPath = "";
									if("1".equals(fourthChildNode.getType())) {
										if (RegistrationStatus.Registered.getStatus().equals(thirdChildNode.getRegistrationStatus())) {
											fourthLevelImgPath = ImgUtil.MD_SUB2_DATA_TYPE;
										} else {
											fourthLevelImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
										}
									}else if("2".equals(fourthChildNode.getType())) {
										if (RegistrationStatus.Registered.getStatus().equals(thirdChildNode.getRegistrationStatus())) {
											fourthLevelImgPath = ImgUtil.MD_SUB2_COMPONENT;
										} else {
											fourthLevelImgPath = ImgUtil.ME;
										}
									}else { // 没有指定Message Component, 也没有指定DataType的Message Building Block
										fourthLevelImgPath = ImgUtil.ME;
									}
									TreeItemDataBean fourthTreeItemDataBean = new TreeItemDataBean(fourthChildNode.getId(), fourthChildNode.getName(), fourthLevelImgPath, fourthChildNode.getType(), fourthChildNode.getLevel());
									TreeListItem fourthTreeListItem = new TreeListItem(thirdTreeListItem, fourthTreeItemDataBean);
									
									String childId = fourthChildNode.getId();
									fourthTreeListItem.setData("childId", childId);
									EcoreTreeNode fourthChildNodeCp = new EcoreTreeNode();
									fourthChildNodeCp.setId(thirdChildNode.getId());
									fourthChildNodeCp.setImgPath(thirdChildNode.getImgPath());
									fourthChildNodeCp.setLevel(fourthChildNode.getLevel());
									fourthChildNodeCp.setName(fourthChildNode.getName());
									fourthChildNodeCp.setType(fourthChildNode.getType());
									fourthChildNodeCp.setObjType(fourthChildNode.getObjType());
									
									fourthTreeListItem.setData("EcoreTreeNode", fourthChildNodeCp);
									fourthTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
								});
							}
						});
					}
				});
			}else if("6".equals(ecoreTreeNode.getType())) {
				//报文集
				this.setImage(ImgUtil.createImg(TreeParentEnum.MESSAGE_SETS.getImgPath()));
				this.setData("type", TreeParentEnum.MESSAGE_SETS.getName());
				secondChildNodes.stream().forEach(secondChildNode -> {
					//二级菜单
					TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean(secondChildNode.getId(), secondChildNode.getName(), ImgUtil.MS_SUB1, secondChildNode.getType(), secondChildNode.getLevel());
					TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);
					
					//三级菜单
					List<EcoreTreeNode> thirdChildNodes = secondChildNode.getChildNodes();
					
					//防止大对象占用内存
					secondChildNode.setChildNodes(null);
					secondTreeListItem.setData("type", TreeParentEnum.MESSAGE_SETS.getName());
					secondTreeListItem.setData("EcoreTreeNode", secondChildNode);
					if (thirdChildNodes != null && thirdChildNodes.size() > 0) {
						thirdChildNodes.stream().forEach(thirdChildNode -> {
							
							String thirdLevelImgPath = "";
							if (RegistrationStatus.Registered.getStatus().equals(thirdChildNode.getRegistrationStatus())) {
								thirdLevelImgPath = ImgUtil.MD_SUB1;
							} else {
								thirdLevelImgPath = ImgUtil.MESSAGE_DEFINITIONS;
							}
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), thirdLevelImgPath, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							
							//四级菜单
							List<EcoreTreeNode> fourthChildNodes = thirdChildNode.getChildNodes();
							
							//防止大对象占用内存
							//thirdChildNode.setChildNodes(null);
							thirdTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS.getName());
							thirdChildNode.setImgPath(thirdLevelImgPath);
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNode);
							
							if (fourthChildNodes != null && fourthChildNodes.size() > 0) {
								fourthChildNodes.stream().forEach(fourthChildNode -> {
									String fourthLevelImgPath = "";
									if("1".equals(fourthChildNode.getType())) {
										if (RegistrationStatus.Registered.getStatus().equals(thirdChildNode.getRegistrationStatus())) {
											fourthLevelImgPath = ImgUtil.MD_SUB2_DATA_TYPE;
										} else {
											fourthLevelImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
										}
									}else if("2".equals(fourthChildNode.getType())) {
										if (RegistrationStatus.Registered.getStatus().equals(thirdChildNode.getRegistrationStatus())) {
											fourthLevelImgPath = ImgUtil.MD_SUB2_COMPONENT;
										} else {
											fourthLevelImgPath = ImgUtil.ME;
										}
									}else { // 没有指定Message Component, 也没有指定DataType的Message Building Block
										fourthLevelImgPath = ImgUtil.ME;
									}
									
									TreeItemDataBean fourthTreeItemDataBean = new TreeItemDataBean(fourthChildNode.getId(), fourthChildNode.getName(), fourthLevelImgPath, fourthChildNode.getType(), fourthChildNode.getLevel());
									TreeListItem fourthTreeListItem = new TreeListItem(thirdTreeListItem, fourthTreeItemDataBean);
									
									String childId = fourthChildNode.getId();
									fourthTreeListItem.setData("childId", childId);
									EcoreTreeNode fourthChildNodeCp = new EcoreTreeNode();
									fourthChildNodeCp.setId(thirdChildNode.getId());
									fourthChildNodeCp.setImgPath(thirdChildNode.getImgPath());
									fourthChildNodeCp.setLevel(fourthChildNode.getLevel());
									fourthChildNodeCp.setName(fourthChildNode.getName());
									fourthChildNodeCp.setType(fourthChildNode.getType());
									fourthChildNodeCp.setObjType(fourthChildNode.getObjType());
									
									fourthTreeListItem.setData("EcoreTreeNode", fourthChildNodeCp);
									fourthTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
								});
							}
						});
					}
				});
			}else if("7".equals(ecoreTreeNode.getType())) {
				//报文
				this.setImage(ImgUtil.createImg(TreeParentEnum.MESSAGE_DEFINITIONS.getImgPath()));
				this.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS.getName());
				
				secondChildNodes.stream().forEach(secondChildNode -> {
					String secondLevelImgPath = "";
					//二级菜单
					if (RegistrationStatus.Registered.getStatus().equals(secondChildNode.getRegistrationStatus())) {
						secondLevelImgPath = ImgUtil.MD_SUB1;
					} else {
						secondLevelImgPath = ImgUtil.MESSAGE_DEFINITIONS;
					}

					TreeItemDataBean secondTreeItemDataBean = new TreeItemDataBean(secondChildNode.getId(), secondChildNode.getName(), secondLevelImgPath, secondChildNode.getType(), secondChildNode.getLevel());
					TreeListItem secondTreeListItem = new TreeListItem(this, secondTreeItemDataBean);
					
					//三级菜单
					List<EcoreTreeNode> thirdChildNodes = secondChildNode.getChildNodes();
					
					//防止大对象占用内存
					secondChildNode.setChildNodes(null);
					secondChildNode.setImgPath(secondLevelImgPath);
					secondTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS.getName());
					secondTreeListItem.setData("EcoreTreeNode", secondChildNode);
					
					if (thirdChildNodes != null && thirdChildNodes.size() > 0) {
						thirdChildNodes.stream().forEach(thirdChildNode -> {
							String thirdLevelImgPath = "";
							if("1".equals(thirdChildNode.getType())) {
								if (RegistrationStatus.Registered.getStatus().equals(secondChildNode.getRegistrationStatus())) {
									thirdLevelImgPath = ImgUtil.MD_SUB2_DATA_TYPE;
								} else {
									thirdLevelImgPath = ImgUtil.MD_SUB2_DATA_TYPE_WITHOUTLOCK;
								}
							}else if("2".equals(thirdChildNode.getType())) {
								if (RegistrationStatus.Registered.getStatus().equals(secondChildNode.getRegistrationStatus())) {
									thirdLevelImgPath = ImgUtil.MD_SUB2_COMPONENT;
								} else {
									thirdLevelImgPath = ImgUtil.ME;
								}
							}else { // 没有指定Message Component, 也没有指定DataType的Message Building Block
								thirdLevelImgPath = ImgUtil.ME;
							}
							TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdChildNode.getId(), thirdChildNode.getName(), thirdLevelImgPath, thirdChildNode.getType(), thirdChildNode.getLevel());
							TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
							
							String childId = thirdChildNode.getId();
							thirdTreeListItem.setData("childId", childId);
							EcoreTreeNode thirdChildNodeCp = new EcoreTreeNode();
							thirdChildNodeCp.setId(secondChildNode.getId());
							thirdChildNodeCp.setImgPath(secondChildNode.getImgPath());
							thirdChildNodeCp.setLevel(thirdChildNode.getLevel());
							thirdChildNodeCp.setName(thirdChildNode.getName());
							thirdChildNodeCp.setType(thirdChildNode.getType());
							thirdChildNodeCp.setObjType(thirdChildNode.getObjType());
							
							thirdTreeListItem.setData("EcoreTreeNode", thirdChildNodeCp);
							thirdTreeListItem.setData("type", TreeParentEnum.MESSAGE_DEFINITIONS_CHILD.getName());
						});
					}
				});
			}else if("8".equals(ecoreTreeNode.getType())) {
				this.setImage(ImgUtil.createImg(TreeParentEnum.VIEW_IDENTICAL_COMPONENTS.getImgPath()));
				this.setData("type", TreeParentEnum.VIEW_IDENTICAL_COMPONENTS.getName());
			}
		}
	}
	
	/**
	 * 填充第三级菜单
	 * @param secondChildNodes
	 * @param secondTreeListItem
	 * @param index
	 * @param imgPath
	 * @Param type 当前属于哪个层级
	 */
	public void fixThirdItem(List<EcoreTreeNode> secondChildNodes, TreeListItem secondTreeListItem, int index, DataTypesEnum dataTypeEnum, String type) {
		List<EcoreTreeNode> thirdEcoreTreeNodes = secondChildNodes.get(index).getChildNodes();
		//防止大对象占用内存
		secondChildNodes.get(index).setChildNodes(null);
		
		if(thirdEcoreTreeNodes != null && thirdEcoreTreeNodes.size() > 0) {
			thirdEcoreTreeNodes.stream().forEach(thirdEcoreTreeNode ->{
				String imgPath = "";
				if ("2".equals(dataTypeEnum.getType())) { // Code Set
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.TEXT_SUB1;
					} else {
						imgPath = ImgUtil.TEXT_SUB1_WITHOUT_LOCK;
					}
				} else if ("3".equals(dataTypeEnum.getType())) { // Boolean
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.BOOLEAN_SUB1_LOCK;
					} else {
						imgPath = ImgUtil.BOOLEAN_SUB1;
					}
				} else if ("4".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.INDICATOR_SUB1;
					} else {
						imgPath = ImgUtil.INDICATOR_SUB1_WITHOUT_LOCK;
					}
				} else if ("5".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) { // Decimal
						imgPath = ImgUtil.DECIMAL_SUB1_LOCK;
					} else {
						imgPath = ImgUtil.DECIMAL_SUB1;
					}
				} else if ("6".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.RATE_SUB1;
					} else {
						imgPath = ImgUtil.RATE_SUB1_WITHOUT_LOCK;
					}
				} else if ("7".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.AMOUNT_SUB1;
					} else {
						imgPath = ImgUtil.AMOUNT_SUB1_WITHOUT_LOCK;
					}
				} else if ("8".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.QUANTITY_SUB1;
					} else {
						imgPath = ImgUtil.QUANTITY_SUB1_WITHOUT_LOCK;
					}
				} else if ("9".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.TIME_SUB1;
					} else {
						imgPath = ImgUtil.TIME_SUB1_WITHOUT_LOCK;
					}
				} else if ("10".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.BINARY_SUB1;
					} else {
						imgPath = ImgUtil.BINARY_SUB1_WITHOUT_LOCK;
					}
				} else if ("11".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.SCHEMA_TYPES_SUB1_LOCK;
					} else {
						imgPath = ImgUtil.SCHEMA_TYPES_SUB1;
					}
				} else if ("12".equals(dataTypeEnum.getType())) {
					if ("Registered".equals(thirdEcoreTreeNode.getRegistrationStatus())) {
						imgPath = ImgUtil.USER_DEFINED_SUB1_LOCK;
					} else {
						imgPath = ImgUtil.USER_DEFINED_SUB1;
					}
				}
				TreeItemDataBean thirdTreeItemDataBean = new TreeItemDataBean(thirdEcoreTreeNode.getId(), thirdEcoreTreeNode.getName(), imgPath, thirdEcoreTreeNode.getType(), thirdEcoreTreeNode.getLevel());
				TreeListItem thirdTreeListItem = new TreeListItem(secondTreeListItem, thirdTreeItemDataBean);
				//防止大对象占用内存
				thirdTreeListItem.setData("EcoreTreeNode", thirdEcoreTreeNode);
				thirdTreeListItem.setData("type", type);
			});
		}
	}
	
	/**
	 * 子节点
	 * @param parent
	 * @param treeItemDataBean
	 */
	public TreeListItem(TreeListItem parent, TreeItemDataBean currentTreeItemDataBean) {
		super(parent, SWT.NULL);
		this.setText(currentTreeItemDataBean.getName() + "");
		this.setImage(ImgUtil.createImg(currentTreeItemDataBean.getImage()));
	}
	
	/**
	 * 子节点
	 * @param parent
	 * @param treeItemDataBean
	 */
	public TreeListItem(TreeListItem parent, TreeItemDataBean currentTreeItemDataBean, int index) {
		super(parent, SWT.NULL, index);
		this.setText(currentTreeItemDataBean.getName() + "");
		this.setImage(ImgUtil.createImg(currentTreeItemDataBean.getImage()));
	}
}
