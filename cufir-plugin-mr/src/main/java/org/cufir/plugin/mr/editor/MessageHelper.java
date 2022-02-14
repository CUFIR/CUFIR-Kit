package org.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.s.data.bean.EcoreConstraint;
import org.cufir.s.data.bean.EcoreExample;
import org.cufir.s.data.bean.EcoreMessageDefinition;
import org.cufir.s.data.bean.EcoreNextVersions;
import org.cufir.s.data.bean.EcoreSemanticMarkupElement;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.springframework.util.StringUtils;

/**
 * 生成实例和约束（合并）
 * 
 * @author gongyi_tt
 *
 */
public class MessageHelper {
	
	public static List<EcoreNextVersions> getPreviousVersions(Map<String, EcoreNextVersions> envMapByNextVersionId, String id, List<EcoreNextVersions> list){
		EcoreNextVersions ecoreNextVersions = envMapByNextVersionId.get(id);
		if(ecoreNextVersions != null) {
			list.add(ecoreNextVersions);
			getPreviousVersions(envMapByNextVersionId, ecoreNextVersions.getId(), list);
		}
		return list;
	}

	public static void generateExamplesTable(Table examplesTable, String id) {
		examplesTable.removeAll();
		ArrayList<EcoreExample> ecoreExampleList = MrHelper.getExample(id);
		for (EcoreExample ecoreExample : ecoreExampleList) {
			TableItem tableItem = new TableItem(examplesTable, SWT.NONE);
			tableItem.setText(ecoreExample.getExample());
			tableItem.setData(ecoreExample);
		}
	}

	/**
	 * 生成约束
	 * @param constraintsTable
	 * @param id
	 * @param mbbTreeItem
	 */
	@SuppressWarnings("unchecked")
	public static void generateConstraintTable(Table constraintsTable, String id, TreeItem mbbTreeItem) {
		constraintsTable.removeAll();
		List<EcoreConstraint> ecoreConstraintList = new ArrayList<>();
		if (mbbTreeItem != null && mbbTreeItem.getData("mbbConstraintList") != null) {
			ecoreConstraintList = (ArrayList<EcoreConstraint>) mbbTreeItem.getData("mbbConstraintList");
		} else if (id != null) {
			ecoreConstraintList = MrHelper.findConstraintsByObjId(MrRepository.get().ecoreConstraints, id);
		}
		for (EcoreConstraint ecoreConstraint : ecoreConstraintList) {
			TableItem tableItem = new TableItem(constraintsTable, SWT.NONE);
			tableItem.setText(new String[] { ecoreConstraint.getName(), ecoreConstraint.getDefinition(),
					ecoreConstraint.getExpressionlanguage(), ecoreConstraint.getExpression() });
			tableItem.setData(ecoreConstraint);
		}
	}

	/**
	 * 
	 * @param synonymsTable
	 * @param id
	 * @param mbbTreeItem
	 */
	@SuppressWarnings("unchecked")
	public static void generateSynonymsTable(Table synonymsTable, String id, TreeItem mbbTreeItem) {
		synonymsTable.removeAll();
		if (mbbTreeItem != null && mbbTreeItem.getData("mbbSynonymsTableItems") != null) {
			ArrayList<String> keyValueList = (ArrayList<String>) mbbTreeItem.getData("mbbSynonymsTableItems");
			for (String keyValue : keyValueList) {
				TableItem tableItem = new TableItem(synonymsTable, SWT.NONE);
				tableItem.setText(new String[] { keyValue.split(",")[0], keyValue.split(",")[1] });
			}
		} else if (id != null) {
			List<EcoreSemanticMarkupElement> synonymsList = MrHelper
					.findSemanticMarkupElementBySemanticMarkupObjId(MrRepository.get().ecoreSemanticMarkups,
							MrRepository.get().ecoreSemanticMarkupElements, id);
			for (EcoreSemanticMarkupElement synonyms : synonymsList) {
				TableItem tableItem = new TableItem(synonymsTable, SWT.NONE);
				tableItem.setText(new String[] { synonyms.getName(), synonyms.getValue() });
			}
		}
	}
	
	/**
	 * 生成名称
	 * @param name
	 * @param mds
	 * @return
	 */
	public static void generateName(Text nameValue, List<EcoreMessageDefinition> mds) {
		try {
			if (nameValue.getText().equals("")) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), "提示", "请先输入命名Name!");
			} else {
				int count = MrImplManager.get().getEcoreMessageDefinitionImpl().countByName(nameValue.getText());
				//名称存在
				if(count > 0) {
					String version = "";
					String pat = "V\\d+";
					Pattern p = Pattern.compile(pat);
					String[] split = p.split(nameValue.getText());
					String previousVersion = getVersion(nameValue.getText(), p, split);
					if("".equals(previousVersion)) {
						throw new RuntimeException("报文名称不规范,示例TestV01,V01是版本号！");
					}else {
						if(StringUtils.isEmpty(previousVersion)) {
							version = "V01";
						}else {
							List<EcoreMessageDefinition> names = mds.stream().filter(md -> md.getName().contains(split[0]) && md.getName().length() == split[0].length() + 3).collect(Collectors.toList());
							names = names.stream().sorted(Comparator.comparing(EcoreMessageDefinition::getName).reversed()).collect(Collectors.toList());
							version = names.get(0).getName().replace(split[0], "");
							int i = Integer.parseInt("" + version.charAt(1) + version.charAt(2));
							i++;
							if(i < 10) {
								version = "V0" + i;
							}else {
								version = "V" + i;
							}
						}
					}
					nameValue.setText(split[0] + version);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取版本号
	 * @param name
	 * @param p
	 * @param split
	 * @return
	 */
	public static String getVersion(String name, Pattern p, String[] split) {
		Matcher m = p.matcher(name);
		int count =0;
		while(m.find()) {
			count++;
		}
		if(count > 1) {
			throw new RuntimeException("报文名称不规范,版本号只可以出现一次！");
		}
		String version = "";
		if(split.length == 1) {
			version = name.replace(split[0], "");
		}else if(split.length == 2){
			version = name.replace(split[0], "");
			version = version.replace(split[1], "");
		}else {
			throw new RuntimeException("报文名称不规范,版本号只可以出现一次！");
		}
		return version;
	}
}
