	package com.zly.apispider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zly.apispider.encrypt.Encrypter;
import com.zly.model.Album;
import com.zly.model.Artist;
import com.zly.model.Image;
import com.zly.model.PlayList;
import com.zly.model.Song;
import com.zly.model.SongList;
import com.zly.model.Fuckzzw;
import com.zly.server.FuckZZW;

public class NetEaseMusicSpider {
	private static Logger logger = Logger.getLogger(NetEaseMusicSpider.class);
	private static CloseableHttpClient client = null;
	private static String searchUrl = "http://music.163.com/api/search/pc";
	private static String songUrl = "http://music.163.com/weapi/song/enhance/player/url?csrf_token=";
	private static String hotUrl = "http://music.163.com/discover/playlist/?order=hot&limit=";
	private static String playListUrl = "http://music.163.com/playlist?id=";
	private static String imgListUrl = "https://music.163.com/discover";
	private static String topListUrl = "https://music.163.com/discover/toplist?id=";
	private static String fuckZZWUrl = "http://music.163.com/discover/toplist";	
	private static Encrypter encrypter = null;
	private static String type = "1";
	private static String limit = "20";
	private static Header[] headers = null;
	private static NetEaseMusicSpider spider;
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.157 Safari/537.36";
	private NetEaseMusicSpider() {
		
	}
	public static NetEaseMusicSpider getInstance() {
		return spider!=null?spider:new NetEaseMusicSpider();
	}
	public NetEaseMusicSpider init() {
		client = HttpClients.createDefault(); 
		encrypter = Encrypter.getInstance();
		headers = new BasicHeader[5];
		headers[0] = new BasicHeader("Accept", "*");
		headers[1] = new BasicHeader("Accept-Enocding","gzip,deflate");
		headers[2] = new BasicHeader("Accept-Language","en-us,en;q=0.8");
		headers[3] = new BasicHeader("User-Agent",userAgent);
		headers[4] = new BasicHeader("Cookie","appver=2.1.2.185222");
		return this;
	}
	public List<Song> search(String keyword, String pageNum) throws ParseException, IOException {
		HttpPost post = new HttpPost(searchUrl);
		HttpResponse response = null;
		String offset = String.valueOf(Integer.parseInt(pageNum)*20);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("limit", limit));
		params.add(new BasicNameValuePair("offset", offset));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("s", keyword));
		post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		post.setHeaders(headers);
		try {
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		List<Song> songList = new ArrayList<>();
		JSONObject jsonsong = JSON.parseObject(EntityUtils.toString(response.getEntity()));
		JSONArray songsArray = jsonsong.getJSONObject("result").getJSONArray("songs");
		for(int i=0;i<songsArray.size();i++) {
			JSONObject songDetail = songsArray.getJSONObject(i);
			String id = songDetail.getString("id");
			String name = songDetail.getString("name");
			String albumName = songDetail.getJSONObject("album").getString("name");
			String albumId = songDetail.getJSONObject("album").getString("id");
			String imgUrl = songDetail.getJSONObject("album").getString("picUrl");
			JSONArray artistsArray = songDetail.getJSONArray("artists");
			List<Artist> artists = new ArrayList<>();
			for(int j=0;j<artistsArray.size();j++) {
				JSONObject artist = artistsArray.getJSONObject(j);
				String artistName = artist.getString("name");
				String artistId = artist.getString("id");
				artists.add(new Artist(artistId, artistName));
			}
			Album album = new Album(albumId, albumName, imgUrl);
			songList.add(new Song(name, id, album, artists));
		}
		return songList;
	}
	public String getSongUrl(String id) {
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{\"ids\":");
		jsonBuilder.append("["+id+"],");
		jsonBuilder.append("\"br\":");
		jsonBuilder.append("320000,");
		jsonBuilder.append("\"crsf_token\":");
		jsonBuilder.append("''}");
		String[] postparams = encrypter.encryptedRequest(jsonBuilder.toString());
		HttpPost post = new HttpPost(songUrl);
		post.setHeaders(headers);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("params", postparams[0]));
		params.add(new BasicNameValuePair("encSecKey", postparams[1]));
		post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		try {
			JSONObject jsonSong = JSON.parseObject(EntityUtils.toString(client.execute(post).getEntity()));
			JSONObject jsonSongDetail = jsonSong.getJSONArray("data").getJSONObject(0);
			return jsonSongDetail.getString("url");
		} catch (ParseException e) {
			logger.error(e);
		} catch (ClientProtocolException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return "nothing";
	}
	public List<PlayList> getPlayList(String limit, String page) throws IOException{
		Document doc = Jsoup.connect(hotUrl+limit+"&offset="+Integer.parseInt(page)*Integer.parseInt(limit))
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding","gzip, deflate, sdch")
				.header("Accept-Language","zh-CN,zh;q=0.8")
				.header("Cache-Control","max-age=0")
				.header("Connection","keep-alive")
				.header("Host","music.163.com")
				.header("Refer","http://music.163.com/")
				.header("Upgrade-Insecure-Requests","1")
				.userAgent(userAgent).get();
		Element playListFather = doc.getElementById("m-pl-container");
		Elements playLists = playListFather.children();
		List<PlayList> p = new ArrayList<>();
		for(Element playList:playLists) {
			Element img = playList.getElementsByClass("j-flag").first();
			Element a = playList.getElementsByClass("msk").first();
			String imgUrl = img.attr("src");
			String title = a.attr("title");
			String playListUrl = a.attr("href");
			String id = playListUrl.split("=")[1];
			p.add(new PlayList(imgUrl, title, playListUrl, id));
		}
		return p;
	}
	public SongList getSongList(String num) throws IOException{
		Document doc = Jsoup.connect(playListUrl+num).userAgent(userAgent).get();
		String title = doc.title();
		String listId = doc.getElementById("content-operation").attr("data-rid");
		String coverUrl = doc.getElementsByClass("j-img").first().attr("src");
		String playCount = doc.getElementById("play-count").text();
		String songsCount = doc.getElementById("playlist-track-count").text();
		Element songListFather = doc.getElementsByTag("textarea").first();
		JSONArray songList = JSON.parseArray(songListFather.text());
		SongList l = new SongList(title, listId, coverUrl, playCount, songsCount);
		for(int i=0;i<songList.size();i++) {
			JSONObject song = songList.getJSONObject(i);
			String id = song.getString("id");
			String name = song.getString("name");
			String albumName = song.getJSONObject("album").getString("name");
			String albumId = song.getJSONObject("album").getString("id");
			String imgUrl = song.getJSONObject("album").getString("picUrl");
			JSONArray artistsArray = song.getJSONArray("artists");
			List<Artist> artists = new ArrayList<>();
			for(int j=0;j<artistsArray.size();j++) {
				JSONObject artist = artistsArray.getJSONObject(j);
				String artistName = artist.getString("name");
				String artistId = artist.getString("id");
				artists.add(new Artist(artistId, artistName));
			}
			Album album = new Album(albumId, albumName, imgUrl);
			l.addSong(new Song(name, id, album, artists));
		}
		return l;
	}
	public List<Image> getImgList() throws IOException {
		Document doc = Jsoup.connect(imgListUrl)
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding","gzip, deflate, sdch")
				.header("Accept-Language","zh-CN,zh;q=0.8")
				.header("Cache-Control","max-age=0")
				.header("Connection","keep-alive")
				.header("Host","music.163.com")
				.header("Refer","http://music.163.com/")
				.header("Upgrade-Insecure-Requests","1")
				.userAgent(userAgent).get();
		Element brotherFatherList = doc.getElementById("g_backtop");
		Element fatherList = brotherFatherList.nextElementSibling();
		String jsonArray = fatherList.outerHtml().split(";")[0].split("Gbanners\\s+=")[1];//Jsoup无法解析script的文本内容
		JSONArray imgArray = JSON.parseArray(jsonArray);
		List<Image> imageList = new ArrayList<>();
		for(int i=0;i<imgArray.size();i++) {
			JSONObject img = imgArray.getJSONObject(i);
			imageList.add(new Image(img.getString("picUrl")));
		}
		return imageList;
	}
	public List<Fuckzzw> fuckZZW() throws IOException {
		Document doc = Jsoup.connect(fuckZZWUrl)
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
					.header("Accept-Encoding","gzip, deflate, sdch")
					.header("Accept-Language","zh-CN,zh;q=0.8")
					.header("Cache-Control","max-age=0")
					.header("Connection","keep-alive")
					.header("Host","music.163.com")
					.header("Refer","http://music.163.com/")
					.header("Upgrade-Insecure-Requests","1")
					.userAgent(userAgent).get();
		Element toplistFather = doc.getElementById("toplist");
		Element toplistsFather = toplistFather.getElementsByTag("ul").first();
		Elements toplists = toplistsFather.getElementsByTag("li");
		List<Fuckzzw> list = new ArrayList<>();
		for(Element toplist:toplists) {
			String id = toplist.attr("data-res-id");
			Element img = toplist.getElementsByTag("img").first();
			String coverUrl = img.attr("src");
			String name = img.attr("alt");
			list.add(new Fuckzzw(id, name, coverUrl));
		}
		return list;
	}
	public List<Song> getTopList(String topListId) throws IOException {
		Document doc = Jsoup.connect(topListUrl+ topListId)
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Accept-Encoding","gzip, deflate, sdch")
				.header("Accept-Language","zh-CN,zh;q=0.8")
				.header("Cache-Control","max-age=0")
				.header("Connection","keep-alive")
				.header("Host","music.163.com")
				.header("Refer","http://music.163.com/")
				.header("Upgrade-Insecure-Requests","1")
				.userAgent(userAgent).get();
		Element songListFather = doc.getElementsByTag("textarea").first();
		JSONArray songList = JSON.parseArray(songListFather.text());
		List<Song> list = new ArrayList<>();
		for(int i=0;i<songList.size();i++) {
			JSONObject song = songList.getJSONObject(i);
			String id = song.getString("id");
			String name = song.getString("name");
			String albumName = song.getJSONObject("album").getString("name");
			String albumId = song.getJSONObject("album").getString("id");
			String imgUrl = song.getJSONObject("album").getString("picUrl");
			JSONArray artistsArray = song.getJSONArray("artists");
			List<Artist> artists = new ArrayList<>();
			for(int j=0;j<artistsArray.size();j++) {
				JSONObject artist = artistsArray.getJSONObject(j);
				String artistName = artist.getString("name");
				String artistId = artist.getString("id");
				artists.add(new Artist(artistId, artistName));
			}
			Album album = new Album(albumId, albumName, imgUrl);
			list.add(new Song(name, id, album, artists));
		}
		return list;
	}
}
