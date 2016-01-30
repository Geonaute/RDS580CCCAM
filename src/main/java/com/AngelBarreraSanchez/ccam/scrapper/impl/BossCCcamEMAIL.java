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
public class BossCCcamEMAIL implements FreeClinesScrapper {
	private String BASE_URL = "http://test.bosscccam.com/Default.aspx//";
	private String default_hops;
	
	private BossCCcamEMAIL(){}
	
	/**
	 * @param default_hops
	 */
	public BossCCcamEMAIL(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res = Jsoup.connect(BASE_URL)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.GET)
				.ignoreContentType(true)
				.execute();	
			
			String linesweb = res.body();
			String lineSearch1 = "id=\"__VIEWSTATE\" value=\"";
			String lineSearch2 = "\" />";
			String hidden1 = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			hidden1 = hidden1.trim();
			
			lineSearch1 = "id=\"__EVENTVALIDATION\" value=\"";
			String hidden2 = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
			hidden2 = hidden2.trim();
			
			String email = "RDS580"+System.currentTimeMillis()+"@yopmail.com";
			res = Jsoup.connect(BASE_URL)
				.data("__VIEWSTATE", hidden1)
				.data("__EVENTVALIDATION",hidden2)
				.data("btnEmailSend", "Test+Send")
				.data("txtEmailAdres",email)
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			
			Thread.sleep(2500);
			String emailContent = YopmailReader.getLastMail(email);
			linesweb = emailContent;
			lineSearch1 = " C: ";
			lineSearch2 = " Test time";
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
