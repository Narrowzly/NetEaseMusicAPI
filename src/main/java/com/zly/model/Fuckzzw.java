package com.zly.model;

import java.util.List;

public class Fuckzzw {
	private String id;
	private String name;
	private String coverUrl;
	private List<Song> songs;
	public Fuckzzw(String id, String name, String coverUrl) {
		this.id = id;
		this.name = name;
		this.coverUrl = coverUrl;
	}
	public Fuckzzw(String id, String name, String coverUrl, List<Song> songs) {
		super();
		this.id = id;
		this.name = name;
		this.coverUrl = coverUrl;
		this.songs = songs;
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
	public String getCoverUrl() {
		return coverUrl;
	}
	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}
	public List<Song> getSongs() {
		return songs;
	}
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
}
