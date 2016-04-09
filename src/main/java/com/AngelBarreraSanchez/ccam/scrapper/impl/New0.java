package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from http://cccam-free.com/new0.php
 * @author Angel Barrera Sanchez
 */
public class New0 implements FreeClinesScrapper {
	
	private String BASE_URL = "http://cccamia.com/NEW/new0.php";
	private String default_hops;
	
	private New0(){}

	/**
	 * @param default_hops
	 */
	public New0(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL).timeout(7500)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.method(Method.GET)
				.execute();	
			Document doc = Jsoup.parse(res.body());
			final String[] tokens = doc.getElementsByTag("h1").get(0).text().split(" ");
			final String host = tokens[1].trim();
			final String port = tokens[2].trim();
			final String user = tokens[3].trim();
			final String pass = tokens[4].trim();
			clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL+ ". " +e.getMessage());
		}
		return clines;
	}
}