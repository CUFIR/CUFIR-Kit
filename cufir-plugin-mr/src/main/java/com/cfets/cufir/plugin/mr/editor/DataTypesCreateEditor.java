package com.cfets.cufir.plugin.mr.editor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.ObjTypeEnum;
import com.cfets.cufir.plugin.mr.enums.RegistrationStatus;
import com.cfets.cufir.plugin.mr.enums.TreeLevelEnum;
import com.cfets.cufir.plugin.mr.handlers.SaveHandler;
import com.cfets.cufir.plugin.mr.utils.ContextServiceUtil;
import com.cfets.cufir.plugin.mr.utils.DerbyDaoUtil;
import com.cfets.cufir.plugin.mr.utils.GenerateCommonTree;
import com.cfets.cufir.plugin.mr.utils.GenerateTableForMCandMDandMS;
import com.cfets.cufir.plugin.mr.utils.ImgUtil;
import com.cfets.cufir.plugin.mr.utils.MessageBoxUtil;
import com.cfets.cufir.plugin.mr.utils.NumberFormatUtil;
import com.cfets.cufir.plugin.mr.utils.SystemUtil;
import com.cfets.cufir.s.data.bean.EcoreBusinessComponent;
import com.cfets.cufir.s.data.bean.EcoreCode;
import com.cfets.cufir.s.data.bean.EcoreConstraint;
import com.cfets.cufir.s.data.bean.EcoreDataType;
import com.cfets.cufir.s.data.bean.EcoreExample;
import com.cfets.cufir.s.data.bean.EcoreMessageComponent;
import com.cfets.cufir.s.data.bean.EcoreMessageDefinition;
import com.cfets.cufir.s.data.bean.EcoreMessageSet;
import com.cfets.cufir.s.data.dao.EcoreDataTypeDao;
import com.cfets.cufir.s.data.dao.impl.EcoreDataTypeDaoImpl;
import com.cfets.cufir.s.data.vo.EcoreCodeVO;
import com.cfets.cufir.s.data.vo.EcoreDataTypeVO;
import com.cfets.cufir.s.data.vo.EcoreTreeNode;

/**
 * DataType 编辑和显示
 * @author jiangqiming_het
 *
 */

public class DataTypesCreateEditor extends MultiPageEditorParent {

	private MyEditorInput myEditorInput;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private EcoreDataTypeDao ecoreDataTypeDao = new EcoreDataTypeDaoImpl();
	EcoreDataTypeVO ecoreDataTypeVO;

	// 基本数据类型
	private String dataType;
	// -----------For Summary Page -----------
	private Text nameText, documentationText, minLengthText, maxLengthText, lengthText, patternText, summaryRemovalDate,
			identificationSchemeText, meaningWhenTrueText, meaningWhenFalseText, minInclusiveText, minExclusiveText,
			maxInclusiveText, maxExclusiveText, totalDigitsText, fractionDigitsText, baseValueText, baseUnitCodeText, namespaceListText;
	private Combo namespaceCombo, processContentCombo;
	private Text objectIdentifierValue;
	private ComboViewer statusComboViewer;
	private List exampleList;

	private Tree newCodeTree;

	private Table summaryConstraintsTable;

	private Table contentConstraintsTable;

	private TreeItem selectedTreeItem;
	
	private TreeItem modelExploreTreeItem;

	private Text contentNameText, contentDocText, contentCodeNameText, contentExpressionText,
			contentExpressionLanguageText, contentObjectIdentifierValue, contentRemovalDate;
	
	private Combo contentStatusCombo;
	
	private String ecoreDataTypeId;
	
	private String traceId;
	
	//为展示数据
	// Use for Impact Page
	private ArrayList<EcoreMessageComponent> ecoreMessageComponentList = new ArrayList<>();
	// Use for Impact Page
	private ArrayList<EcoreMessageDefinition> msgDefinitionList = new ArrayList<>();
	// Use for Impact Page
	private ArrayList<EcoreMessageSet> msgSetList = new ArrayList<>();
	// Use for Incoming Association Page
	private ArrayList<EcoreBusinessComponent> imcomingAssociationList = new ArrayList<>();
	// Use for Incoming Association Page
	private EcoreDataType previousEcoreDataType = new EcoreDataType();
	// Use for Incoming Association Page
	private ArrayList<EcoreDataType> nextDataTypeList = new ArrayList<>();

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartProperty("customName", "dataTypesCreate");
		this.myEditorInput = (MyEditorInput) input;

		modelExploreTreeItem = this.myEditorInput.getTransferDataBean().getTreeListItem();
		this.dataType = this.myEditorInput.getTransferDataBean().getType();
		
		// 设置标题图片
		this.setTitleImage(ImgUtil.createImg(this.myEditorInput.getTransferDataBean().getImgPath()));
		//ctrl + s保存必须要
		ContextServiceUtil.setContext(site);
		
		this.ecoreDataTypeId = this.myEditorInput.getTransferDataBean().getId();

		this.setPartProperty("ID", this.ecoreDataTypeId);
		
