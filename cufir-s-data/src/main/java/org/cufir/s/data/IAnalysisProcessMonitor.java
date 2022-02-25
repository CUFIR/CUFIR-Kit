package org.cufir.s.data;

/**
 * 解析处理过程监听
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public interface IAnalysisProcessMonitor {
	
	/**
	 * 提示信息和完成步数
	 * @param info
	 * @param percent
	 */
	public void info(String info,int percent);
}
