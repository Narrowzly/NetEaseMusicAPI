package com.zly.apispider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import com.zly.model.EncrypterSong;
import com.zly.model.Image;
import com.zly.model.PlayList;
import com.zly.model.Song;
import com.zly.model.SongList;
import com.zly.model.ZZW;
import com.zly.model.Fuckzzw;


public class NetEaseMusicSpider {
	private Logger logger = Logger.getLogger(NetEaseMusicSpider.class);
	private CloseableHttpClient client;
	private Encrypter encrypter;
	private static NetEaseMusicSpider spider;
	private SpiderConfig config;
	
	private NetEaseMusicSpider() {
		config = new SpiderConfig();
		client = HttpClients.createDefault(); 
		encrypter = Encrypter.getInstance();
	}
	public static NetEaseMusicSpider getInstance() {
		return spider!=null?spider:new NetEaseMusicSpider();
	}
	
	public List<Song> search(String keyword, String pageNum) throws ParseException, IOException {
		HttpPost post = new HttpPost(config.getSearchUrl());
		HttpResponse response = null;
		String offset = String.valueOf(Integer.parseInt(pageNum)*20);
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("limit", config.getLimit()));
		params.add(new BasicNameValuePair("offset", offset));
		params.add(new BasicNameValuePair("type", config.getType()));
		params.add(new BasicNameValuePair("s", keyword));
		post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
		post.setHeaders(config.getHeaders());
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
		List<String> ids = new ArrayList<>();
		ids.add(id);
		String encryptedSong = JSON.toJSONString(new EncrypterSong(ids, "320000", ""));
		String[] postparams = encrypter.encryptedRequest(encryptedSong);
		HttpPost post = new HttpPost(config.getSongUrl());
		post.setHeaders(config.getHeaders());
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
		Document doc = config.configHeaders (
				Jsoup.connect(config.getHotUrl()+limit+"&offset="+
				Integer.parseInt(page)*Integer.parseInt(limit))
				);	
		Element playListFather = doc.getElementById("m-pl-container");
		Elements playLists = playListFather.children();
		List<PlayList> p = new ArrayList<>();
		for(Element playList:playLists) {
			Element img = playList.getElementsByClass("j-flag").first();
			Element a = playList.getElementsByClass("msk").first();
			Element user = playList.getElementsByClass("nm").first();
			String imgUrl = img.attr("src");
			String title = a.attr("title");
			String creator = user.attr("title");
			String playListUrl = a.attr("href");
			String id = playListUrl.split("=")[1];
			p.add(new PlayList(imgUrl, title, playListUrl, id, creator));
		}
		return p;
	}
	public SongList getSongList(String num) throws IOException{
		Document doc = config.configHeaders(Jsoup.connect(config.getPlayListUrl()+num));
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
		Document doc = config.configHeaders(Jsoup.connect(config.getImgListUrl()));
		Element brotherFatherList = doc.getElementById("g_backtop");
		Element fatherList = brotherFatherList.nextElementSibling();
		String jsonArray = fatherList.outerHtml().split(";")[0].split("Gbanners\\s+=")[1];//Jsoup鏃犳硶瑙ｆ瀽script鐨勬枃鏈唴瀹�
		JSONArray imgArray = JSON.parseArray(jsonArray);
		List<Image> imageList = new ArrayList<>();
		for(int i=0;i<imgArray.size();i++) {
			JSONObject img = imgArray.getJSONObject(i);
			imageList.add(new Image(img.getString("picUrl")));
		}
		return imageList;
	}
	public ZZW fuckZZW() throws IOException, InterruptedException {
		Document doc = config.configHeaders(Jsoup.connect(config.getFuckZZWUrl()));
		Element toplistFather = doc.getElementById("toplist");
		Element toplistsFather = toplistFather.getElementsByTag("ul").first();
		Element topListsFather2 = toplistFather.getElementsByTag("ul").get(1);
		Elements toplists2 = topListsFather2.getElementsByTag("li");
		Elements toplists = toplistsFather.getElementsByTag("li");
		List<Fuckzzw> list1 = new ArrayList<>();
		List<Fuckzzw> list2 = new ArrayList<>();
		for(Element toplist:toplists) {
			String id = toplist.attr("data-res-id");
			Element img = toplist.getElementsByTag("img").first();
			String coverUrl = img.attr("src").split("\\?")[0];
			String name = img.attr("alt");
			Thread.sleep(500);
			List<Song> topThreeList = new ArrayList<>();
			List<Song> songList = getTopList(id);
			for(int i=0;i<3;i++) {
				topThreeList.add(songList.get(i));
			}
			Fuckzzw zzw = new Fuckzzw(id, name, coverUrl, topThreeList);
			list1.add(zzw);
		}
		for(Element toplist:toplists2) {
			String id = toplist.attr("data-res-id");
			Element img = toplist.getElementsByTag("img").first();
			String coverUrl = img.attr("src").split("\\?")[0];
			String name = img.attr("alt");
			list2.add(new Fuckzzw(id, name, coverUrl));
		}
		return new ZZW(list1, list2);
	}
	public List<Song> getTopList(String topListId) throws IOException {
		Document doc = config.configHeaders(Jsoup.connect(config.getTopListUrl()+ topListId));
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
