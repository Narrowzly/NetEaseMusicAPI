package com.zly.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.zly.apispider.NetEaseMusicSpider;
import com.zly.apispider.WrapperResp;
import com.zly.model.Song;

@SuppressWarnings("all")
public class TopList extends HttpServlet {
	private static Logger logger = Logger.getLogger(TopList.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp = WrapperResp.wrap(resp);
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			List<Song> l = spider.getTopList(req.getParameter("id"));
			JSONArray array = new JSONArray();
			array.addAll(l);
			PrintWriter writer = resp.getWriter();
			writer.println(array.toJSONString());
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
