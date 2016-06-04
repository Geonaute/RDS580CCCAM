package com.AngelBarreraSanchez.ccam.scrapper.impl;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;
import com.AngelBarreraSanchez.ccam.util.YopmailReader;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from bosscccam
 * @author Angel Barrera Sanchez
 */
public class EuroccEMAIL implements FreeClinesScrapper {
	private String BASE_URL = "http://216.224.177.58/freecc/freecc.php";
	private String default_hops;
	
	private EuroccEMAIL(){}
	
	/**
	 * @param default_hops
	 */
	public EuroccEMAIL(String default_hops) {
		this.default_hops = default_hops;
	}
	
	public static void main(String[] args) {
		EuroccEMAIL c = new EuroccEMAIL();
		c.getLines();
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			
			
			String email = "RDS580"+System.currentTimeMillis()+"@yopmail.com";
			Response res = Jsoup.connect(BASE_URL)
				.data("submit", "Send")
				.data("email",email)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			
			Thread.sleep(3333);
			String emailContent = YopmailReader.getLastMail(email);
			String linesweb = emailContent;
			String lineSearch1 = "C: ";
			String lineSearch2 = " #";
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
