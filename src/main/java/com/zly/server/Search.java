package com.zly.server;

import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONArray;
import com.zly.apispider.NetEaseMusicSpider;
import com.zly.model.Song;

public class Search extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, 　　Content-Type, X-E4M-With");
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		List<Song> list = null;
		try {
			list = spider.init().search(req.getParameter("key").trim(),req.getParameter("pagenum").trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray();
		array.addAll(list);
		PrintWriter writer = resp.getWriter();
		writer.println(array.toJSONString());
	}
}
