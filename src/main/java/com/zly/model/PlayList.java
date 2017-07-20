package com.zly.model;

public class PlayList {
	private String imgUrl;
	private String title;
	private String playListUrl;
	private String id;
	private String creator;
	public PlayList(String imgUrl, String title, String playListUrl, String id, String creator) {
		this.imgUrl = imgUrl;
		this.title = title;
		this.playListUrl = playListUrl;
		this.id = id;
		this.creator = creator;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlayListUrl() {
		return playListUrl;
	}
	public void setPlayListUrl(String playListUrl) {
		this.playListUrl = playListUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
}
