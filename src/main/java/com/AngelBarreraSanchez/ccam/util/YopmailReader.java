package com.AngelBarreraSanchez.ccam.util;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class YopmailReader {
	
	public static String getLastMail(String email) throws IOException{
		Response res = Jsoup.connect("http://m.yopmail.com/en/inbox.php?login="+email+"&p=1&d=&ctrl=&scrl=&spam=true&yf=HZwD0ZGH5AwLlAGpjBGt0Aj&yp=YZGD0BQt3AGLjBGL4ZmNkBN&yj=RZGHjZmLlAwNkAmtmZGV4BN&v=2.6&r_c=&id=")
				.userAgent("Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4")
				.method(Method.GET)
				.execute();
		
		Map<String, String> cookies = res.cookies();
		
		Document doc = Jsoup.parse(res.body());
		Elements elements = doc.getElementsByClass("lm_m");
		res = Jsoup.connect("http://m.yopmail.com/en/" + elements.get(0).attr("href"))
				.userAgent("Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.79 Safari/537.4")
				.cookies(cookies)
				.method(Method.GET)
				.execute();
		doc = Jsoup.parse(res.body());
		return doc.getElementById("mailmillieu").text();
	}
}
