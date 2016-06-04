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
public class Hacksat implements FreeClinesScrapper {
	private String BASE_URL = "http://06.hack-sat.org/ddeerr444/index.php";
	private String default_hops;
	
	private Hacksat(){}
	
	/**
	 * @param default_hops
	 */
	public Hacksat(String default_hops) {
		this.default_hops = default_hops;
	}
	
	public static void main(String[] args) {
		Hacksat h = new Hacksat();
		h.getLines();
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			String user = "RDS580"+System.currentTimeMillis();
			Response res = Jsoup.connect(BASE_URL)
				.data("user",user)
				.data("pass","hack-sat.net")
				.data("submit","Active+User!")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "C: ";
			String lineSearch2 = " :|: and ";
			String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			line = line.trim();
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