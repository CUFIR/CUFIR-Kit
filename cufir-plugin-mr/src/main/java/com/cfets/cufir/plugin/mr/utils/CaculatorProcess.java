package com.cfets.cufir.plugin.mr.utils;


/**
 *  计算
 * @author TangTao
 * @deprecated
 */
public class CaculatorProcess extends CufirProgress {

	public CaculatorProcess(String name,boolean percent) {
		super(name,percent);
	}
	
	public CaculatorProcess(String name,String note,boolean percent) {
		super(name,note,percent);
	}

	@Override
	public void excute() {
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
//				int p = i*100/10;
//				String info = "process-" + i + " " + p + "%";
				String info = "process-" + i;
				// 复用父类
				this.show(info,10);
				// process.setPercent();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