		//加载数据
		loadDataTypeData();
		

	}
	
	private void loadDataTypeData() {
		try {
			ecoreDataTypeVO = ecoreDataTypeDao.findDataTypeVO(ecoreDataTypeId);
			
			if(ecoreDataTypeVO != null) {
				ecoreMessageComponentList = DerbyDaoUtil.getMessageComponentByDataTypeId(ecoreDataTypeId);
				msgDefinitionList = DerbyDaoUtil.getMessageDefinitionList(ecoreMessageComponentList);
				msgSetList = DerbyDaoUtil.getMessageSetList(msgDefinitionList);
				imcomingAssociationList = DerbyDaoUtil.getIncomingAssociationsListForDataType(ecoreDataTypeId);
				previousEcoreDataType = DerbyDaoUtil.getDataTypePreviousVersion(ecoreDataTypeVO.getEcoreDataType().getPreviousVersion());
				nextDataTypeList = DerbyDaoUtil.getDataTypeListByIds(DerbyDaoUtil.getNextVersionIds(ecoreDataTypeId));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ecoreDataTypeVO = new EcoreDataTypeVO();
	}

	@Override
	protected void createPages() {
		
		createSummaryPage();
		
		if (this.dataType.equals(DataTypesEnum.CODE_SETS.getName())) {
			traceId = ecoreDataTypeVO.getEcoreDataType() != null? ecoreDataTypeVO.getEcoreDataType().getTrace(): null;
			createContentPageForCodeSets();
		} else {
			createContentPageForOthers();
		}

		createImpactPage();
		createIncomingAssociationsPage();
		createVersionSubset();
		
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			if (RegistrationStatus.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
				if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是 可衍生的: 可编辑
					this.setDirty(true);
				} else {
					this.setDirty(false);
				}
			} else { // 临时注册和新增可以编辑
				this.setDirty(true);
			}
			this.setPartName(ecoreDataTypeVO.getEcoreDataType().getName());
		} else {
			this.setDirty(true);
			this.setPartName("");
		}
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SaveHandler sh=new SaveHandler();
		try {
			sh.execute(null);
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	void createSummaryPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		composite.setLayout(new FillLayout());
		int index = addPage(scrolledComposite);
		setPageText(index, "Summary");

		if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatus.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
			if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是 可衍生的: 那么可编辑
				composite.setEnabled(true);
			} else {
				composite.setEnabled(false);
			}
		}
		
		// ================= General Information Group Begin =======================
		ExpandBar summaryBar = new ExpandBar(composite, SWT.NONE);
		summaryBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		summaryBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		summaryBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));

		Composite generalInformationComposite1 = new Composite(summaryBar, SWT.NONE);
		generalInformationComposite1.setLayout(new FillLayout());
		generalInformationComposite1.setLayoutData(new GridData(800, 500));
		
		ScrolledComposite scrolledComposite2 = new ScrolledComposite(generalInformationComposite1, SWT.V_SCROLL);
		scrolledComposite2.setExpandHorizontal(true);
		scrolledComposite2.setExpandVertical(true);
		
		Composite generalInformationComposite = new Composite(scrolledComposite2, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));
		generalInformationComposite.setLayoutData(new GridData(800, 500));
		scrolledComposite2.setContent(generalInformationComposite);
		
		Label nameLabel = new Label(generalInformationComposite, SWT.NONE);
		nameLabel.setText("Name");
		GridData nameLabelGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		nameLabelGridData.widthHint = 200;
		nameLabel.setLayoutData(nameLabelGridData);

		nameText = new Text(generalInformationComposite, SWT.BORDER);
		GridData nameValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		nameValueGridData.widthHint = 500;
		nameText.setLayoutData(nameValueGridData);
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			nameText.setText(ecoreDataTypeVO.getEcoreDataType().getName()==null?"":ecoreDataTypeVO.getEcoreDataType().getName());
		}
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setPartName(nameText.getText());
				//左边的树的名称展示
				EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)modelExploreTreeItem.getData("EcoreTreeNode");
				String type = (String)modelExploreTreeItem.getData("type");
				if (DataTypesEnum.checkTypeName(type)) {
					if (TreeLevelEnum.LEVEL_3.getLevel().equals(ecoreTreeNode.getLevel())) {
						modelExploreTreeItem.setText(nameText.getText());
						setDirty(true);
					} else {
						modelExploreTreeItem.getParentItem().setText(nameText.getText());
					}
				}

			}
		});

		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setLayoutData(nameLabelGridData);
		documentationLabel.setText("Documentation");
		documentationText = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
		GridData docValueGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		docValueGridData.widthHint = 500;
		docValueGridData.heightHint = 150;
		documentationText.setLayoutData(docValueGridData);
		documentationText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			documentationText.setText(ecoreDataTypeVO.getEcoreDataType().getDefinition()==null?"":ecoreDataTypeVO.getEcoreDataType().getDefinition());
		}

		if (dataType.equals(DataTypesEnum.CODE_SETS.getName()) || dataType.equals(DataTypesEnum.TEXT.getName())|| dataType.equals(DataTypesEnum.BINARY.getName())) {
			Label minLengthLabel = new Label(generalInformationComposite, SWT.NONE);
			minLengthLabel.setLayoutData(nameLabelGridData);
			minLengthLabel.setText("Min Length");

			minLengthText = new Text(generalInformationComposite, SWT.BORDER);
			minLengthText.setLayoutData(nameValueGridData);
			minLengthText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				minLengthText.setText(ecoreDataTypeVO.getEcoreDataType().getMinLength() == null ? "" : String.valueOf(ecoreDataTypeVO.getEcoreDataType().getMinLength()));
			}

			Label maxLengthLabel = new Label(generalInformationComposite, SWT.NONE);
			maxLengthLabel.setLayoutData(nameLabelGridData);
			maxLengthLabel.setText("Max Length");

			maxLengthText = new Text(generalInformationComposite, SWT.BORDER);
			maxLengthText.setLayoutData(nameValueGridData);
			maxLengthText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				maxLengthText.setText(ecoreDataTypeVO.getEcoreDataType().getMaxLength() == null ? "" : String.valueOf(ecoreDataTypeVO.getEcoreDataType().getMaxLength()));
			}

			Label lengthLabel = new Label(generalInformationComposite, SWT.NONE);
			lengthLabel.setLayoutData(nameLabelGridData);
			lengthLabel.setText("Length");

			lengthText = new Text(generalInformationComposite, SWT.BORDER);
			lengthText.setLayoutData(nameValueGridData);
			lengthText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				lengthText.setText(ecoreDataTypeVO.getEcoreDataType().getLength() == null ? "" : String.valueOf(ecoreDataTypeVO.getEcoreDataType().getLength()));
			}
		}

		if (dataType.equals(DataTypesEnum.INDICATOR.getName())) {
			Label meaningWhenTrueLabel = new Label(generalInformationComposite, SWT.NONE);
			meaningWhenTrueLabel.setLayoutData(nameLabelGridData);
			meaningWhenTrueLabel.setText("Meaning When True");

			meaningWhenTrueText = new Text(generalInformationComposite, SWT.BORDER);
			meaningWhenTrueText.setLayoutData(nameValueGridData);
			meaningWhenTrueText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				meaningWhenTrueText.setText(ecoreDataTypeVO.getEcoreDataType().getMeaningWhenTrue()==null?"":ecoreDataTypeVO.getEcoreDataType().getMeaningWhenTrue());
			}

			Label meaningWhenFalseLabel = new Label(generalInformationComposite, SWT.NONE);
			meaningWhenFalseLabel.setLayoutData(nameLabelGridData);
			meaningWhenFalseLabel.setText("Meaning When False");

			meaningWhenFalseText = new Text(generalInformationComposite, SWT.BORDER);
			meaningWhenFalseText.setLayoutData(nameValueGridData);
			meaningWhenFalseText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				meaningWhenFalseText.setText(ecoreDataTypeVO.getEcoreDataType().getMeaningWhenFalse()==null?"":ecoreDataTypeVO.getEcoreDataType().getMeaningWhenFalse());
			}
		}

		if (dataType.equals(DataTypesEnum.DECIMAL.getName()) || dataType.equals(DataTypesEnum.RATE.getName())
				|| dataType.equals(DataTypesEnum.AMOUNT.getName()) || dataType.equals(DataTypesEnum.QUANTITY.getName())
				|| dataType.equals(DataTypesEnum.TIME.getName())) {
			Label minInclusiveLabel = new Label(generalInformationComposite, SWT.NONE);
			minInclusiveLabel.setLayoutData(nameLabelGridData);
			minInclusiveLabel.setText("Min Inclusive");
			minInclusiveText = new Text(generalInformationComposite, SWT.BORDER);
			minInclusiveText.setLayoutData(nameValueGridData);
			minInclusiveText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				minInclusiveText.setText(ecoreDataTypeVO.getEcoreDataType().getMinInclusive()==null?"":ecoreDataTypeVO.getEcoreDataType().getMinInclusive());
			}

			Label minExclusiveLabel = new Label(generalInformationComposite, SWT.NONE);
			minExclusiveLabel.setLayoutData(nameLabelGridData);
			minExclusiveLabel.setText("Min Exclusive");
			minExclusiveText = new Text(generalInformationComposite, SWT.BORDER);
			minExclusiveText.setLayoutData(nameValueGridData);
			minExclusiveText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				minExclusiveText.setText(ecoreDataTypeVO.getEcoreDataType().getMinExclusive()==null?"":ecoreDataTypeVO.getEcoreDataType().getMinExclusive());
			}
			
			Label maxInclusiveLabel = new Label(generalInformationComposite, SWT.NONE);
			maxInclusiveLabel.setLayoutData(nameLabelGridData);
			maxInclusiveLabel.setText("Max Inclusive");
			maxInclusiveText = new Text(generalInformationComposite, SWT.BORDER);
			maxInclusiveText.setLayoutData(nameValueGridData);
			maxInclusiveText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				maxInclusiveText.setText(ecoreDataTypeVO.getEcoreDataType().getMaxInclusive()==null?"":ecoreDataTypeVO.getEcoreDataType().getMaxInclusive());
			}

			Label maxExclusiveLabel = new Label(generalInformationComposite, SWT.NONE);
			maxExclusiveLabel.setLayoutData(nameLabelGridData);
			maxExclusiveLabel.setText("Max Exclusive");
			maxExclusiveText = new Text(generalInformationComposite, SWT.BORDER);
			maxExclusiveText.setLayoutData(nameValueGridData);
			maxExclusiveText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				maxExclusiveText.setText(ecoreDataTypeVO.getEcoreDataType().getMaxExclusive()==null?"":ecoreDataTypeVO.getEcoreDataType().getMaxExclusive());
			}
			
			if(!dataType.equals(DataTypesEnum.TIME.getName())) {
				Label totalDigitsLabel = new Label(generalInformationComposite, SWT.NONE);
				totalDigitsLabel.setLayoutData(nameLabelGridData);
				totalDigitsLabel.setText("Total Digits");
				totalDigitsText = new Text(generalInformationComposite, SWT.BORDER);
				totalDigitsText.setLayoutData(nameValueGridData);
				totalDigitsText.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						setDirty(true);
					}
				});
				
				if(ecoreDataTypeVO.getEcoreDataType() != null) {
					totalDigitsText.setText(ecoreDataTypeVO.getEcoreDataType().getTotalDigits() == null ? "" : ""+ecoreDataTypeVO.getEcoreDataType().getTotalDigits());
				}
				
				Label fractionDigitsLabel = new Label(generalInformationComposite, SWT.NONE);
				fractionDigitsLabel.setLayoutData(nameLabelGridData);
				fractionDigitsLabel.setText("Fraction Digits");
				fractionDigitsText = new Text(generalInformationComposite, SWT.BORDER);
				fractionDigitsText.setLayoutData(nameValueGridData);
				fractionDigitsText.addModifyListener(new ModifyListener() {
					@Override
					public void modifyText(ModifyEvent e) {
						setDirty(true);
					}
				});
				
				if(ecoreDataTypeVO.getEcoreDataType() != null) {
					fractionDigitsText.setText(ecoreDataTypeVO.getEcoreDataType().getFractionDigits() == null ? "" : ""+ecoreDataTypeVO.getEcoreDataType().getFractionDigits());
				}
			}
		}
		
		if(dataType.equals(DataTypesEnum.USER_DEFINED.getName())){
			Label namespaceLabel = new Label(generalInformationComposite, SWT.NONE);
			namespaceLabel.setLayoutData(nameLabelGridData);
			namespaceLabel.setText("Namespace");
			namespaceCombo = new Combo(generalInformationComposite, SWT.BORDER);
			namespaceCombo.add("##any");
			namespaceCombo.add("##other");
			namespaceCombo.add("list");
			namespaceCombo.setLayoutData(nameLabelGridData);
			namespaceCombo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				namespaceCombo.setText(ecoreDataTypeVO.getEcoreDataType().getNamespace()==null?"":ecoreDataTypeVO.getEcoreDataType().getNamespace());
			}
			
			Label nameSpaceListLabel = new Label(generalInformationComposite, SWT.NONE);
			nameSpaceListLabel.setLayoutData(nameLabelGridData);
			nameSpaceListLabel.setText("Namespace List");
			namespaceListText = new Text(generalInformationComposite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
			namespaceListText.setLayoutData(docValueGridData);
			namespaceListText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				namespaceListText.setText(ecoreDataTypeVO.getEcoreDataType().getNamespaceList()==null?"":ecoreDataTypeVO.getEcoreDataType().getNamespaceList());
			}
			
			Label processContentLabel = new Label(generalInformationComposite, SWT.NONE);
			processContentLabel.setLayoutData(nameLabelGridData);
			processContentLabel.setText("Process Content");
			processContentCombo = new Combo(generalInformationComposite, SWT.BORDER);
			processContentCombo.add("lax");
			processContentCombo.add("skip");
			processContentCombo.add("strict");
			processContentCombo.setLayoutData(nameLabelGridData);
			processContentCombo.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				processContentCombo.setText(ecoreDataTypeVO.getEcoreDataType().getProcessContents()==null?"":ecoreDataTypeVO.getEcoreDataType().getProcessContents());
			}
		}
		
		if(!dataType.equals(DataTypesEnum.USER_DEFINED.getName())){
			Label patternLabel = new Label(generalInformationComposite, SWT.NONE);
			patternLabel.setLayoutData(nameLabelGridData);
			patternLabel.setText("Pattern");
			patternText = new Text(generalInformationComposite, SWT.BORDER);
			patternText.setLayoutData(nameValueGridData);
			patternText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				patternText.setText(ecoreDataTypeVO.getEcoreDataType().getPattern()==null?"":ecoreDataTypeVO.getEcoreDataType().getPattern());
			}
		}

		if (dataType.equals(DataTypesEnum.CODE_SETS.getName()) || dataType.equals(DataTypesEnum.TEXT.getName())) {
			Label identificationSchemeLabel = new Label(generalInformationComposite, SWT.NONE);
			identificationSchemeLabel.setLayoutData(nameLabelGridData);
			identificationSchemeLabel.setText("Identification Scheme");

			identificationSchemeText = new Text(generalInformationComposite, SWT.BORDER);
			identificationSchemeText.setLayoutData(nameValueGridData);
			if(dataType.equals(DataTypesEnum.TEXT.getName())){
				identificationSchemeText.setMessage("This entry is required if it is of type Identifier.");
			}
			identificationSchemeText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				identificationSchemeText.setText(ecoreDataTypeVO.getEcoreDataType().getIdentificationScheme()==null?"":ecoreDataTypeVO.getEcoreDataType().getIdentificationScheme());
			}
		}

		if (dataType.equals(DataTypesEnum.RATE.getName())) {
			Label baseValueLabel = new Label(generalInformationComposite, SWT.NONE);
			baseValueLabel.setLayoutData(nameLabelGridData);
			baseValueLabel.setText("Base Value");
			baseValueText = new Text(generalInformationComposite, SWT.BORDER);
			baseValueText.setLayoutData(nameValueGridData);
			baseValueText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				baseValueText.setText(ecoreDataTypeVO.getEcoreDataType().getBaseValue() == null ? "" : "" + ecoreDataTypeVO.getEcoreDataType().getBaseValue());
			}

			Label baseUnitCodeLabel = new Label(generalInformationComposite, SWT.NONE);
			baseUnitCodeLabel.setLayoutData(nameLabelGridData);
			baseUnitCodeLabel.setText("Base Unit Code");
			baseUnitCodeText = new Text(generalInformationComposite, SWT.BORDER);
			baseUnitCodeText.setLayoutData(nameValueGridData);
			baseUnitCodeText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					setDirty(true);
				}
			});
			if(ecoreDataTypeVO.getEcoreDataType() != null) {
				baseUnitCodeText.setText(ecoreDataTypeVO.getEcoreDataType().getBaseUnitCode()==null?"":ecoreDataTypeVO.getEcoreDataType().getBaseUnitCode());
			}
		}

		ExpandItem generalInformationItem = new ExpandItem(summaryBar, SWT.NONE);
		generalInformationItem.setText("General Information");
		generalInformationItem.setHeight(350);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite1);
		
		scrolledComposite2.setMinSize(generalInformationComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// ================= General Information Group End ============================

		// ================= CMP Information Group Begin ==============================
		Composite cmpComposite = new Composite(summaryBar, SWT.NONE);
		cmpComposite.setLayout(new GridLayout(2, false));

		Label objectIdentifierLabel = new Label(cmpComposite, SWT.NONE);
		objectIdentifierLabel.setLayoutData(nameLabelGridData);
		objectIdentifierLabel.setText("Object Identifier");
		objectIdentifierValue = new Text(cmpComposite, SWT.BORDER);
		objectIdentifierValue.setLayoutData(nameValueGridData);
		objectIdentifierValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			objectIdentifierValue.setText(ecoreDataTypeVO.getEcoreDataType().getObjectIdentifier()==null?"":ecoreDataTypeVO.getEcoreDataType().getObjectIdentifier());
		}

		ExpandItem cmpItem = new ExpandItem(summaryBar, SWT.NONE);
		cmpItem.setText("CMP Information");
		cmpItem.setHeight(80);
		cmpItem.setExpanded(true);
		cmpItem.setControl(cmpComposite);
		// ================= CMP Information Group End ==============================

		// ===== Registration Information Group Begin ===============================
		Composite registrationComposite = new Composite(summaryBar, SWT.NONE);
		registrationComposite.setLayout(new GridLayout(2, false));

		Label registrationStatusLabel = new Label(registrationComposite, SWT.NONE);
		registrationStatusLabel.setLayoutData(nameLabelGridData);
		registrationStatusLabel.setText("Registration Status");

		GridData valueGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		valueGridData.widthHint = 150;
		statusComboViewer = new ComboViewer(registrationComposite, SWT.NONE);
		statusComboViewer.add(RegistrationStatus.Registered.getStatus());
		statusComboViewer.add(RegistrationStatus.Provisionally.getStatus());
		statusComboViewer.add(RegistrationStatus.Added.getStatus());
		Combo statusCombo = statusComboViewer.getCombo();
		statusCombo.setLayoutData(valueGridData);
		statusCombo.setEnabled(false);
		statusCombo.select(2);
		statusCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			statusCombo.setText(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus()==null?"":ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus());
		}
		
		Label removalDateLabel = new Label(registrationComposite, SWT.NONE);
		removalDateLabel.setText("Removal Date");
		summaryRemovalDate = new Text(registrationComposite, SWT.BORDER);
		summaryRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
		summaryRemovalDate.setLayoutData(valueGridData);
		summaryRemovalDate.setEnabled(false);
		summaryRemovalDate.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				setDirty(true);
			}
		});
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			String date;
			try {
				date = sdf.format(ecoreDataTypeVO.getEcoreDataType().getRemovalDate());
			} catch (Exception e) {
				date = "";
			}
			summaryRemovalDate.setText(date);
		}

		ExpandItem registrationItem = new ExpandItem(summaryBar, SWT.NONE);
		registrationItem.setText("Registration Information");
		registrationItem.setHeight(80);
		registrationItem.setExpanded(true);
		registrationItem.setControl(registrationComposite);

		// ===== Registration Information Group End ===============================

		// =================== Exampel Group Begin ==============================
		Composite exampleComposite = new Composite(summaryBar, SWT.NONE);
		exampleComposite.setLayout(new GridLayout(1, false));

