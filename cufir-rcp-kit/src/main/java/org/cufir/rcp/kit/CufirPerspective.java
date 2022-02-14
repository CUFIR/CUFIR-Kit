package org.cufir.rcp.kit;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class CufirPerspective implements IPerspectiveFactory {


	/**
	 * Constructs a new Default layout engine.
	 */
//	public CufirPerspective() {
//		super();
//	}

	@Override
	public void createInitialLayout(IPageLayout layout) {
//		layout.setEditorAreaVisible(true);
// 		String editorArea = layout.getEditorArea();
//
//		IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
//		IFolderLayout outputfolder= layout.createFolder("bottom", IPageLayout.BOTTOM, (float)0.75, editorArea); //$NON-NLS-1$
//		
//		
//		folder.addPlaceholder(IPageLayout.ID_PROJECT_EXPLORER);
//
//		outputfolder.addView(IPageLayout.ID_PROBLEM_VIEW);
//		outputfolder.addPlaceholder(IConsoleConstants.ID_CONSOLE_VIEW);
//		outputfolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
//		outputfolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
//
//		IFolderLayout outlineFolder = layout.createFolder("right", IPageLayout.RIGHT, (float)0.25, editorArea); //$NON-NLS-1$
//		outlineFolder.addView(IPageLayout.ID_OUTLINE);
//
//		outlineFolder.addPlaceholder(TemplatesView.ID);
//		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
//
//
//		// views - debugging
//		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
//
//		// views - standard workbench
//		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
//		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
//		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
//		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
//		layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
//		layout.addShowViewShortcut(TemplatesView.ID);
//		layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView"); //$NON-NLS-1$
		
		
	}
}
