package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

public class Cccamcafard2 implements FreeClinesScrapper {
	private String BASE_URL = "http://generator.cccamcafard.com/gjhlgjhl4040/index.php";
	
	
	private String default_hops;
	
	private Cccamcafard2(){}
	
	/**
	 * @param default_hops
	 */
	public Cccamcafard2(String default_hops) {
		this.default_hops = default_hops;
	}
	
	public static void main(String[] args) {
		Cccamcafard2 c = new Cccamcafard2();
		c.getLines();
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL)
				.data("username","RDS580"+System.currentTimeMillis())
				.data("add_user", "")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "C: ";
			String lineSearch2 = " <center></font>";
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
