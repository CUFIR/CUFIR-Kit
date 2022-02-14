package org.cufir.plugin.mr.handlers;

/**
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年12月9日
 */
public interface IProcessListener {

	
	public void onEnd();
	
	
	public void onProcessing(String info,int percent);
}