//		contraintsComposite.setLayoutData(new GridData(400, 250));
		Label exampleDescLabel = new Label(exampleComposite, SWT.WRAP);
		exampleDescLabel.setText("In this section, you can define examples.");
		GridData exampleLabelGridData = new GridData();
//		exampleLabelGridData.widthHint = 400;
		exampleDescLabel.setLayoutData(exampleLabelGridData);

		Composite exampleToolbarComposite = new Composite(exampleComposite, SWT.NONE);
		exampleToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		addExampleBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createExampleDialogue(exampleList);
				setDirty(true);
			}
		});

		Button deleteExampleBtn = new Button(exampleToolbarComposite, SWT.NONE);
		deleteExampleBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteExampleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteExampleDialogue(exampleList);
				setDirty(true);
			}
		});

		exampleList = new List(exampleComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		GridData exampleListGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		exampleListGridData.widthHint = 500;
		exampleListGridData.heightHint = 100;
		exampleList.setLayoutData(exampleListGridData);
		if(this.ecoreDataTypeVO.getEcoreExamples() != null && this.ecoreDataTypeVO.getEcoreExamples().size() > 0) {
			Map<String,String>map=new HashMap<String,String>();
			for (EcoreExample example: this.ecoreDataTypeVO.getEcoreExamples()) {
				map.put(example.getExample(), example.getId());
				exampleList.add(example.getExample());
				
			}
			exampleList.setData("map", map);
		}

		ExpandItem exampleItem = new ExpandItem(summaryBar, SWT.NONE);
		exampleItem.setText("Example");
		exampleItem.setHeight(210);
		exampleItem.setExpanded(true);
		exampleItem.setControl(exampleComposite);
		// =================== Exampel Group End ==============================

		// =================== constraints Begin ==============================
		Composite constraintComposite = new Composite(summaryBar, SWT.NONE);
		GridData constraintCompositeGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		constraintComposite.setLayout(new GridLayout(1, true));
		constraintComposite.setLayoutData(constraintCompositeGridData);

		Label contraintsDescLabel = new Label(constraintComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object (other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
//		labelGridData.widthHint = 400;
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite toolbarComposite = new Composite(constraintComposite, SWT.NONE);
		toolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		addConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createConstraintDialogue("add", "1", summaryConstraintsTable, newCodeTree, dataType);
				setDirty(true);
			}
		});
		
		Button editConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		editConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.EDIT_IMG_TOOLBAR));
		editConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//不选中不触发任何效果
				if(summaryConstraintsTable.getSelectionCount() > 0) {
					MessageBoxUtil.createConstraintDialogue("edit", "1", summaryConstraintsTable, newCodeTree, dataType);
					setDirty(true);
				}
			}
		});

		Button deleteConstraintBtn = new Button(toolbarComposite, SWT.NONE);
		deleteConstraintBtn.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteConstraintBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteConstraint("1", summaryConstraintsTable, newCodeTree, dataType);
				setDirty(true);
			}
		});
		
		summaryConstraintsTable = new Table(constraintComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		summaryConstraintsTable.setHeaderVisible(true);
		summaryConstraintsTable.setLinesVisible(true);
		TableColumn nameColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		nameColumn.setText("Name");
		nameColumn.setWidth(150);
		TableColumn DefinitionColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		DefinitionColumn.setText("Definiton");
		DefinitionColumn.setWidth(150);
		TableColumn ExpressionLanguageColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		ExpressionLanguageColumn.setText("Expression Language");
		ExpressionLanguageColumn.setWidth(150);
		TableColumn expressionColumn = new TableColumn(summaryConstraintsTable, SWT.NONE);
		expressionColumn.setText("Expression");
		expressionColumn.setWidth(150);

		GridData constraintsTableGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		constraintsTableGridData.widthHint = 620;
		constraintsTableGridData.heightHint = 100;
		summaryConstraintsTable.setLayoutData(constraintsTableGridData);
		
		if(this.ecoreDataTypeVO.getEcoreConstraints() != null && this.ecoreDataTypeVO.getEcoreConstraints().size() > 0) {
			summaryConstraintsTable.removeAll();
			for (EcoreConstraint ecoreConstraint : this.ecoreDataTypeVO.getEcoreConstraints()) {
				TableItem tableItem = new TableItem(summaryConstraintsTable, SWT.NONE);
				tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
						ecoreConstraint.getExpression(), ecoreConstraint.getExpressionlanguage() });
			}
		}

		ExpandItem contentItem = new ExpandItem(summaryBar, SWT.NONE);
		contentItem.setText("Constraints");
		contentItem.setHeight(200);
		contentItem.setExpanded(true);
		contentItem.setControl(constraintComposite);
		// =================== constraints End ==============================

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * content页面
	 */
	void createContentPageForCodeSets() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setBounds(parentComposite.getShell().getBounds());

		GridData sc_gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		scrolledComposite.setLayoutData(sc_gd);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		int index = addPage(scrolledComposite);
		setPageText(index, "Content");

		composite.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = scrolledComposite.getBounds().width;
		composite.setLayoutData(gd);

		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData lc_gd = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 380; // composite.getBounds().width / 3;
		lc_gd.heightHint = 800;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);
		
		// 如果是派生的 右边不能编辑
		if (this.traceId != null) {
			rightComposite.setEnabled(false);
		}

		// ------------ Content Expand Bar Begin ----------------------------------
		ExpandBar contentBar = new ExpandBar(leftComposite, SWT.NONE);
		contentBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		contentBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		contentBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite contentBarComposite = new Composite(contentBar, SWT.NONE);
		contentBarComposite.setBounds(contentBar.getBounds());
		contentBarComposite.setLayout(new GridLayout(1, false));
		contentBarComposite.setLayoutData(new GridData(350, 750));

		Composite contentToolbarComposite = new Composite(contentBarComposite, SWT.NONE);
		contentToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addButtonForContent = new Button(contentToolbarComposite, SWT.NONE);
		addButtonForContent.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addButtonForContent.addMouseListener(new AddContentElementListener());
		if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatus.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
			if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是可衍生的，
				addButtonForContent.setEnabled(true);
			} else {
				addButtonForContent.setEnabled(false);
			}
		} else if (ecoreDataTypeVO.getEcoreDataType() != null && ecoreDataTypeVO.getEcoreDataType().getTrace() != null) {
			addButtonForContent.setEnabled(false);
		}
		
		Button deleteButtonForContent = new Button(contentToolbarComposite, SWT.NONE);
		deleteButtonForContent.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForContent.addMouseListener(new DeleteContentElementListener(rightComposite));
		if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatus.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
			if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是可衍生的，
				deleteButtonForContent.setEnabled(true);
			} else {
				deleteButtonForContent.setEnabled(false);
			}
		} else if (ecoreDataTypeVO.getEcoreDataType() != null && ecoreDataTypeVO.getEcoreDataType().getTrace() != null) {
			deleteButtonForContent.setEnabled(false);
		}		
		
		// 构造Content下的Code节点
		if (traceId != null) {
			
			newCodeTree = new Tree(contentBarComposite, SWT.CHECK | SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			newCodeTree.setLayoutData(new GridData(330, 650));
			
			ArrayList<EcoreCode> ecoreCodeList = DerbyDaoUtil.getCodeByCodeSetId(traceId);
			for (EcoreCode ecoreCode: ecoreCodeList) {
				TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
				treeItem.setText("" + ecoreCode.getName());
				treeItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB2));
				
				for (TreeItem codeTreeItem: this.modelExploreTreeItem.getItems()) {
					EcoreTreeNode ecoreTreeNode = (EcoreTreeNode)codeTreeItem.getData("EcoreTreeNode");
					if (ecoreTreeNode.getName().equals(ecoreCode.getName())) {
						treeItem.setChecked(true);
					}
				}
				treeItem.setData("contentNameText", ecoreCode.getName());
				treeItem.setData("contentDocText", ecoreCode.getDefinition());
				treeItem.setData("contentCodeNameText", ecoreCode.getCodeName());
				treeItem.setData("contentObjectIdentifierValue", ecoreCode.getObjectIdentifier());
				treeItem.setData("contentStatusCombo", ecoreCode.getRegistrationStatus());
				treeItem.setData("contentRemovalDate", ecoreCode.getRemovalDate());
				
				if (ecoreDataTypeVO.getEcoreDataType() != null && RegistrationStatus.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
					treeItem.setData("disableCheckbox", Boolean.TRUE);
				}
				
				java.util.List<EcoreConstraint> ecoreConstraints = DerbyDaoUtil.getContraints(ecoreCode.getId());
				java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
				if (ecoreConstraints != null && ecoreConstraints.size() > 0) {
					for (EcoreConstraint constraint : ecoreConstraints) {
						String[] str = new String[4];
						str[0] = constraint.getName();
						str[1] = constraint.getDefinition();
						str[2] = constraint.getExpression();
						str[3] = constraint.getExpressionlanguage();
						constraintsTableItems.add(str);
					}
				}
				treeItem.setData("constraintsTableItems", constraintsTableItems);
			}
		} else {

			newCodeTree = new Tree(contentBarComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
			newCodeTree.setLayoutData(new GridData(330, 650));
			
			if(ecoreDataTypeVO.getEcoreCodeVOs() != null && ecoreDataTypeVO.getEcoreCodeVOs().size() > 0) {
				java.util.List<EcoreCodeVO> ecoreCodeVOs = ecoreDataTypeVO.getEcoreCodeVOs();
				for (EcoreCodeVO ecoreCodeVO : ecoreCodeVOs) {
					EcoreCode ecoreCode = ecoreCodeVO.getEcoreCode();
					TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
					treeItem.setText("" + ecoreCode.getName());
					treeItem.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB2));
					treeItem.setData("contentNameText", ecoreCode.getName());
					treeItem.setData("contentDocText", ecoreCode.getDefinition());
					treeItem.setData("contentCodeNameText", ecoreCode.getCodeName());
					treeItem.setData("contentObjectIdentifierValue", ecoreCode.getObjectIdentifier());
					treeItem.setData("contentStatusCombo", ecoreCode.getRegistrationStatus());
					treeItem.setData("contentRemovalDate", ecoreCode.getRemovalDate());
					
					java.util.List<EcoreConstraint> ecoreConstraints = ecoreCodeVO.getEcoreConstraints();
					java.util.List<String[]> constraintsTableItems = new ArrayList<String[]>();
					if (ecoreConstraints != null && ecoreConstraints.size() > 0) {
						for (EcoreConstraint constraint : ecoreConstraints) {
							String[] str = new String[4];
							str[0] = constraint.getName();
							str[1] = constraint.getDefinition();
							str[2] = constraint.getExpression();
							str[3] = constraint.getExpressionlanguage();
							constraintsTableItems.add(str);
						}
					}
					treeItem.setData("constraintsTableItems", constraintsTableItems);
				}
			}
		}

		// 不选中无法操作
