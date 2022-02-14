package org.cufir.plugin.mr.editor;

import org.cufir.plugin.mr.bean.DataTypesEnum;
import org.cufir.plugin.mr.bean.TransferDataBean;
import org.cufir.plugin.mr.bean.TreeMenuEnum;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.part.NullEditorInput;

/**
 * editor工具类
 * @author jin.c.li
 *
 */
@SuppressWarnings("restriction")
public class EditorUtil {
	
	//DT-创建
	public static String DT_EDITOR_CREATE = "org.cufir.plugin.mr.editor.DataTypesEditor";
	
	//BC-创建
	public static String BC_EDITOR_CREATE = "org.cufir.plugin.mr.editor.BusinessComponentEditor";
	
	//ES-创建
	public static String ES_EDITOR_CREATE = "org.cufir.plugin.mr.editor.ExternalSchemasEditor";
	
	//MC-创建
	public static String MC_EDITOR_CREATE = "org.cufir.plugin.mr.editor.MessageComponentEditor";
	
	//BA-创建
	public static String BA_EDITOR_CREATE = "org.cufir.plugin.mr.editor.BusinessAreaEditor";
	
	//MS-创建
	public static String MS_EDITOR_CREATE = "org.cufir.plugin.mr.editor.MessageSetEditor";
	
	//MD-创建
	public static String MD_EDITOR_CREATE = "org.cufir.plugin.mr.editor.MessageDefinitionEditor";
	
	
	/**
	 * 打开editor编辑器
	 * @param status 0-打开/编辑  1-新建
	 * @param transferDataBean
	 */
	public static void open(String tabType, TransferDataBean transferDataBean) {
		
		String editor_open_id = BC_EDITOR_CREATE;
		
		if(DataTypesEnum.checkTypeName(tabType)) {
			editor_open_id = DT_EDITOR_CREATE;
		}else if(tabType.equals(TreeMenuEnum.BUSINESS_COMPONENTS.getName())){
			editor_open_id = BC_EDITOR_CREATE;
		}else if(tabType.equals(TreeMenuEnum.EXTERNAL_SCHEMAS.getName())){
			editor_open_id = ES_EDITOR_CREATE;
		}else if(tabType.equals(TreeMenuEnum.MESSAGE_COMPONENTS.getName())){
			editor_open_id = MC_EDITOR_CREATE;
		}else if(tabType.equals(TreeMenuEnum.BUSINESS_AREAS.getName())){
			editor_open_id = BA_EDITOR_CREATE;
		}else if(tabType.equals(TreeMenuEnum.MESSAGE_SETS.getName())){
			editor_open_id = MS_EDITOR_CREATE;
		}else if(tabType.equals(TreeMenuEnum.MESSAGE_DEFINITIONS.getName())){
			editor_open_id = MD_EDITOR_CREATE;
		}
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorInput input = new MrEditorInput(transferDataBean);
		try {
			IEditorReference[] viewReferences = page.getEditorReferences();
            for (IEditorReference ier : viewReferences) {
            	IEditorInput editorInput = ier.getEditorInput();
            	if(!(editorInput instanceof NullEditorInput)) {
            		TransferDataBean transferDataBeanEditor = ((MrEditorInput)editorInput).getTransferDataBean();
            		// 获取editorId
            		if (ier.getId().equalsIgnoreCase(editor_open_id) && transferDataBeanEditor.equals(transferDataBean)) {
            			input = editorInput;
            		}
            	}
            }
			page.openEditor(input, editor_open_id);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
