package com.AngelBarreraSanchez.ccam.util;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;

public class ClineTester {
	
	private ClineTester() {
	}

	private static ClineTester clineTester;

	public static ClineTester getInstance() {
		if (clineTester == null) {
			clineTester = new ClineTester();
		}
		return clineTester;
	}

	public boolean isClineActive(CCCAMEntity line) {
		boolean online = false;
		try{
			online = new CCcamConnector(line).testCline();
			return online;
		}catch(Exception ex){
			return false;
		}
	}
}
