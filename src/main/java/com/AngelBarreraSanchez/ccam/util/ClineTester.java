package com.AngelBarreraSanchez.ccam.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;

public class ClineTester {
	
	private final String URL_TESTER = "http://vip122.top2servers.tv/test.php";
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";
	private final String ENCODING = "UTF-8";
	
	private ClineTester() {
	}

	private static ClineTester clineTester;

	public static ClineTester getInstance() {
		if (clineTester == null) {
			clineTester = new ClineTester();
		}
		return clineTester;
	}

	public boolean isClineActive(CCCAMEntity line) {
		try {
            URL obj = new URL(URL_TESTER);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(15000);
            conn.addRequestProperty("User-Agent", USER_AGENT);
            conn.addRequestProperty("Referer", URL_TESTER);
            conn.setDoOutput(true);

            OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream(), ENCODING);

            w.write("lines="+"C: " + line.getHost() + " " + line.getPort() + " " + line.getUser() + " " + line.getPass());
            w.flush();
            w.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }

            in.close();
            conn.disconnect();
			
            Document web = Jsoup.parse(html.toString());
			
			if (web.getElementsByTag("font").get(0).text().contains(line.getUser()) && web.getElementsByTag("font").attr("color").equals("green")) {
				return true;
			} else {
				return false;
			}
		} catch (IOException | IndexOutOfBoundsException ioe) {
			return false;
		}
	}
}
