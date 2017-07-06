package com.zly.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.zly.apispider.NetEaseMusicSpider;

public class SongDetail extends HttpServlet {
	private static Logger logger = Logger.getLogger(SongDetail.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, 　　Content-Type, X-E4M-With");
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			String songUrl= spider.init().getSongUrl(id);
			PrintWriter writer = resp.getWriter();
			writer.println("{\"songUrl\":\""+songUrl+"\","+"\"id\":\""+id+"\"}");
		} catch (IOException e) {
			logger.error(e);
		}
	
	
	}
}
