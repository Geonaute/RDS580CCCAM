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
 * Get a Cline from http://fullmonsters.zapto.org/cccam-gratuit/js/teste.php
 * @author Angel Barrera Sanchez
 */
public class Fullmonsters implements FreeClinesScrapper {
	private String BASE_URL = "http://fullmonsters2.zapto.org/cccam-gratuit/js/catalonia.php";
	private String default_hops;
	
	private Fullmonsters(){}
	
	/**
	 * @param default_hops
	 */
	public Fullmonsters(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			String user = "RDS580"+System.currentTimeMillis();
			Response res = Jsoup.connect(BASE_URL)
				.data("name",user)
				.data("surname","Click_On_Ads")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "c: ";
			String lineSearch2 = " </font>";
			String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			line = line.trim();
			line = line.replace("  ", " ");
//			line = line.replace(user, "").replace(pass, "");
			final String[] tokens = line.split(" ");
			final String host = tokens[0].trim();
			final String port = tokens[1].trim();
			user = tokens[2].trim();
			final String pass = tokens[3].trim();
			clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL+ ". " +e.getMessage());
		}
		return clines;
	}
}