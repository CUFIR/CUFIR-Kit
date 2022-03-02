package org.cufir.plugin.mr.handlers;

import org.cufir.plugin.mr.editor.MrRepository;
import org.cufir.s.data.IAnalysisProgressMonitor;
import org.cufir.s.ide.i18n.I18nApi;
import org.eclipse.core.runtime.Platform;

/**
 * 打开文件创建进程
 */
public class MrRepositoryInitProgress extends CommonProgress implements IAnalysisProgressMonitor{

	private IProgressListener listener;
	
	private ProgressComposite summaryProgressComposite;

	public MrRepositoryInitProgress(IProgressListener listener,ProgressComposite summaryProgressComposite) {
		super("CUFIR初始化:");
		this.listener = listener;
		this.summaryProgressComposite = summaryProgressComposite;
	}
	
	public MrRepositoryInitProgress() {
		super("CUFIR初始化结束:");
	}

	@Override
	public void excute() {
		I18nApi.init(Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//") + "config//i18n");
		MrRepository.get().init(this, summaryProgressComposite.getPb());
	}
	
	@Override
	public void progress() {
		excute();
	}

	@Override
	public void end() {
		listener.onEnd();
	}

	@Override
	public void info(String note, int percent) {
		summaryProgressComposite.getPb().setSelection(percent);
		summaryProgressComposite.getNoteLb().setText(note);
		summaryProgressComposite.getCountLb().setText(percent + "%");
	}
}