//		rightComposite.setEnabled(false);
		newCodeTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 选中可操作
//				rightComposite.setEnabled(true);
				TreeItem item = (TreeItem) e.item;
				selectedTreeItem = item;
				if (item.getData("disableCheckbox") != null && (Boolean)item.getData("disableCheckbox") && e.detail == SWT.CHECK) {
					item.setChecked(!item.getChecked());
				}
				if (RegistrationStatus.Registered.getStatus().equals(item.getData("contentStatusCombo"))) {
					setUnavailable(contentNameText);
					setUnavailable(contentDocText);
					setUnavailable(contentCodeNameText);
					setUnavailable(contentObjectIdentifierValue);
					setUnavailable(contentRemovalDate);
				} else if (traceId != null) {
					setUnavailable(contentNameText);
					setUnavailable(contentDocText);
					setUnavailable(contentCodeNameText);
					setUnavailable(contentObjectIdentifierValue);
					setUnavailable(contentRemovalDate);
				} else {
					rightComposite.setEnabled(true);
					setAvailable(contentNameText);
					setAvailable(contentDocText);
					setAvailable(contentCodeNameText);
					setAvailable(contentObjectIdentifierValue);
					setAvailable(contentRemovalDate);
				}
				contentNameText.setText(
						String.valueOf(item.getData("contentNameText") == null ? "" : item.getData("contentNameText")));
				contentDocText.setText(
						String.valueOf(item.getData("contentDocText") == null ? "" : item.getData("contentDocText")));
				contentCodeNameText.setText(String.valueOf(
						item.getData("contentCodeNameText") == null ? "" : item.getData("contentCodeNameText")));
				contentObjectIdentifierValue.setText(String.valueOf(
						item.getData("contentObjectIdentifierValue") == null ? "" : item.getData("contentObjectIdentifierValue")));
				contentStatusCombo.setText(String.valueOf(
						item.getData("contentStatusCombo") == null ? "" : item.getData("contentStatusCombo")));
				contentRemovalDate.setText(String.valueOf(
						item.getData("contentRemovalDate") == null ? "" : item.getData("contentRemovalDate")));
				contentConstraintsTable.removeAll();
				java.util.List<String[]> tableItems = (java.util.List<String[]>) item.getData("constraintsTableItems");
				if (tableItems != null && tableItems.size() > 0) {
					for (int i = 0; i < tableItems.size(); i++) {
						TableItem tableItem = new TableItem(contentConstraintsTable, SWT.NONE);
						tableItem.setText(tableItems.get(i));
					}
				}
			}
		});

		ExpandItem contentItem = new ExpandItem(contentBar, SWT.NONE);
		contentItem.setText("Content");
		contentItem.setHeight(750);
		contentItem.setExpanded(true);
		contentItem.setControl(contentBarComposite);
		// ------------ Content Expand Bar End ----------------------------------

		// ------------ Code Details Begin ------------------------
		ExpandBar codeDetailBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
		codeDetailBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		codeDetailBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		codeDetailBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		
		Composite generalInformationComposite = new Composite(codeDetailBar, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));

		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalInforGridData.widthHint = 400;
		Label beNameLabel = new Label(generalInformationComposite, SWT.NONE);
		beNameLabel.setText("Name");
		contentNameText = new Text(generalInformationComposite, SWT.BORDER);
		contentNameText.setLayoutData(generalInforGridData);
		contentNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentNameText", contentNameText.getText());
					selectedTreeItem.setText(contentNameText.getText()==null?"":contentNameText.getText());
					setDirty(true);
				}
			}
		});
//		this.setUnavailable(contentNameText);
		
		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 400;
		documentationGridData.heightHint = 150;
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setText("Documentation");
		contentDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
		contentDocText.setLayoutData(documentationGridData);
		contentDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentDocText", contentDocText.getText());
					setDirty(true);
				}
			}
		});
//		this.setUnavailable(contentDocText);
		
		Label minOccursLabel = new Label(generalInformationComposite, SWT.NONE);
		minOccursLabel.setText("CodeName");
		contentCodeNameText = new Text(generalInformationComposite, SWT.BORDER);
		contentCodeNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentCodeNameText", contentCodeNameText.getText());
					setDirty(true);					
				}
			}
		});
//		this.setUnavailable(contentCodeNameText);
		
		ExpandItem generalInformationItem = new ExpandItem(codeDetailBar, SWT.NONE);
		generalInformationItem.setText("Code Details");
		generalInformationItem.setHeight(280);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);
		// ------------ Code Details End ------------------------

		// -----------------------CMP Information Group Begin -----------------------
		Composite cmpInformationComposite = new Composite(codeDetailBar, SWT.NONE);
		cmpInformationComposite.setLayout(new GridLayout(2, false));
		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
		objectIdentifierLabel.setText("Object Identifier");
		contentObjectIdentifierValue = new Text(cmpInformationComposite, SWT.BORDER);
		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		objectIdentifierGridData.widthHint = 350;
		contentObjectIdentifierValue.setLayoutData(objectIdentifierGridData);
		contentObjectIdentifierValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentObjectIdentifierValue", contentObjectIdentifierValue.getText());
					setDirty(true);
				}
			}
		});
//		this.setUnavailable(contentObjectIdentifierValue);
		
		ExpandItem cmpInformationItem = new ExpandItem(codeDetailBar, SWT.NONE);
		cmpInformationItem.setText("CMP Information");
		cmpInformationItem.setHeight(90);
		cmpInformationItem.setExpanded(true);
		cmpInformationItem.setControl(cmpInformationComposite);
		// -----------------------CMP Information Group End -----------------------

		// ----------------------- Registration Information Group Begin -----------
		Composite registrationInfoComposite = new Composite(codeDetailBar, SWT.NONE);
		registrationInfoComposite.setLayout(new GridLayout(2, false));
		Label registrationStatusLabel = new Label(registrationInfoComposite, SWT.NONE);
		registrationStatusLabel.setText("Registration Status");
		ComboViewer codeStatusComboViewer = new ComboViewer(registrationInfoComposite, SWT.NONE);
		contentStatusCombo = codeStatusComboViewer.getCombo();
