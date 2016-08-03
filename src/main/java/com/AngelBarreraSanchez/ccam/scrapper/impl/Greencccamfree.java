package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from http://greencccamfree.ddns.net/genf.php
 * @author Willyn
 */
public class Greencccamfree implements FreeClinesScrapper {
	private String BASE_URL = "http://greencccamfree.ddns.net/genf2.php";
	private String default_hops;
	
	private Greencccamfree(){}
	
	/**
	 * @param default_hops
	 */
	public Greencccamfree(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res = Jsoup.connect("http://myexternalip.com/raw")
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.method(Method.GET).execute();
			final String ip = res.body().trim();

			res = Jsoup.connect(BASE_URL).data("clav1","RDS580"+System.currentTimeMillis())
				.data("arrob1","RDS580"+System.currentTimeMillis()+"@yopmail.com")
				.data("u1", ip)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = " free es \"C:";
			String lineSearch2 = " \" validez hasta";
			String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			line = line.replaceAll("\\s+", " ");
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