package com.zly.model;

import java.util.ArrayList;
import java.util.List;

public class SongList {
	private String name;
	private String id;
	private String imgUrl;
	private String songsCount;
	private String playCount;
	private List<Song> songs;
	public SongList(String name, String id, String imgUrl, String playCount, String songsCount) {
		this.name = name;
		this.id = id;
		this.imgUrl = imgUrl;
		this.playCount = playCount;
		this.songsCount = songsCount;
		songs = new ArrayList<>();
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public List<Song> getSongs() {
		return songs;
	}
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
	public String getSongsCount() {
		return songsCount;
	}
	public void setSongsCount(String songsCount) {
		this.songsCount = songsCount;
	}
	public String getPlayCount() {
		return playCount;
	}
	public void setPlayCount(String playCount) {
		this.playCount = playCount;
	}
	public void addSong(Song song) {
		songs.add(song);
	}
}
