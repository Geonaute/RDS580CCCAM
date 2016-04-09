package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;
import com.AngelBarreraSanchez.ccam.util.AdflyDecode;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from http://websat.ddns.net/mejor/cam-321.php
 * @author Angel Barrera Sanchez
 */
public class WebSat implements FreeClinesScrapper {
	private String BASE_URL = "http://websat.ddns.net";
	private String default_hops;
	
	private WebSat(){}
	
	/**
	 * @param default_hops
	 */
	public WebSat(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res1 = Jsoup.connect(BASE_URL)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.referrer(BASE_URL)
					.method(Method.GET)
					.execute();
			Document doc1 = Jsoup.parse(res1.body());
			Elements forms = doc1.getElementsByClass("bouton");
			String url = forms.get(0).attr("href");
			
			String urlDecoded = AdflyDecode.getInstance().decode(url);
			
			Response res = Jsoup.connect(urlDecoded)
				.data("Username","RDS580"+System.currentTimeMillis())
				.data("Password","RDS580")
				.data("addf","")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(urlDecoded)
				.cookie("acceptcookie", "ok")
				.cookie("acceptcookiefreecounterstat", "ok")
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "C: ";
			String lineSearch2 = " </b>";
			String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			line = line.trim();
			final String[] tokens = line.split(" ");
			final String host = tokens[0].trim();
			final String port = tokens[1].trim();
			final String user = tokens[2].trim();
			final String pass = tokens[3].trim();
			clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL+ ". " +e.getMessage());
		}
		return clines;
	}
}