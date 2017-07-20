package com.zly.model;

import java.util.List;


public class ZZW {
	private List<Fuckzzw> special;
	private List<Fuckzzw> global;
	public ZZW(List<Fuckzzw> special, List<Fuckzzw> global) {
		this.special = special;
		this.global = global;
	}
	public List<Fuckzzw> getSpecial() {
		return special;
	}
	public void setSpecial(List<Fuckzzw> special) {
		this.special = special;
	}
	public List<Fuckzzw> getGlobal() {
		return global;
	}
	public void setGlobal(List<Fuckzzw> global) {
		this.global = global;
	}
}
