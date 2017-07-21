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
import com.zly.model.SongUrl;

@SuppressWarnings("all")
public class SongDetail extends HttpServlet {
	private static Logger logger = Logger.getLogger(SongDetail.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		resp = WrapperResp.wrap(resp);
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			String songUrl= spider.getSongUrl(id);
			PrintWriter writer = resp.getWriter();
			writer.println(JSON.toJSONString(new SongUrl(songUrl, id)));
		} catch (IOException e) {
			logger.error(e);
		}
	
	
	}
}
