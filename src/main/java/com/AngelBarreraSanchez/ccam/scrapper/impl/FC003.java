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
 * Get a Cline from http://free-cccam.tk/FC-003.43e/gen/index.php
 * @author Angel Barrera Sanchez
 */
public class FC003 implements FreeClinesScrapper {
	private String BASE_URL = "http://free-cccam.tk/FC-003.43e/gen/index.php";
	private String default_hops;
	
	private FC003(){}
	
	/**
	 * @param default_hops
	 */
	public FC003(String default_hops) {
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
				.data("submit","Activate!")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer("http://free-cccam.tk/FC-003.43e/gen/")
				.cookie("FC-003-43e", "cualquier+cosa")
				.cookie("__cfduid", "d3699ef052886c71715f5e8a7a10313fc1452418712")
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "<FONT COLOR=\"#00F\"><strong>C: ";
			String lineSearch2 = " </strong></FONT>       <br>";
			String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			line = line.trim();
			final String[] tokens = line.split(" ");
			final String host = tokens[0].trim();
			final String port = tokens[1].trim();
			final String user = tokens[3].trim();
			final String pass = tokens[4].trim();
			clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL+ ". " +e.getMessage());
		}
		return clines;
	}
}