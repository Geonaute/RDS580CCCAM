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
 * Get a Cline from http://server.sat2arab.com/cccam.php
 * @author Angel Barrera Sanchez
 */
public class Sat2arab implements FreeClinesScrapper {
	private String BASE_URL = "http://server.sat2arab.com/cccam.php";
	private String default_hops;
	
	private Sat2arab(){}
	
	/**
	 * @param default_hops
	 */
	public Sat2arab(String default_hops) {
		this.default_hops = default_hops;
	}
	
	public static void main(String[] args) {
		Sat2arab  cg = new Sat2arab();
		cg.getLines();
	}
	
	/**
	 * Implementation method
	 */
	public Set<CCCAMEntity> getLines() {
		Set<CCCAMEntity> clines = new HashSet<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL)
				.data("author","RDS580"+System.currentTimeMillis())
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = " C: ";
			String lineSearch2 = " </FONT>";
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