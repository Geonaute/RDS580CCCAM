package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;
import com.AngelBarreraSanchez.ccam.util.ClineTester;

/**
 * Implementation of topreviewapps
 * Get a Cline from http://topreviewapps.no-ip.biz/free-cccam-server121.php
 * @author Angel Barrera Sanchez
 */
public class Topreviewapps implements FreeClinesScrapper {
	private String BASE_URL = "http://topreviewapps.no-ip.biz/free-cccam-server121.php";
	
	
	private String BASE_URL_1 = "http://topreviewapps.no-ip.biz/free-cccam-server121.php";
	private String BASE_URL_2 = "http://topreviewapps.no-ip.biz/free-cccam-server232.php";
	private String BASE_URL_3 = "http://topreviewapps.no-ip.biz/free-cccam-server343.php";
	private String BASE_URL_4 = "http://topreviewapps.no-ip.biz/free-cccam-server454.php";
	private String BASE_URL_5 = "http://topreviewapps.no-ip.biz/free-cccam-server565.php";
	private String[] urls = new String[]{BASE_URL_1,BASE_URL_2,BASE_URL_3,BASE_URL_4,BASE_URL_5};
	
	private String default_hops;
	
	private Topreviewapps(){}
	
	/**
	 * @param default_hops
	 */
	public Topreviewapps(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		for(String url : urls){
			try {
				Response res = Jsoup.connect(url)
					.data("user","RDS580"+System.currentTimeMillis())
					.data("pass","RDS580")
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.referrer(url)
					.method(Method.POST)
					.execute();	
				final String linesweb = res.body();
				String lineSearch1 = "C: ";
				String lineSearch2 = " \" and it will  expire";
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
		}
		return clines;
	}
}