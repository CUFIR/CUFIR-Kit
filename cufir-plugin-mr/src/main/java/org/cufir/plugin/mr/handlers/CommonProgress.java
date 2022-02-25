package org.cufir.plugin.mr.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * 定义一个进度对话框和进度条执行(文件导入的父类)
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public abstract class CommonProgress implements IRunnableWithProgress {

	private IProgressMonitor monitor;

	 // Dialog 标题
	private String title = "";
	
	// 执行的任务名称
	//private String info = "";

	private String note = "";

	private boolean percentable = false;

	public CommonProgress(String taskName) {
		this(taskName, "", false);
	}

	public CommonProgress(String title, boolean percentable) {
		this(title, "", percentable);
	}

	public CommonProgress(String title, String note, boolean percentable) {
		this.title = title;
		this.note = note;
		this.percentable = percentable;
	}

	@Override
	public void run(IProgressMonitor monitor) {
		this.monitor = monitor;
		monitor.setTaskName(note);
		try {
			start();
			excute();
			end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行业务信息，交由子类进行实现
	 */
	public abstract void excute();

	/**
	 * 开始任务并设置任务总数
	 */
	protected void start() {
		int totalStep = IProgressMonitor.UNKNOWN;
		if (percentable) {
			totalStep = 100;
		}
		monitor.beginTask(note, totalStep);
	}

	/**
	 * 执行完毕
	 */
	protected void end() {
		monitor.done();
		totalStep = 100;
	}
	
	private int totalStep = 0;

	/**
	 *  显示进度信息
	 * @param info
	 */
	public void show(String info) {
		// 设置进度信息
		monitor.subTask(info);
	}

	/**
	 *  显示进度信息和百分比信息
	 * @param info
	 */
	public void show(String info, int percent) {
		if (percent > 100) {
			throw new RuntimeException("step不能大于100");
		}
		// 设置进度信息
		monitor.worked(percent);
		totalStep = totalStep + percent;
		if (percentable) {
			monitor.subTask(info + ":(已完成" + totalStep + "%)");
		}
	}

	/**
	 * 启动进程
	 */
	public void process() {
		CommonProgressMonitorDialog dialog = new CommonProgressMonitorDialog(title);
		try {
			// 运行
			dialog.run(true, false, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
