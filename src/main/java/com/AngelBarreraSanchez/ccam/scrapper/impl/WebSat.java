package com.AngelBarreraSanchez.ccam.scrapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;

/**
 * Implementation of FreeClinesScrapper
 * Get a Cline from http://websat.ddns.net/mejor/cam-321.php
 * @author Angel Barrera Sanchez
 */
public class WebSat implements FreeClinesScrapper {
	private String BASE_URL = "http://websat.ddns.net";
	private String default_hops;
	
	private WebSat(){}
	
	/**
	 * @param default_hops
	 */
	public WebSat(String default_hops) {
		this.default_hops = default_hops;
	}
	
	/**
	 * Implementation method
	 */
	public List<CCCAMEntity> getLines() {
		List<CCCAMEntity> clines = new ArrayList<CCCAMEntity>();
		try {
			Response res1 = Jsoup.connect("http://websat.ddns.net/mejor/web-sat.php")
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
					.referrer(BASE_URL)
					.method(Method.GET)
					.execute();
			Document doc1 = Jsoup.parse(res1.body());
			Elements forms = doc1.getElementsByTag("form");
			String url = forms.get(0).attr("action");
			Response res = Jsoup.connect(BASE_URL+url)
				.data("nom","RDS580"+System.currentTimeMillis())
				.data("user","RDS580"+System.currentTimeMillis())
				.data("pass","RDS580")
				.data("yes","yes")
				.data("g-recaptcha-response","03AHJ_Vut1DG-0-XmxJxjNva4T9ln_l6qM1zO2UIYupo74OCV_yyqMlO6GNCfYf6I8E-wDJOz2G2zLyRB5TQD26rwwIRQ3w8UvrafY5pQnox7ICaZKltRH7DR13wwgISmNu5N73hFXWrCFW1oTNETePon_L_jupclyCiLztAnJQw5NFtOTOKSYQcOZ4IJNkmGtRVff3AwXCzOJDkz29oLYxQhfJ9TCpcjmIQ8fhCPP467vwgv7ygZpKHAC3sZqBLHxcdkXpY036TKTBCRA4_CDsDWuDyJMqmGMWwpudqOEezXcZnEZZ_gCjZ_IPWm_Hut0pLZlnUXLQkOvsmy3_aBXwuT51AXFPmi7mKSuBZ5F1cWCJqjsTortJiXT7AxVsuGc31A2DKs1qRLIiTdRShtfpDQB1_llyFlCzoSuJyZkyrZL85o41CcivAOEsfgze72nwM-k__aO_xKCoJR5tC9rPyr0MlgqPjf2xcvTppkbGOY2PDbP8CTG3wNda_weLLXLZgRF94yq_Z8gXShefVQMkTPQ8mFsvFw0VXodtdavPVQlfDJCrIvhz1hCqtNChtiaGP-Wh4x9fzB2Ms39Kh4fpxUinGkrvziHsbE0knIDIdlpGZUjxKeJVK1ZBprEFXsUtPpfgx3-iI6cA318EpjAECm6kbAq03rkiOMfAGmw1e-C0dHyDDV5TUz0OrydW8oEZbpEBxKGtVEJVKnLiGKW5dBPl7yeKgHUNQ7wydHsBnq9ehcZbKQ07HqAP-LNfy16fqL4n5JMafHTo4hP_KkWiw0ctQ0p-nzVKXqkUDjqmlsRBjUndMZu88LD6VNPZ75AT8W1M27kXoTiVp6CCE3mxxsA-jhk45AIbvG5rOlVpZdLW8p5bGmdyvAbsxgD0VFabVp41zqWBDGyC80ffTK_1ijmTqDhoKg1Ag")
				.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
				.referrer(BASE_URL)
				.method(Method.POST)
				.execute();	
			final String linesweb = res.body();
			String lineSearch1 = "C: ";
			String lineSearch2 = " </b>";
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