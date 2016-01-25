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
 * Get a Cline from http://www.mycccam24.com/
 * @author Angel Barrera Sanchez
 */
public class Mycccam24 implements FreeClinesScrapper {
	private String BASE_URL_1 = "http://www.mycccam24.com/1sv2016.php";
	private String BASE_URL_2 = "http://www.mycccam24.com/2sv2016.php";
	private String BASE_URL_3 = "http://www.mycccam24.com/3sv2016.php";
	private String BASE_URL_4 = "http://www.mycccam24.com/4sv2016.php";
	private String BASE_URL_5 = "http://www.mycccam24.com/5sv2016.php";
	private String BASE_URL_6 = "http://www.mycccam24.com/6sv2016.php";
	private String[] URLS = new String[]{BASE_URL_1, BASE_URL_2, BASE_URL_3, BASE_URL_4, BASE_URL_5, BASE_URL_6};
	private String default_hops;
	
	private Mycccam24(){}
	
	/**
	 * @param default_hops
	 */
	public Mycccam24(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		for(String url : URLS){
			try {
				Response res = Jsoup.connect(url).followRedirects(true)
						.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
						.referrer("http://www.sasontnwc.net/aMhQ")
						.cookie("PHPSESSID", "m512ik0j388l3pdmfeasjgu3e2")
						.method(Method.GET)
						.execute();	
					final String linesweb = res.body();
					final String lineSearch1 = " C: ";
					final String lineSearch2 = "</FONT>";
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