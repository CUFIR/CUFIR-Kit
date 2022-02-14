package org.cufir.plugin.mr.handlers;

import org.cufir.plugin.mr.editor.MrRepository;
import org.cufir.plugin.mr.editor.SummaryProgressComposite;
import org.cufir.s.data.IAnalysisProcessMonitor;
import org.eclipse.core.runtime.Platform;

import com.cfets.cufir.s.ide.utils.i18n.I18nApi;

/**
 * 打开文件创建进程
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月17日
 */
public class MrRepositoryInitProcess extends CommonProgress implements IAnalysisProcessMonitor{

	private IProcessListener listener;
	
	private SummaryProgressComposite summaryProgressComposite;

	public MrRepositoryInitProcess(IProcessListener listener,SummaryProgressComposite summaryProgressComposite) {
		super("CUFIR初始化:");
		this.listener = listener;
		this.summaryProgressComposite = summaryProgressComposite;
	}
	
	public MrRepositoryInitProcess() {
		super("CUFIR初始化结束:");
	}

	@Override
	public void excute() {
		I18nApi.init(Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//") + "config//i18n");
		MrRepository.get().init(this, summaryProgressComposite.getPb());
	}
	
	@Override
	public void process() {
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
