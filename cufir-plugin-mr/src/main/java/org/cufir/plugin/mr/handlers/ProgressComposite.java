package org.cufir.plugin.mr.handlers;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * 进展组件
 */
public class ProgressComposite {

	private Label noteLb, countLb;
	
	private ProgressBar pb;

	public Label getNoteLb() {
		return noteLb;
	}

	public Label getCountLb() {
		return countLb;
	}

	public ProgressBar getPb() {
		return pb;
	}

	public ProgressComposite(Composite parent) {
		
		Color white = new Color(Display.getCurrent(), 255, 255, 255);
		
		noteLb = new Label(parent, SWT.LEFT);
		noteLb.setBounds(20, 295, 300, 20);
		noteLb.setForeground(white);
		
		pb = new ProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH);
		pb.setBounds(20, 320, 520, 20);
		pb.setMinimum(0);
		pb.setMaximum(100);
		
		countLb = new Label(parent, SWT.LEFT);
		countLb.setBounds(545, 320, 35, 20);
		countLb.setForeground(white);
		countLb.setText("0%");
	}
}
