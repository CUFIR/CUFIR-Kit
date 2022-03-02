package org.cufir.plugin.mr.editor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.cufir.plugin.mr.MrHelper;
import org.cufir.s.ecore.bean.EcoreConstraint;
import org.cufir.s.ecore.bean.EcoreExample;
import org.cufir.s.ecore.bean.EcoreMessageDefinition;
import org.cufir.s.ecore.bean.EcoreNextVersions;
import org.cufir.s.ecore.bean.EcoreSemanticMarkupElement;
import org.cufir.s.ide.i18n.I18nApi;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;
import org.springframework.util.StringUtils;

/**
 * 编辑帮助
 */
public class MrEditorHelper {

	/**
	 * 获取上级版本
	 * @param envMapByNextVersionId
	 * @param id
	 * @param list
	 * @return
	 */
	public static List<EcoreNextVersions> getPreviousVersions(Map<String, EcoreNextVersions> envMapByNextVersionId, String id, List<EcoreNextVersions> list){
		EcoreNextVersions ecoreNextVersions = envMapByNextVersionId.get(id);
		if(ecoreNextVersions != null) {
			list.add(ecoreNextVersions);
			getPreviousVersions(envMapByNextVersionId, ecoreNextVersions.getId(), list);
		}
		return list;
	}
	
	/**
	 * 设置实例表单
	 * @param examplesTable
	 * @param id
	 */
	public static void setExamplesTable(Table examplesTable, String id) {
		examplesTable.removeAll();
		ArrayList<EcoreExample> ecoreExampleList = MrHelper.getExample(id);
		for (EcoreExample ecoreExample : ecoreExampleList) {
			TableItem tableItem = new TableItem(examplesTable, SWT.NONE);
			tableItem.setText(ecoreExample.getExample());
			tableItem.setData(ecoreExample);
		}
	}
	
	/**
	 * 设置约束表单
	 * @param constraintsTable
	 * @param id
	 * @param mbbTreeItem
	 */
	@SuppressWarnings("unchecked")
	public static void setConstraintTable(Table constraintsTable, String id, TreeItem mbbTreeItem) {
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
	 * 设置同义词表单
	 * @param synonymsTable
	 * @param id
	 * @param mbbTreeItem
	 */
	@SuppressWarnings("unchecked")
	public static void setSynonymsTable(Table synonymsTable, String id, TreeItem mbbTreeItem) {
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
	public static String generateName(String name, List<EcoreMessageDefinition> mds) {
		String newName = "";
		try {
			if (name.equals("")) {
				MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.error.name.null"));
			} else {
				int count = MrImplManager.get().getEcoreMessageDefinitionImpl().countByName(name);
				//名称存在
				if(count > 0) {
					String version = "";
					String pat = "V\\d+";
					Pattern p = Pattern.compile(pat);
					String[] split = p.split(name);
					String previousVersion = getVersion(name, p, split);
					if("0".equals(previousVersion)) {
						return newName;
					}
					if("".equals(previousVersion)) {
						MessageDialog.openWarning(Display.getDefault().getActiveShell(), I18nApi.get("tips.name"), I18nApi.get("tips.md.error.name.one"));
					}else {
						if(StringUtils.isEmpty(previousVersion)) {
							version = "V01";
						}else {
							List<EcoreMessageDefinition> names = mds.stream().filter(md -> md.getName().contains(split[0]) && md.getName().length() == split[0].length() + 3).collect(Collectors.toList());
							if(names != null && names.size() > 0) {
								names = names.stream().sorted(Comparator.comparing(EcoreMessageDefinition::getName).reversed()).collect(Collectors.toList());
								version = names.get(0).getName().replace(split[0], "");
								int i = Integer.parseInt("" + version.charAt(1) + version.charAt(2));
								i++;
								if(i < 10) {
									version = "V0" + i;
								}else {
									version = "V" + i;
								}
							}else {
								version = "V01";
							}
						}
					}
					newName = split[0] + version;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newName;
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
			return "0";
		}
		String version = "";
		if(split.length == 1) {
			version = name.replace(split[0], "");
		}else if(split.length == 2){
			version = name.replace(split[0], "");
			version = version.replace(split[1], "");
		}else {
			return "0";
		}
		return version;
	}
}