//		contentStatusCombo.add("Registered");
//		contentStatusCombo.add("Provisionally Registered");
//		contentStatusCombo.select(1);
		contentStatusCombo.setEnabled(false);
		GridData valueGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		valueGridData.widthHint = 150;
		contentStatusCombo.setLayoutData(valueGridData);
		contentStatusCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentStatusCombo", contentStatusCombo.getText());
					setDirty(true);
				}
			}
		});
		
		Label removalDateLabel = new Label(registrationInfoComposite, SWT.NONE);
		removalDateLabel.setText("Removal Date");
		contentRemovalDate = new Text(registrationInfoComposite, SWT.BORDER);
		contentRemovalDate.setToolTipText("Date Format: MM/DD/YY or YYYY-MM-DD");
		contentRemovalDate.setLayoutData(valueGridData);
		contentRemovalDate.setEnabled(false);
		contentRemovalDate.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (!selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentRemovalDate", contentRemovalDate.getText());
					setDirty(true);
				}
			}
		});

		ExpandItem registrationInfoItem = new ExpandItem(codeDetailBar, SWT.NONE);
		registrationInfoItem.setText("Registration Information");
		registrationInfoItem.setHeight(90);
		registrationInfoItem.setExpanded(true);
		registrationInfoItem.setControl(registrationInfoComposite);

		// ----------------------- Registration Information Group End

		// ---------- Constraints Expand Item Begin
		Composite contraintsComposite = new Composite(codeDetailBar, SWT.NONE);
		contraintsComposite.setLayout(new GridLayout(1, false));
		contraintsComposite.setLayoutData(new GridData(400, 250));
		Label contraintsDescLabel = new Label(contraintsComposite, SWT.WRAP);
		contraintsDescLabel.setText(
				"All the constraints contained in this object(other constraints - such as constraints defined on type - may also apply).");
		GridData labelGridData = new GridData();
		labelGridData.widthHint = 400;
		contraintsDescLabel.setLayoutData(labelGridData);

		Composite contraintsToolbarComposite = new Composite(contraintsComposite, SWT.NONE);
		contraintsToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Button addButtonForContraints = new Button(contraintsToolbarComposite, SWT.NONE);
		addButtonForContraints.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
		addButtonForContraints.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.createConstraintDialogue("add", "1", contentConstraintsTable, newCodeTree, dataType, selectedTreeItem);
				setDirty(true);
			}
		});
//		this.setUnavailable(addButtonForContraints);
		
		Button deleteButtonForConstraint = new Button(contraintsToolbarComposite, SWT.NONE);
		deleteButtonForConstraint.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
		deleteButtonForConstraint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				MessageBoxUtil.deleteConstraint("1", contentConstraintsTable, newCodeTree,dataType, selectedTreeItem);
				setDirty(true);
			}
		});
//		this.setUnavailable(deleteButtonForConstraint);
		
		contentConstraintsTable = new Table(contraintsComposite, SWT.BORDER);
		contentConstraintsTable.setHeaderVisible(true);
		contentConstraintsTable.setLinesVisible(true);
		contentConstraintsTable.setLayoutData(new GridData(420, 100));

		TableColumn nameColumn = new TableColumn(contentConstraintsTable, SWT.NONE);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		TableColumn defifinitionColumn = new TableColumn(contentConstraintsTable, SWT.NONE);
		defifinitionColumn.setWidth(100);
		defifinitionColumn.setText("Definition");
		TableColumn expressionLanguageColumn = new TableColumn(contentConstraintsTable, SWT.NONE);
		expressionLanguageColumn.setWidth(100);
		expressionLanguageColumn.setText("Expression Language");
		TableColumn expressionColumn = new TableColumn(contentConstraintsTable, SWT.NONE);
		expressionColumn.setWidth(150);
		expressionColumn.setText("Expression");

		ExpandItem contraintsItem = new ExpandItem(codeDetailBar, SWT.NONE);
		contraintsItem.setText("Contraints");
		contraintsItem.setHeight(250);
		contraintsItem.setExpanded(true);
		contraintsItem.setControl(contraintsComposite);
		// ---------- Constraints Expand Item End -------------------------------------

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	/**
	 * content页面(其它统一)
	 */
	void createContentPageForOthers() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setBounds(parentComposite.getShell().getBounds());

		GridData sc_gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		scrolledComposite.setLayoutData(sc_gd);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		int index = addPage(scrolledComposite);
		setPageText(index, "Content");

		composite.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = scrolledComposite.getBounds().width;
		composite.setLayoutData(gd);

		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData lc_gd = new GridData(SWT.TOP, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 380; // composite.getBounds().width / 3;
		lc_gd.heightHint = 800;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);

		// ------------ Content Expand Bar Begin ----------------------------------
		ExpandBar contentBar = new ExpandBar(leftComposite, SWT.NONE);
		contentBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		contentBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		contentBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite contentBarComposite = new Composite(contentBar, SWT.NONE);
		contentBarComposite.setBounds(contentBar.getBounds());
		contentBarComposite.setLayout(new GridLayout(1, false));
		contentBarComposite.setLayoutData(new GridData(350, 750));

		Composite contentToolbarComposite = new Composite(contentBarComposite, SWT.NONE);
		contentToolbarComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

