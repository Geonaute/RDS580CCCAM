package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from http://shasha-tv.blogsyte.com/free/index.php
 * @author Angel Barrera Sanchez
 */
public class Shashatv implements FreeClinesScrapper {
	
	private String BASE_URL = "http://shasha-tv.blogsyte.com/free/index.php";
	private String default_hops;
	
	private Shashatv(){}

	/**
	 * @param default_hops
	 */
	public Shashatv(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public Set<CCCAMEntity> getLines() {
		Set<CCCAMEntity> clines = new HashSet<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL)
					.data("user","RDS580"+System.currentTimeMillis())
					.data("pass","hack-sat.net")
					.data("submit","Active+User!")
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.referrer("http://shasha-tv.blogsyte.com/free/")
					.method(Method.POST)
					.execute();	
			
			final String linesweb = res.body();
			final String lineSearch1 = " C: ";
			final String lineSearch2 = " :|: and it will  expire";
			String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			line = line.trim();
			final String[] tokens = line.split(" ");
			final String host = tokens[0].trim();
			final String port = tokens[1].trim();
			final String user = tokens[2].trim();
			final String pass = tokens[3].trim();
			clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL);
			System.out.println("Error: " + e.getMessage());
		}
		return clines;
	}
}