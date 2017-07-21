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
import com.zly.model.ZZW;

@SuppressWarnings("all")
public class FuckZZW extends HttpServlet {
	private static Logger logger = Logger.getLogger(FuckZZW.class);
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp = WrapperResp.wrap(resp);
		NetEaseMusicSpider spider = NetEaseMusicSpider.getInstance();
		try {
			ZZW zzw = spider.fuckZZW();
			PrintWriter writer = resp.getWriter();
			writer.println(JSON.toJSONString(zzw));
		} catch (IOException e) {
			logger.error(e);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
