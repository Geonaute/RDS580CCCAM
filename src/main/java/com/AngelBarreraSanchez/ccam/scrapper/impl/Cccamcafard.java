package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

public class Cccamcafard implements FreeClinesScrapper {
	private String BASE_URL = "http://cccamcafard.com/cccam-server-free/";
	
	
	private String default_hops;
	
	private Cccamcafard(){}
	
	/**
	 * @param default_hops
	 */
	public Cccamcafard(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {

			Response resIni = Jsoup.connect(BASE_URL)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.referrer(BASE_URL).method(Method.GET).execute();

			Document doc = Jsoup.parse(resIni.body());
			List<String> URLs = new ArrayList<>();
			Elements elements = doc.getElementsContainingText("Generator ");
			for (Element e : elements) {
				if (e.hasAttr("href")) {
					URLs.add(e.attr("href"));
				}
			}

			for (String url : URLs) {

				Response res = Jsoup.connect(url).data("username", "RDS580" + System.currentTimeMillis())
						.data("add_user", "")
						.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
						.referrer(url).method(Method.POST).execute();
				final String linesweb = res.body();
				String lineSearch1 = "C: ";
				String lineSearch2 = " </FONT> ";
				String line = linesweb.substring(linesweb.indexOf(lineSearch1) + lineSearch1.length(),
						linesweb.indexOf(lineSearch2, linesweb.indexOf(lineSearch1) + lineSearch1.length()));
				line = line.trim();
				final String[] tokens = line.split(" ");
				final String host = tokens[0].trim();
				final String port = tokens[1].trim();
				final String user = tokens[2].trim();
				final String pass = tokens[3].trim();
				clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
			}
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL + ". " + e.getMessage());
		}
		return clines;
	}
}
