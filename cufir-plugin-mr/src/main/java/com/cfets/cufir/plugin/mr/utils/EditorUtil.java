package com.cfets.cufir.plugin.mr.utils;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.part.NullEditorInput;

import com.cfets.cufir.plugin.mr.bean.TransferDataBean;
import com.cfets.cufir.plugin.mr.editor.MyEditorInput;
import com.cfets.cufir.plugin.mr.enums.DataTypesEnum;
import com.cfets.cufir.plugin.mr.enums.TreeParentEnum;

/**
 * editor工具类
 * @author jin.c.li
 *
 */
public class EditorUtil {
	
//	public static final String OPEN_STATUS = "0";
//	public static final String CREATE_STATUS = "1";
	
	//DT-创建
	public static String DT_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.DataTypesCreateEditor";
	
	//BC-创建
	public static String BC_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.BusinessComponentCreateEditor";
	
	//ES-创建
	public static String ES_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.ExternalSchemasCreateEditor";
	
	//MC-创建
	public static String MC_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.MessageComponentCreateEditor";
	
	//BA-创建
	public static String BA_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.BusinessAreaCreateEditor";
	
	//MS-创建
	public static String MS_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.MessageSetCreateEditor";
	
	//MD-创建
	public static String MD_EDITOR_CREATE = "com.cfets.cufir.plugin.mr.editor.MessageDefinitionCreateEditor";
	
	
	/**
	 * 打开editor编辑器
	 * @param status 0-打开/编辑  1-新建
	 * @param transferDataBean
	 */
	public static void open(String tabType, TransferDataBean transferDataBean) {
		
		String editor_open_id = BC_EDITOR_CREATE;
		
		if(DataTypesEnum.checkTypeName(tabType)) {
			editor_open_id = DT_EDITOR_CREATE;
		}else if(tabType.equals(TreeParentEnum.BUSINESS_COMPONENTS.getName())){
			editor_open_id = BC_EDITOR_CREATE;
		}else if(tabType.equals(TreeParentEnum.EXTERNAL_SCHEMAS.getName())){
			editor_open_id = ES_EDITOR_CREATE;
		}else if(tabType.equals(TreeParentEnum.MESSAGE_COMPONENTS.getName())){
			editor_open_id = MC_EDITOR_CREATE;
		}else if(tabType.equals(TreeParentEnum.BUSINESS_AREAS.getName())){
			editor_open_id = BA_EDITOR_CREATE;
		}else if(tabType.equals(TreeParentEnum.MESSAGE_SETS.getName())){
			editor_open_id = MS_EDITOR_CREATE;
		}else if(tabType.equals(TreeParentEnum.MESSAGE_DEFINITIONS.getName())){
			editor_open_id = MD_EDITOR_CREATE;
		}
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorInput input = new MyEditorInput(transferDataBean);
		try {
			IEditorReference[] viewReferences = page.getEditorReferences();
            for (IEditorReference ier : viewReferences) {
            	IEditorInput editorInput = ier.getEditorInput();
            	if(!(editorInput instanceof NullEditorInput)) {
            		TransferDataBean transferDataBeanEditor = ((MyEditorInput)editorInput).getTransferDataBean();
            		// 获取editorId
            		if (ier.getId().equalsIgnoreCase(editor_open_id) && transferDataBeanEditor.equals(transferDataBean)) {
            			// 关闭editor窗口
//            			page.hideEditor(ier);
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
