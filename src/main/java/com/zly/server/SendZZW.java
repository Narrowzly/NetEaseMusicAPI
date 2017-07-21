package com.zly.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.zly.apispider.NetEaseMusicSpider;
import com.zly.apispider.WrapperResp;
import com.zly.model.PlayList;

@SuppressWarnings("all")
public class SendZZW extends HttpServlet {
	private static Logger logger = Logger.getLogger(SendZZW.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String limit = req.getParameter("limit");
		String pagenum = req.getParameter("pagenum");
		resp = WrapperResp.wrap(resp);
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			List<PlayList> lists = spider.getPlayList(limit, pagenum);
			JSONArray array = new JSONArray();
			array.addAll(lists);
			PrintWriter writer = resp.getWriter();
			writer.println(array.toJSONString());
		} catch (IOException e) {
			logger.error(e);
		}
	
	}
}
