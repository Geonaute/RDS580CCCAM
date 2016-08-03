package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

public class Sudansat implements FreeClinesScrapper {
	private String BASE_URL = "http://api.appsgateway.net/csp/Api/GeneratorFrame.aspx";
	
	
	private String default_hops;
	
	private Sudansat(){}
	
	/**
	 * @param default_hops
	 */
	public Sudansat(String default_hops) {
		this.default_hops = default_hops;
	}
	
	public static void main(String[] args) {
		Sudansat s = new Sudansat();
		s.getLines();
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL)
				.data("btnGenerate","Generate+Your+Cline+Now!")
				.data("__VIEWSTATE", "LWrjC6/ldIuPX5LaUEb86R8IRY6ionFhQzr56gAaZpHTb6HNdVjpR2/Jvxjddhb2Opza7pqhLL9moYQ2UBgw0/qKJkjYAkcrqATqQJwvu73u+RqiconLNNnYgclBh80O")
				.data("__VIEWSTATEGENERATOR", "81CA8123")
				.data("__EVENTVALIDATION", "mbWVvebYy1n2+2h6qxVEeOXjVtz7SwSssYxQ1u0WSnO+8CEhDkXsVl53kJZfhcT/ZoMi00WygNO91eKPf39WSOedl/EaRdbexhJpJWDq2xgc22TyBMO9NWmTosa4I0LC")
				.cookie("ASP.NET_SessionId", "x51lruenrhrvvz2bkt5k3z3x")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "C:";
			String lineSearch2 = "</span>";
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
