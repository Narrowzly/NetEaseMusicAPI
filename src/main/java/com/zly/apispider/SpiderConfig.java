package com.zly.apispider;

import java.io.IOException;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

public class SpiderConfig {
	private Logger logger = Logger.getLogger(SpiderConfig.class);
	private String searchUrl;
	private String songUrl;
	private String hotUrl;
	private String playListUrl;
	private String imgListUrl;
	private String topListUrl;
	private String fuckZZWUrl;
	private String userAgent;
	private String type;
	private String limit;
	private Header[] headers;
	private Properties spiderProperties;
	public SpiderConfig() {
		spiderProperties = new Properties();
		try {
			spiderProperties.load(this.getClass().getClassLoader().getResourceAsStream("spider.properties"));
		} catch (IOException e) {
			logger.debug("properties load unsuccessfully!");
		}
		searchUrl = spiderProperties.getProperty("searchUrl");
		songUrl = spiderProperties.getProperty("songUrl");
		hotUrl = spiderProperties.getProperty("hotUrl");
		playListUrl = spiderProperties.getProperty("playListUrl");
		imgListUrl = spiderProperties.getProperty("imgListUrl");
		topListUrl = spiderProperties.getProperty("topListUrl");
		fuckZZWUrl = spiderProperties.getProperty("fuckZZWUrl");
		userAgent = spiderProperties.getProperty("userAgent");
		type = spiderProperties.getProperty("type");
		limit = spiderProperties.getProperty("limit");
		headers = new BasicHeader[5];
		headers[0] = new BasicHeader("Accept", "*");
		headers[1] = new BasicHeader("Accept-Enocding","gzip,deflate");
		headers[2] = new BasicHeader("Accept-Language","en-us,en;q=0.8");
		headers[3] = new BasicHeader("User-Agent",userAgent);
		headers[4] = new BasicHeader("Cookie","appver=2.1.2.185222");
	}
	public String getSearchUrl() {
		return searchUrl;
	}
	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}
	public String getSongUrl() {
		return songUrl;
	}
	public void setSongUrl(String songUrl) {
		this.songUrl = songUrl;
	}
	public String getHotUrl() {
		return hotUrl;
	}
	public void setHotUrl(String hotUrl) {
		this.hotUrl = hotUrl;
	}
	public String getPlayListUrl() {
		return playListUrl;
	}
	public void setPlayListUrl(String playListUrl) {
		this.playListUrl = playListUrl;
	}
	public String getImgListUrl() {
		return imgListUrl;
	}
	public void setImgListUrl(String imgListUrl) {
		this.imgListUrl = imgListUrl;
	}
	public String getTopListUrl() {
		return topListUrl;
	}
	public void setTopListUrl(String topListUrl) {
		this.topListUrl = topListUrl;
	}
	public String getFuckZZWUrl() {
		return fuckZZWUrl;
	}
	public void setFuckZZWUrl(String fuckZZWUrl) {
		this.fuckZZWUrl = fuckZZWUrl;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLimit() {
		return limit;
	}
	public void setLimit(String limit) {
		this.limit = limit;
	}
	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	public Document configHeaders(Connection conn) throws IOException {
		return conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			.header("Accept-Encoding","gzip, deflate, sdch")
			.header("Accept-Language","zh-CN,zh;q=0.8")
			.header("Cache-Control","max-age=0")
			.header("Connection","keep-alive")
			.header("Host","music.163.com")
			.header("Refer","http://music.163.com/")
			.header("Upgrade-Insecure-Requests","1")
			.userAgent(userAgent).get();

	}
}
