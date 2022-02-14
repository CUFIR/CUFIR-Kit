package com.cfets.cufir.s.data;

public class TestExportMDR3 {

	public static void main(String[] args) throws Exception {
		MDR3Mgr mgr=new MDR3Mgr();
		mgr.generateMDR3Excel("b5297dc3-ea5d-491c-897c-5b1d70435e00","Bank Services Billing"
				,"E:\\BankServicesBilling.xls");
	}
}
