package org.cufir.plugin.mr.handlers;

/**
 * 进展监听接口
 */
public interface IProgressListener {

	
	public void onEnd();
	
	
	public void onProgressing(String info,int percent);
}
