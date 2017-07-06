package com.zly.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.zly.apispider.NetEaseMusicSpider;
import com.zly.model.SongList;

public class PlayList extends HttpServlet {
	private static Logger logger = Logger.getLogger(PlayList.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, 　　Content-Type, X-E4M-With");
		String id = req.getParameter("id");
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			SongList songList = spider.init().getSongList(id);
			String jsonString = JSON.toJSONString(songList);
			PrintWriter writer = resp.getWriter();
			writer.println(jsonString);
		} catch (IOException e) {
			logger.error(e);
		}
	

	}
}
