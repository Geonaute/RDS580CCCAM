package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from Freecline.com
 * @author Angel Barrera Sanchez
 */
public class Freecline implements FreeClinesScrapper {
	private String BASE_URL = "http://www.freecline.com/history/CCcam/<ano>/<mes>/<dia>/";
	private String default_hops;
	
	private Freecline(){}
	
	/**
	 * @param default_hops
	 */
	public Freecline(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		Calendar cal = GregorianCalendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH)-1;
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res1 = Jsoup.connect(BASE_URL.replace("<ano>", year+"").replace("<mes>", month+"").replace("<dia>", day+""))
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.referrer(BASE_URL)
					.method(Method.GET)
					.execute();
			Document doc1 = Jsoup.parse(res1.body());
			Element tbody = doc1.getElementsByTag("tbody").get(0);
			for(Element tr : tbody.getElementsByTag("tr")){
				
				if(tr.outerHtml().contains("status_icon_online")){
					String linesweb = tr.outerHtml();
					String lineSearch1 = "title=\"Detailed information of the line\">";
					String lineSearch2 = "</a></td>";
					String line = linesweb.substring(linesweb.indexOf(lineSearch1) +  lineSearch1.length(), linesweb.indexOf(lineSearch2,linesweb.indexOf(lineSearch1) +  lineSearch1.length()));
					line = line.trim();
					final String[] tokens = line.split(" ");
					final String host = tokens[1].trim();
					final String port = tokens[2].trim();
					final String user = tokens[3].trim();
					final String pass = tokens[4].trim();
					clines.add(new CCCAMEntity(host, port, user, pass, default_hops));
				}
			}
		} catch (Exception e) {
			System.out.println("Error en " + BASE_URL+ ". " +e.getMessage());
		}
		return clines;
	}
}