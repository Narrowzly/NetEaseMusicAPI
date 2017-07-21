package com.zly.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.zly.apispider.NetEaseMusicSpider;
import com.zly.apispider.WrapperResp;
import com.zly.model.SongList;

@SuppressWarnings("all")
public class PlayList extends HttpServlet {
	private static Logger logger = Logger.getLogger(PlayList.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp = WrapperResp.wrap(resp);
		String id = req.getParameter("id");
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			SongList songList = spider.getSongList(id);
			String jsonString = JSON.toJSONString(songList);
			PrintWriter writer = resp.getWriter();
			writer.println(jsonString);
		} catch (IOException e) {
			logger.error(e);
		}
	

	}
}
