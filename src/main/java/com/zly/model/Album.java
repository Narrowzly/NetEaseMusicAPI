package com.zly.model;

public class Album {
	private String id;
	private String name;
	private String picUrl;
	public Album(String id, String name, String picUrl) {
		this.id = id;
		this.name = name;
		this.picUrl = picUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
