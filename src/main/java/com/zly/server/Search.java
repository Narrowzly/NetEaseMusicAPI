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
import com.zly.apispider.WrapperResp;
import com.zly.model.Song;

@SuppressWarnings("all")
public class Search extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp = WrapperResp.wrap(resp);
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		List<Song> list = null;
		try {
			list = spider.search(req.getParameter("key").trim(),req.getParameter("pagenum").trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray();
		array.addAll(list);
		PrintWriter writer = resp.getWriter();
		writer.println(array.toJSONString());
	}
}
