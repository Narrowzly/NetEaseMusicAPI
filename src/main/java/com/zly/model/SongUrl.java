package com.zly.model;

public class SongUrl {
	private String songUrl;
	private String id;
	public SongUrl(String songUrl, String id) {
		this.songUrl = songUrl;
		this.id = id;
	}
	public String getSongUrl() {
		return songUrl;
	}
	public void setSongUrl(String songUrl) {
		this.songUrl = songUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
