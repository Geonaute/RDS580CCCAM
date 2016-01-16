package com.AngelBarreraSanchez.ccam.scrapper.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from http://call-share.ddns.net/zazoud/index.php";
 * @author Angel Barrera Sanchez
 */
public class Zetita implements FreeClinesScrapper {
	private String BASE_URL = "http://call-share.ddns.net/zazoud/index.php";
	private String default_hops;
	
	private Zetita(){}
	
	/**
	 * @param default_hops
	 */
	public Zetita(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL)
				.data("user","RDS580"+System.currentTimeMillis())
				.data("pass","RDS580")
				.data("yes", "yes")
				.data("submit", "Activate here")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.cookie("acceptcookiefreecounterstat", "ok")
				.cookie("counter", "a6177519291b0df1c6a3e037ac8fb060")
				.cookie("counter_nv", "a6177519291b0df1c6a3e037ac8fb060")
				.cookie("acceptcookie", "ok.")
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "<b>C: ";
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
			System.out.println("Error en " + BASE_URL);
			System.out.println("Error: " + e.getMessage());
		}
		return clines;
	}
}
