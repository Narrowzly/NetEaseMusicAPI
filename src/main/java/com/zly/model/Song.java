package com.zly.model;

import java.util.List;

public class Song {
	private String name;
	private String id;
	private Album al;
	private List<Artist> ar;
	public Song(String name, String id, Album al, List<Artist> ar) {
		this.name = name;
		this.id = id;
		this.al = al;
		this.ar = ar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Album getAl() {
		return al;
	}
	public void setAl(Album al) {
		this.al = al;
	}
	public List<Artist> getAr() {
		return ar;
	}
	public void setAr(List<Artist> ar) {
		this.ar = ar;
	}
}
