package com.zly.model;

import java.util.List;

public class EncrypterSong {
	private List<String> ids;
	private String br;
	private String crsf_token;
	public EncrypterSong(List<String> ids, String br, String crsf_token) {
		this.ids = ids;
		this.br = br;
		this.crsf_token = crsf_token;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public String getBr() {
		return br;
	}
	public void setBr(String br) {
		this.br = br;
	}
	public String getCrsf_token() {
		return crsf_token;
	}
	public void setCrsf_token(String crsf_token) {
		this.crsf_token = crsf_token;
	}
	
}