//		Button addButtonForContent = new Button(contentToolbarComposite, SWT.NONE);
//		addButtonForContent.setImage(ImgUtil.createImg(ImgUtil.ADD_IMG_TOOLBAR));
//		addButtonForContent.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.createConstraintDialogue(null, newCodeTree, selectedTreeItem);
//			}
//		});
//
//		Button deleteButtonForContent = new Button(contentToolbarComposite, SWT.NONE);
//		deleteButtonForContent.setImage(ImgUtil.createImg(ImgUtil.DELETE_IMG_YES));
//		deleteButtonForContent.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				MessageBoxUtil.deleteConstraint(null, newCodeTree, selectedTreeItem);
//				if (newCodeTree.getItemCount() == 0) {
//					rightComposite.setEnabled(false);
//					contentNameText.setText("");
//					contentDocText.setText("");
//					contentExpressionText.setText("");
//					contentExpressionLanguageText.setText("");
//					contentObjectIdentifierValue.setText("");
//				}
//			}
//		});

		newCodeTree = new Tree(contentBarComposite, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		newCodeTree.setLayoutData(new GridData(330, 650));
		
		if(ecoreDataTypeVO.getEcoreConstraints() != null && ecoreDataTypeVO.getEcoreConstraints().size() > 0) {
			java.util.List<EcoreConstraint> ecoreConstraints = ecoreDataTypeVO.getEcoreConstraints();
			if (ecoreConstraints != null && ecoreConstraints.size() > 0) {
				for (EcoreConstraint constraint : ecoreConstraints) {
					TreeItem treeItem = new TreeItem(newCodeTree, SWT.NONE);
					treeItem.setText("" + constraint.getName());
					treeItem.setImage(ImgUtil.createImg(ImgUtil.CONSTRAINT));
					treeItem.setData("contentNameText", constraint.getName());
					treeItem.setData("contentDocText", constraint.getDefinition());
					treeItem.setData("contentExpressionText", constraint.getExpression());
					treeItem.setData("contentExpressionLanguageText", constraint.getExpressionlanguage());
					treeItem.setData("contentObjectIdentifierValue", constraint.getObjectIdentifier());
				}
			}
		}

		// 不选中无法操作
//		rightComposite.setEnabled(false);
		newCodeTree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 选中可操作
//				rightComposite.setEnabled(true);
				TreeItem item = (TreeItem) e.item;
				selectedTreeItem = item;
				contentNameText.setText(String.valueOf(item.getData("contentNameText") == null ? "" : item.getData("contentNameText")));
				contentDocText.setText(
						String.valueOf(item.getData("contentDocText") == null ? "" : item.getData("contentDocText")));
				contentExpressionText
						.setText(String.valueOf(item.getData("contentExpressionText") == null ? "" : item.getData("contentExpressionText")));
				contentExpressionLanguageText.setText(String.valueOf(
						item.getData("contentExpressionLanguageText") == null ? "" : item.getData("contentExpressionLanguageText")));
				contentObjectIdentifierValue.setText(String.valueOf(
						item.getData("contentObjectIdentifierValue") == null ? "" : item.getData("contentObjectIdentifierValue")));
			}
		});

		ExpandItem contentItem = new ExpandItem(contentBar, SWT.NONE);
		contentItem.setText("Content");
		contentItem.setHeight(750);
		contentItem.setExpanded(true);
		contentItem.setControl(contentBarComposite);
		// ------------ Content Expand Bar End ----------------------------------

		// ------------ Code Details Begin ------------------------
		ExpandBar codeDetailBar = new ExpandBar(rightComposite, SWT.V_SCROLL);
		codeDetailBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		codeDetailBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		codeDetailBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite generalInformationComposite = new Composite(codeDetailBar, SWT.NONE);
		generalInformationComposite.setLayout(new GridLayout(2, false));

		GridData generalInforGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		generalInforGridData.widthHint = 400;
		Label beNameLabel = new Label(generalInformationComposite, SWT.NONE);
		beNameLabel.setText("Name");
		contentNameText = new Text(generalInformationComposite, SWT.BORDER);
		contentNameText.setLayoutData(generalInforGridData);
		contentNameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentNameText", contentNameText.getText());
					selectedTreeItem.setText(contentNameText.getText()==null?"":contentNameText.getText());
					setDirty(true);
				}
			}
		});
		this.setUnavailable(contentNameText);
		
		GridData documentationGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		documentationGridData.widthHint = 400;
		documentationGridData.heightHint = 150;
		Label documentationLabel = new Label(generalInformationComposite, SWT.NONE);
		documentationLabel.setText("Documentation");
		contentDocText = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
		contentDocText.setLayoutData(documentationGridData);
		contentDocText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentDocText", contentDocText.getText());
					setDirty(true);					
				}
			}
		});
		this.setUnavailable(contentDocText);
		
		Label expression = new Label(generalInformationComposite, SWT.NONE);
		expression.setText("Expression");
		contentExpressionText = new Text(generalInformationComposite, SWT.BORDER | SWT.WRAP);
		contentExpressionText.setLayoutData(documentationGridData);
		contentExpressionText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentExpressionText", contentExpressionText.getText());
					setDirty(true);					
				}
			}
		});
		this.setUnavailable(contentExpressionText);
		
		Label expressionLanguage = new Label(generalInformationComposite, SWT.NONE);
		expressionLanguage.setText("Expression Language");
		contentExpressionLanguageText = new Text(generalInformationComposite, SWT.BORDER);
		contentExpressionLanguageText.setLayoutData(generalInforGridData);
		contentExpressionLanguageText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentExpressionLanguageText", contentExpressionLanguageText.getText());
					setDirty(true);					
				}
			}
		});
		this.setUnavailable(contentExpressionLanguageText);
		
		ExpandItem generalInformationItem = new ExpandItem(codeDetailBar, SWT.NONE);
		generalInformationItem.setText("General Information");
		generalInformationItem.setHeight(500);
		generalInformationItem.setExpanded(true);
		generalInformationItem.setControl(generalInformationComposite);
		// ------------ Code Details End ------------------------

		// -----------------------CMP Information Group Begin -----------------------
		Composite cmpInformationComposite = new Composite(codeDetailBar, SWT.NONE);
		cmpInformationComposite.setLayout(new GridLayout(2, false));
		Label objectIdentifierLabel = new Label(cmpInformationComposite, SWT.NONE);
		objectIdentifierLabel.setText("Object Identifier");
		contentObjectIdentifierValue = new Text(cmpInformationComposite, SWT.BORDER);
		GridData objectIdentifierGridData = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		objectIdentifierGridData.widthHint = 350;
		contentObjectIdentifierValue.setLayoutData(objectIdentifierGridData);
		contentObjectIdentifierValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (selectedTreeItem != null && !selectedTreeItem.isDisposed()) {
					selectedTreeItem.setData("contentObjectIdentifierValue", contentObjectIdentifierValue.getText());
					setDirty(true);					
				}
			}
		});
		this.setUnavailable(contentObjectIdentifierValue);
		
		ExpandItem cmpInformationItem = new ExpandItem(codeDetailBar, SWT.NONE);
		cmpInformationItem.setText("CMP Information");
		cmpInformationItem.setHeight(90);
		cmpInformationItem.setExpanded(true);
		cmpInformationItem.setControl(cmpInformationComposite);
		// -----------------------CMP Information Group End -----------------------

		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	void createImpactPage() {
		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL);
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		composite.setLayout(new GridLayout(2, false));
		int index = addPage(scrolledComposite);
		setPageText(index, "Impact");
		GridData gd = new GridData(SWT.TOP, SWT.TOP, true, false, 1, 1);
		composite.setLayoutData(gd);

		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Label descriptionLeft = new Label(composite, SWT.WRAP);
		descriptionLeft.setText(
				"This section shows all the repository items that refer to this respository item. Select any of then to see how it is used.");
		GridData leftGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		leftGridData.widthHint = 500;
		descriptionLeft.setLayoutData(leftGridData);

		Label descriptionRight = new Label(composite, SWT.WRAP);
		descriptionRight.setText(
				"This section shows the content of the impacted element that is currently selected in left-hand pane.");
		GridData rightGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		rightGridData.widthHint = 600;
		descriptionRight.setLayoutData(rightGridData);

		Composite groupListComposite = new Composite(composite, SWT.NONE);
		groupListComposite.setLayout(new GridLayout(1, true));
		GridData groupListCompositeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		groupListCompositeGridData.widthHint = 500;
		groupListComposite.setLayoutData(groupListCompositeGridData);

		GridData groupGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		groupGridData.widthHint = 500;
		groupGridData.heightHint = 300;

		Group messageSetsGroup = new Group(groupListComposite, SWT.NONE);
		messageSetsGroup.setText("Message Sets");
		messageSetsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageSetsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageSetsGroup.setLayoutData(groupGridData);
		messageSetsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageSetTable = new Table(messageSetsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageSetTable.setLinesVisible(true);
		TableCursor messageSetTableCursor = new TableCursor(messageSetTable, SWT.NONE);
		// 构造Message Set表格
		GenerateTableForMCandMDandMS.generateMessageSetTableOfImpactPage(messageSetTable, msgSetList);

		Group messageDefinitionsGroup = new Group(groupListComposite, SWT.NONE);
		messageDefinitionsGroup.setText("Message Definitions");
		messageDefinitionsGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageDefinitionsGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageDefinitionsGroup.setLayoutData(groupGridData);
		messageDefinitionsGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageDefinitionTable = new Table(messageDefinitionsGroup, SWT.BORDER | SWT.V_SCROLL);
		messageDefinitionTable.setLinesVisible(true);
		TableCursor messageDefinitionTableCursor = new TableCursor(messageDefinitionTable, SWT.NONE);
		// 构造Message Definition表格
		GenerateTableForMCandMDandMS.generateMessageDefinationTableOfImpactPage(messageDefinitionTable, msgDefinitionList);

		Group messageComponentGroup = new Group(groupListComposite, SWT.NONE);
		messageComponentGroup.setText("Message Component");
		messageComponentGroup.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));
		messageComponentGroup.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		messageComponentGroup.setLayoutData(groupGridData);
		messageComponentGroup.setLayout(new FillLayout(SWT.VERTICAL));
		Table messageComponentTable = new Table(messageComponentGroup, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		messageComponentTable.setLinesVisible(true);
		TableCursor msgComponentTableCursor = new TableCursor(messageComponentTable, SWT.NONE);
		// 构造Message Component表格
		GenerateTableForMCandMDandMS.generateMessageComponentTableOfImpactPage(messageComponentTable, ecoreMessageComponentList);
		
		Composite treeComposite = new Composite(composite, SWT.NONE);
		FillLayout treeCompositetFillLayout = new FillLayout(SWT.VERTICAL);
		treeComposite.setLayout(treeCompositetFillLayout);
		GridData treeCompositeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		treeCompositeGridData.widthHint = 600;
		treeCompositeGridData.heightHint = 800;
		treeComposite.setLayoutData(treeCompositeGridData);

		Tree containmentTree = new Tree(treeComposite, SWT.BORDER);
		TreeItem treeRoot = new TreeItem(containmentTree, SWT.NONE);
		treeRoot.setExpanded(true);

		// For Message Set表格，添加点击事件
		messageSetTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = messageSetTableCursor.getRow();
				String msgSetName = tableItem.getText(messageSetTableCursor.getColumn());
				treeRoot.setText(msgSetName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MS_SUB1));
				treeRoot.removeAll();
				String messageSetId = (String)tableItem.getData(String.valueOf(messageSetTable.getSelectionIndex()));
				ArrayList<EcoreMessageDefinition> msgDefinitionList = DerbyDaoUtil.getMsgDefinitionByMsgSetId(messageSetId);
				// 递归生成contrainment tree on Impact page
				GenerateCommonTree.generateContainmentTreeForMessageSetofImpactPage(treeRoot, msgDefinitionList);
				// 加载Constraint节点
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(messageSetId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		
		// For 消息定义表添加单击事件
		messageDefinitionTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = messageDefinitionTableCursor.getRow();
				String msgDefinitionName = tableItem.getText(messageDefinitionTableCursor.getColumn());
				String msgDefinitionId = (String)tableItem.getData(String.valueOf(messageDefinitionTable.getSelectionIndex()));
				treeRoot.setText(msgDefinitionName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MD_SUB1));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				GenerateCommonTree.generateContainmentTreeForMessageDefinition(treeRoot, DerbyDaoUtil.getMsgBuildingBlock(msgDefinitionId));
				// 加载Constraint节点
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(msgDefinitionId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		
		// For 消息组件表 添加单击事件
		msgComponentTableCursor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem tableItem = msgComponentTableCursor.getRow();
				String msgComponentName = tableItem.getText(msgComponentTableCursor.getColumn());
				String msgComponentId = (String)tableItem.getData(String.valueOf(messageComponentTable.getSelectionIndex()));
				treeRoot.setText(msgComponentName);
				treeRoot.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
				treeRoot.removeAll();
				// 递归生成contrainment tree on Impact page
				GenerateCommonTree.generateContainmentTreeForMessageComponent(treeRoot, DerbyDaoUtil.getMessageElementList(msgComponentId));
				// 加载Constraint节点
				GenerateCommonTree.addContraintsNode(treeRoot, DerbyDaoUtil.getContraints(msgComponentId), null, null);
				treeRoot.setExpanded(true);
			}
		});
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

	}
	
	void createIncomingAssociationsPage() {
		Composite composite = new Composite(getContainer(), SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		int index = addPage(composite);
		setPageText(index, "Incoming Associations");
		GridData gd = new GridData(SWT.TOP, SWT.TOP, true, false, 1, 1);
		composite.setLayoutData(gd);

		Label descriptionLabel = new Label(composite, SWT.WRAP);
		descriptionLabel.setText(
				"Impactd Elements: This section shows all the Business Component that contain an element typed by this repository item. Select any of them to see how it is used.");
		GridData leftGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		leftGridData.widthHint = 500;
		descriptionLabel.setLayoutData(leftGridData);

		Label containmentTreeLabel = new Label(composite, SWT.WRAP);
		containmentTreeLabel.setText(
				"This section shows the content of the impacted element that is currently selected in the left-hand pane.");
		GridData rightGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		rightGridData.widthHint = 600;
		containmentTreeLabel.setLayoutData(rightGridData);

		Table elementTable = new Table(composite, SWT.BORDER | SWT.V_SCROLL);
		elementTable.setLinesVisible(true);
		GridData elementTableGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		elementTableGridData.widthHint = 500;
		elementTableGridData.heightHint = 800;
		elementTable.setLayoutData(elementTableGridData);
//		TableCursor elementTableCursor = new TableCursor(elementTable, SWT.NONE);
		this.generateImpactedElementOfIncomingPage(elementTable);

		GridData containmentTreeGridData = new GridData(SWT.CENTER, SWT.TOP, true, false, 1, 1);
		containmentTreeGridData.widthHint = 600;
		containmentTreeGridData.heightHint = 500;

//		Tree containmentTree = new Tree(composite, SWT.BORDER);
//		TreeItem treeRoot = new TreeItem(containmentTree, SWT.NONE);
//		containmentTree.setLayoutData(containmentTreeGridData);
	}

	private void generateImpactedElementOfIncomingPage(Table elementTable) {
		for (EcoreBusinessComponent bizComponent: imcomingAssociationList) {
			TableItem tableItem1 = new TableItem(elementTable, SWT.NONE);
			tableItem1.setText(bizComponent.getName());
			tableItem1.setData("bizComponentId", bizComponent.getId());
			tableItem1.setImage(ImgUtil.createImg(ImgUtil.BC));
		}
	}
	
	void createVersionSubset() {

		Composite parentComposite = getContainer();
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrolledComposite.setBounds(parentComposite.getShell().getBounds());

		GridData sc_gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		scrolledComposite.setLayoutData(sc_gd);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(composite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		int index = addPage(scrolledComposite);
		setPageText(index, "Version/Subsets");

		composite.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = scrolledComposite.getBounds().width;
		composite.setLayoutData(gd);

		Composite leftComposite = new Composite(composite, SWT.NONE);
		leftComposite.setLayout(new FillLayout(SWT.VERTICAL));
		GridData lc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		lc_gd.widthHint = 550; //composite.getBounds().width / 3;
		leftComposite.setLayoutData(lc_gd);

		GridData rc_gd = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		rc_gd.widthHint = 500; // composite.getBounds().width / 3;
		Composite rightComposite = new Composite(composite, SWT.NONE);
		rightComposite.setLayout(new FillLayout(SWT.VERTICAL));
		rightComposite.setLayoutData(rc_gd);

		ExpandBar nextVersionBar = new ExpandBar(leftComposite, SWT.NONE);
		Composite nextVersionBarComposite = new Composite(nextVersionBar, SWT.NONE);
		GridData nextVersionBarCompositeGridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		nextVersionBarComposite.setLayout(new GridLayout(1, false));
		nextVersionBarComposite.setLayoutData(nextVersionBarCompositeGridData);

		nextVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		nextVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		nextVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Label leftDescriptionLabel = new Label(nextVersionBarComposite, SWT.WRAP);
		leftDescriptionLabel.setText("Shows the new versions of this repository itme.");
		List versionListWidget = new List(nextVersionBarComposite, SWT.BORDER);
		GridData versionListGridData = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		versionListGridData.widthHint = 500;
		versionListWidget.setLayoutData(versionListGridData);
		this.generateNextDataTypeVersionList(versionListWidget);

		ExpandItem nextVersionItem = new ExpandItem(nextVersionBar, SWT.NONE);
		nextVersionItem.setText("Next Version");
		nextVersionItem.setHeight(450);
		nextVersionItem.setExpanded(true);
		nextVersionItem.setControl(nextVersionBarComposite);

		ExpandBar previousVersionBar = new ExpandBar(rightComposite, SWT.NONE);
		previousVersionBar.setBackground(SystemUtil.getColor(SWT.COLOR_WHITE));
		previousVersionBar.setForeground(SystemUtil.getColor(SWT.COLOR_DARK_BLUE));
		previousVersionBar.setFont(new Font(Display.getCurrent(), new FontData("微软雅黑", 13, SWT.BOLD)));

		Composite previousVersionComposite = new Composite(previousVersionBar, SWT.NONE);
		previousVersionComposite.setLayout(new GridLayout(1, false));
		Label rightDescriptionLabel = new Label(previousVersionComposite, SWT.WRAP);
		rightDescriptionLabel.setText("Shows the previous version of this repository item.");
		Label previousVersionLabel = new Label(previousVersionComposite, SWT.WRAP);
		previousVersionLabel.setText("The previous version of this object is: ");
		Link previousVersionLink = new Link(previousVersionComposite, SWT.NONE);
		previousVersionLink.setText(previousEcoreDataType.getName() == null? "" : previousEcoreDataType.getName());
		previousVersionLink.setData(previousEcoreDataType.getId());
		
		ExpandItem previousVersionItem = new ExpandItem(previousVersionBar, SWT.NONE);
		previousVersionItem.setText("Previous Version");
		previousVersionItem.setHeight(450);
		previousVersionItem.setExpanded(true);
		previousVersionItem.setControl(previousVersionComposite);
	}
	
	private void generateNextDataTypeVersionList(List versionListWidget) {
		for (int index = 0; index < this.nextDataTypeList.size(); index++) {
			EcoreDataType ecoreDataType = nextDataTypeList.get(index);
			versionListWidget.add(ecoreDataType.getName());
			versionListWidget.setData(String.valueOf(index), ecoreDataType.getId());
		}
	}

	void createMsgElementSelectionDialogue() {
		TreeItem item = new TreeItem(newCodeTree, SWT.NONE);
		item.setImage(ImgUtil.createImg(ImgUtil.CODE_SET_SUB2));
		item.setText("");
		item.setData("contentStatusCombo", RegistrationStatus.Added.getStatus()); 
	}

	void createSelectMsgElementType(Text typeText, ArrayList<EcoreMessageComponent> allMessageComponentList) {
		Shell messageComponentWindow = new Shell(Display.getCurrent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		messageComponentWindow.setText("Select Message Element type:");
		messageComponentWindow.setLayout(new FormLayout());
		Composite c = new Composite(messageComponentWindow, SWT.NONE);
		FormData fd_c = new FormData();
		fd_c.top = new FormAttachment(0);
		fd_c.left = new FormAttachment(0);
		fd_c.bottom = new FormAttachment(100);
		fd_c.right = new FormAttachment(100);
		Text searchText = new Text(c, SWT.BORDER);
		searchText.setBounds(10, 10, 350, 30);

		Table msgComponentTable = new Table(c, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		msgComponentTable.setLinesVisible(true);
		this.generateMessageComponentTable(msgComponentTable, allMessageComponentList);
		msgComponentTable.setBounds(10, 50, 350, 400);

		Button okButton = new Button(c, SWT.PUSH);
		okButton.setText("OK");
		okButton.setBounds(275, 460, 35, 30);
		okButton.addMouseListener(new OkButtonListener(typeText, msgComponentTable, messageComponentWindow));

		Button cancelButton = new Button(c, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setBounds(320, 460, 52, 30);
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				messageComponentWindow.close();
			}
		});

		messageComponentWindow.setSize(400, 550);
		messageComponentWindow.open();
		messageComponentWindow.layout();
	}

	private void generateMessageComponentTable(Table msgComponentTable,
			ArrayList<EcoreMessageComponent> allMessageComponentList) {
		for (EcoreMessageComponent msgComponent : allMessageComponentList) {
			TableItem tableItem = new TableItem(msgComponentTable, SWT.NONE);
			tableItem.setText(msgComponent.getName()==null?"":msgComponent.getName());
			tableItem.setData(msgComponent.getId());
			tableItem.setImage(ImgUtil.createImg(ImgUtil.MC_SUB1_COMPONENT));
		}
		msgComponentTable.setSelection(0);
	}

	/**
	 * 获取所有信息
	 * @return
	 */
	public EcoreDataTypeVO getAllData() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		EcoreDataTypeVO dataTypeVO = new EcoreDataTypeVO();
		
		//获取基本信息
		EcoreDataType ecoreDataType = new EcoreDataType();
		ecoreDataType.setId(ecoreDataTypeId);
		DataTypesEnum[] dataTypesEnums = DataTypesEnum.values();
		for (DataTypesEnum dataTypesEnum : dataTypesEnums) {
			if(dataTypesEnum.getName().equals(this.dataType)) {
				ecoreDataType.setType(dataTypesEnum.getType());
				break;
			}
		}

		ecoreDataType.setName(this.nameText == null ? null : this.nameText.getText());
		ecoreDataType.setDefinition(this.documentationText == null ? null : this.documentationText.getText());
		ecoreDataType.setMinLength(this.minLengthText == null ? null : NumberFormatUtil.getInt(this.minLengthText.getText()));
		ecoreDataType.setMaxLength(this.maxLengthText == null ? null : NumberFormatUtil.getInt(this.maxLengthText.getText()));
		ecoreDataType.setLength(this.lengthText == null ? null : NumberFormatUtil.getInt(this.lengthText.getText()));
		ecoreDataType.setPattern(this.patternText == null ? null : this.patternText.getText());
		ecoreDataType.setIdentificationScheme(this.identificationSchemeText == null ? null : this.identificationSchemeText.getText());
		ecoreDataType.setMeaningWhenTrue(this.meaningWhenTrueText == null ? null : this.meaningWhenTrueText.getText());
		ecoreDataType.setMeaningWhenFalse(this.meaningWhenFalseText == null ? null : this.meaningWhenFalseText.getText());
		ecoreDataType.setMinInclusive(this.minInclusiveText == null ? null : this.minInclusiveText.getText());
		ecoreDataType.setMinExclusive(this.minExclusiveText == null ? null : this.minExclusiveText.getText());
		ecoreDataType.setMaxInclusive(this.maxInclusiveText == null ? null : this.maxInclusiveText.getText());
		ecoreDataType.setMaxExclusive(this.maxExclusiveText == null ? null : this.maxExclusiveText.getText());
		ecoreDataType.setTotalDigits(this.totalDigitsText == null ? null : NumberFormatUtil.getInt(this.totalDigitsText.getText()));
		ecoreDataType.setFractionDigits(this.fractionDigitsText == null ? null : NumberFormatUtil.getInt(this.fractionDigitsText.getText()));
		ecoreDataType.setBaseValue(this.baseValueText == null ? null : NumberFormatUtil.getDouble(this.baseValueText.getText()));
		ecoreDataType.setBaseUnitCode(this.baseUnitCodeText == null ? null : this.baseUnitCodeText.getText());
		ecoreDataType.setNamespace(this.namespaceCombo == null ? null : this.namespaceCombo.getText());
		ecoreDataType.setNamespaceList(this.namespaceListText == null ? null : this.namespaceListText.getText());
		ecoreDataType.setProcessContents(this.processContentCombo == null ? null : this.processContentCombo.getText());
		ecoreDataType.setObjectIdentifier(this.objectIdentifierValue.getText());
		ecoreDataType.setRegistrationStatus(statusComboViewer.getCombo().getText());
		Date removalDate;
		try {
			removalDate = (summaryRemovalDate.getText() == null ? null : sdf.parse(summaryRemovalDate.getText()));
		} catch (ParseException e) {
			removalDate = null;
		}
		ecoreDataType.setRemovalDate(removalDate);
		
		dataTypeVO.setEcoreDataType(ecoreDataType);
		
		//获取example信息
		java.util.List<EcoreExample> examples = new ArrayList<EcoreExample>();
		String[] exampleItems = this.exampleList.getItems();
		Map<String,String>map=(Map<String, String>) exampleList.getData("map");
		if(exampleItems != null && exampleItems.length > 0) {
			for (String str : exampleItems) {
				EcoreExample ecoreExample = new EcoreExample();
				if (map!=null) {
					ecoreExample.setId(map.get(str)==null?UUID.randomUUID().toString():map.get(str));
					}else {
						ecoreExample.setId(UUID.randomUUID().toString());
					}
				ecoreExample.setExample(str);
				ecoreExample.setObj_id(ecoreDataType.getId());
				ecoreExample.setObj_type(ObjTypeEnum.DataType.getType());
				examples.add(ecoreExample);
			}
		}
		dataTypeVO.setEcoreExamples(examples);
		
		//获取constraints
		java.util.List<EcoreConstraint> constraints = new ArrayList<EcoreConstraint>();
		TableItem[] constraintItems = summaryConstraintsTable.getItems();
		if(constraintItems != null && constraintItems.length > 0) {
			for (TableItem tableItem : constraintItems) {
				EcoreConstraint ecoreConstraint = new EcoreConstraint();
				ecoreConstraint.setId(ecoreConstraint.getId()==null?UUID.randomUUID().toString():ecoreConstraint.getId());
//				ecoreConstraint.setId(UUID.randomUUID().toString());
				ecoreConstraint.setObj_id(ecoreDataType.getId());
				ecoreConstraint.setObj_type(ecoreDataType.getType());
				ecoreConstraint.setName(tableItem.getText(0));
				ecoreConstraint.setDefinition(tableItem.getText(1));
				ecoreConstraint.setExpressionlanguage(tableItem.getText(2));
				ecoreConstraint.setExpression(tableItem.getText(3));
				constraints.add(ecoreConstraint);
			}
		}
		dataTypeVO.setEcoreConstraints(constraints);

		java.util.List<EcoreCodeVO> ecoreCodeVOs = new ArrayList<EcoreCodeVO>();
		TreeItem[] treeItems = newCodeTree.getItems();
		
		// Derived Code Set
		if (this.traceId != null) {
			
			ecoreDataType.setTrace(this.traceId);
			
			for (TreeItem treeItem : treeItems) {
				
				if (treeItem.getChecked()) {
					String contentNameText = String.valueOf(treeItem.getData("contentNameText") == null ? "" : treeItem.getData("contentNameText"));
					String contentDocText = String.valueOf(treeItem.getData("contentDocText") == null ? "" : treeItem.getData("contentDocText"));
					String contentCodeNameText = String.valueOf(treeItem.getData("contentCodeNameText") == null ? "" : treeItem.getData("contentCodeNameText"));
					String contentObjectIdentifierValue = String.valueOf(treeItem.getData("contentObjectIdentifierValue") == null ? "" : treeItem.getData("contentObjectIdentifierValue"));
					String contentStatusCombo = String.valueOf(treeItem.getData("contentStatusCombo") == null ? "" : treeItem.getData("contentStatusCombo"));
					Date contentRemovalDate;
					try {
						contentRemovalDate = treeItem.getData("contentRemovalDate") == null ? null : sdf.parse(treeItem.getData("contentRemovalDate").toString());
					} catch (ParseException e) {
						contentRemovalDate = null;
					}
//					String contentExpressionText = String.valueOf(treeItem.getData("contentExpressionText") == null ? "" : treeItem.getData("contentExpressionText"));
//					String contentExpressionLanguageText = String.valueOf(treeItem.getData("contentExpressionLanguageText") == null ? "" : treeItem.getData("contentExpressionLanguageText"));
				
					EcoreCodeVO ecoreCodeVO = new EcoreCodeVO();
					
					if (ecoreDataType.getType().equals(DataTypesEnum.CODE_SETS.getType())) {
						EcoreCode ecoreCode = new EcoreCode();
						ecoreCode.setId(ecoreCode.getId()==null?UUID.randomUUID().toString():ecoreCode.getId());
//						ecoreCode.setId(UUID.randomUUID().toString());
						ecoreCode.setName(contentNameText);
						ecoreCode.setDefinition(contentDocText);
						ecoreCode.setCodeName(contentCodeNameText);
						ecoreCode.setObjectIdentifier(contentObjectIdentifierValue);
						ecoreCode.setRegistrationStatus(contentStatusCombo);
						ecoreCode.setRemovalDate(contentRemovalDate);
						ecoreCode.setCodesetid(ecoreDataType.getId());
						
						ecoreCodeVO.setEcoreCode(ecoreCode);
						//constraints
						java.util.List<EcoreConstraint> contentConstraints = new ArrayList<EcoreConstraint>();
						TableItem[] contentConstraintItems = contentConstraintsTable.getItems();
						if(contentConstraintItems != null && contentConstraintItems.length > 0) {
							for (TableItem tableItem : contentConstraintItems) {
								EcoreConstraint contentEcoreConstraint = new EcoreConstraint();
								contentEcoreConstraint.setId(contentEcoreConstraint.getId()==null?UUID.randomUUID().toString():contentEcoreConstraint.getId());
//								contentEcoreConstraint.setId(UUID.randomUUID().toString());
								contentEcoreConstraint.setObj_id(ecoreCode.getId());
								//-----------------------------------------------------------------
								contentEcoreConstraint.setObj_type(ObjTypeEnum.DataType_CodeSet.getType());
								contentEcoreConstraint.setName(tableItem.getText(0));
								contentEcoreConstraint.setDefinition(tableItem.getText(1));
								contentEcoreConstraint.setExpressionlanguage(tableItem.getText(2));
								contentEcoreConstraint.setExpression(tableItem.getText(3));
								contentConstraints.add(contentEcoreConstraint);
							}
						}
						ecoreCodeVO.setEcoreConstraints(contentConstraints);

						ecoreCodeVOs.add(ecoreCodeVO);
					}
				
				}
			}
		} else {
			for (TreeItem treeItem : treeItems) {
				String contentNameText = String.valueOf(treeItem.getData("contentNameText") == null ? "" : treeItem.getData("contentNameText"));
				String contentDocText = String.valueOf(treeItem.getData("contentDocText") == null ? "" : treeItem.getData("contentDocText"));
				String contentCodeNameText = String.valueOf(treeItem.getData("contentCodeNameText") == null ? "" : treeItem.getData("contentCodeNameText"));
				String contentObjectIdentifierValue = String.valueOf(treeItem.getData("contentObjectIdentifierValue") == null ? "" : treeItem.getData("contentObjectIdentifierValue"));
				String contentStatusCombo = String.valueOf(treeItem.getData("contentStatusCombo") == null ? "" : treeItem.getData("contentStatusCombo"));
				Date contentRemovalDate;
				try {
					contentRemovalDate = treeItem.getData("contentRemovalDate") == null ? null : sdf.parse(treeItem.getData("contentRemovalDate").toString());
				} catch (ParseException e) {
					contentRemovalDate = null;
				}
//				String contentExpressionText = String.valueOf(treeItem.getData("contentExpressionText") == null ? "" : treeItem.getData("contentExpressionText"));
//				String contentExpressionLanguageText = String.valueOf(treeItem.getData("contentExpressionLanguageText") == null ? "" : treeItem.getData("contentExpressionLanguageText"));
			
				EcoreCodeVO ecoreCodeVO = new EcoreCodeVO();
				
				if (ecoreDataType.getType().equals(DataTypesEnum.CODE_SETS.getType())) {
					EcoreCode ecoreCode = new EcoreCode();
					ecoreCode.setId(ecoreCode.getId()==null?UUID.randomUUID().toString():ecoreCode.getId());
//					ecoreCode.setId(UUID.randomUUID().toString());
					ecoreCode.setName(contentNameText);
					ecoreCode.setDefinition(contentDocText);
					ecoreCode.setCodeName(contentCodeNameText);
					ecoreCode.setObjectIdentifier(contentObjectIdentifierValue);
					ecoreCode.setRegistrationStatus(contentStatusCombo);
					ecoreCode.setRemovalDate(contentRemovalDate);
					ecoreCode.setCodesetid(ecoreDataType.getId());
					
					ecoreCodeVO.setEcoreCode(ecoreCode);
					//constraints
					java.util.List<EcoreConstraint> contentConstraints = new ArrayList<EcoreConstraint>();
					TableItem[] contentConstraintItems = contentConstraintsTable.getItems();
					if(contentConstraintItems != null && contentConstraintItems.length > 0) {
						for (TableItem tableItem : contentConstraintItems) {
							EcoreConstraint contentEcoreConstraint = new EcoreConstraint();
							contentEcoreConstraint.setId(contentEcoreConstraint.getId()==null?UUID.randomUUID().toString():contentEcoreConstraint.getId());
//							contentEcoreConstraint.setId(UUID.randomUUID().toString());
							contentEcoreConstraint.setObj_id(ecoreCode.getId());
							//-----------------------------------------------------------------
							contentEcoreConstraint.setObj_type(ObjTypeEnum.DataType_CodeSet.getType());
							contentEcoreConstraint.setName(tableItem.getText(0));
							contentEcoreConstraint.setDefinition(tableItem.getText(1));
							contentEcoreConstraint.setExpressionlanguage(tableItem.getText(2));
							contentEcoreConstraint.setExpression(tableItem.getText(3));
							contentConstraints.add(contentEcoreConstraint);
						}
					}
					ecoreCodeVO.setEcoreConstraints(contentConstraints);

					ecoreCodeVOs.add(ecoreCodeVO);
				}
			}
		}

		
		dataTypeVO.setEcoreCodeVOs(ecoreCodeVOs);
		dataTypeVO.setEcoreNextVersions(null);
		
		return dataTypeVO;
	}

	class AddContentElementListener extends MouseAdapter {
		@Override
		public void mouseUp(MouseEvent e) {
			createMsgElementSelectionDialogue();
		}
	}

	class DeleteContentElementListener extends MouseAdapter {
		Composite rightComposite;

		public DeleteContentElementListener(Composite rightComposite) {
			this.rightComposite = rightComposite;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			// 删除操作
			TreeItem[] selection = newCodeTree.getSelection();
			if (selection != null && selection.length > 0) {
				TreeItem item = selection[0];
				item.dispose();
				if (newCodeTree.getItemCount() == 0) {
					contentNameText.setText("");
					contentDocText.setText("");
					contentCodeNameText.setText("");
//					this.rightComposite.setEnabled(false);
				}
			}
		}
	}

	class OkButtonListener extends MouseAdapter {

		private Table table;

		private Shell shell;

		private Text typeText;

		public OkButtonListener(Text typeText, Table table, Shell shell) {
			this.typeText = typeText;
			this.table = table;
			this.shell = shell;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			TableItem[] tableItemArray = table.getSelection();
			for (TableItem treeItem : tableItemArray) {
				this.typeText.setText(treeItem.getText()==null?"":treeItem.getText());
				this.typeText.setData(String.valueOf(treeItem.getData()));
			}
			shell.close();
		}
	}
	
	@Override
	public void setDirty(boolean dirty) {
		
		if(ecoreDataTypeVO.getEcoreDataType() != null) {
			if (dirty && RegistrationStatus.Registered.getStatus().equals(ecoreDataTypeVO.getEcoreDataType().getRegistrationStatus())) {
				if ("1".equals(ecoreDataTypeVO.getEcoreDataType().getType()) && ecoreDataTypeVO.getEcoreDataType().getTrace() == null) { // 是Code Set，并且是 可衍生的: 可编辑
					super.setDirty(true);
				} else {
					super.setDirty(false);
				}
			} else { // 临时注册和新增可以编辑
				super.setDirty(dirty);
			}
		} else {
			super.setDirty(true);
		}
//		super.setDirty(dirty);
	}
		
	private void setUnavailable(Widget swtWidget) {
//		if (ecoreDataTypeVO.getEcoreDataType() != null && ecoreDataTypeVO.getEcoreDataType().getTrace() != null) {
			if (swtWidget instanceof Text) {
				((Text) swtWidget).setEditable(false);
			} else if (swtWidget instanceof Button) {
				((Button) swtWidget).setEnabled(false);
			}
//		}
	}
	
	private void setAvailable(Widget swtWidget) {
		if (swtWidget instanceof Text) {
			((Text) swtWidget).setEditable(true);
		} else if (swtWidget instanceof Button) {
			((Button) swtWidget).setEnabled(true);
		}
	}
}
