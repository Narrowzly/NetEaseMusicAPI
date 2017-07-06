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
import com.zly.model.Image;

public class ImageList extends HttpServlet {
	private static Logger logger = Logger.getLogger(ImageList.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html;charset=UTF-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, 　　Content-Type, X-E4M-With");
		try {
			List<Image> l = NetEaseMusicSpider.getInstance().init().getImgList();
			JSONArray array = new JSONArray();
			array.addAll(l);
			PrintWriter writer = resp.getWriter();
			writer.println(array.toJSONString());
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
