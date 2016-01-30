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
 * Get a Cline from http://www.topcccam.com/serv1.php
 * @author Angel Barrera Sanchez
 */
public class TopCccam implements FreeClinesScrapper {
	private String BASE_URL_1 = "http://free.topcccam.com/12.php";
	private String BASE_URL_2 = "http://free.topcccam.com/13.php";
	private String[] urls = new String[]{BASE_URL_1,BASE_URL_2};
	private String default_hops;
	
	private TopCccam(){}

	/**
	 * @param default_hops
	 */
	public TopCccam(String default_hops) {
		this.default_hops = default_hops;
	}
	
	public static void main(String[] args) {
		TopCccam t = new TopCccam();
		t.getLines();
	}

	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		for(String url : urls){
			try {
				Response res = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.method(Method.GET)
					.execute();	
				final String linesweb = res.body();
				String lineSearch1 = " C: ";
				String lineSearch2 = "</FONT>";
				String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
				line = line.trim();
				final String[] tokens = line.split(" ");
				final String host = tokens[0].trim();
				final String port = tokens[1].trim();
				final String user = tokens[2].trim();
				final String pass = tokens[3].trim();
				clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
			} catch (Exception e) {
				System.out.println("Error en " + url+ ". " +e.getMessage());
			}
		}
		return clines;
	}
}