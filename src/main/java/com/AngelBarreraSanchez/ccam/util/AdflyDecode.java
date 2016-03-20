package com.AngelBarreraSanchez.ccam.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

public class AdflyDecode {
	
	private AdflyDecode() {
	}

	private static AdflyDecode adflyDecode;

	public static AdflyDecode getInstance() {
		if (adflyDecode == null) {
			adflyDecode = new AdflyDecode();
		}
		return adflyDecode;
	}

	public String decode(String adflyurl) throws IOException {
		URL url1 = new URL(adflyurl);
		BufferedReader in = new BufferedReader(new InputStreamReader(url1.openStream()));
		String inputLine;
		String cleanResponse;
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		StringBuffer response = new StringBuffer();
		byte[] valueDecoded = null;

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine + "\n");
		}

		in.close();

		Pattern p = Pattern.compile("var ysmm = \'(.+?)\';");
		Matcher m = p.matcher(response.toString());

		while (m.find()) {
			cleanResponse = m.group(1);
			for (int i = 0; i < cleanResponse.length(); i += 2) {
				sb.append(cleanResponse.substring(i, i + 1));
			}
			for (int b = cleanResponse.length() - 1; b >= 0; b -= 2) {
				sb1.append(cleanResponse.charAt(b));
			}
			String bytesEncoded = sb.toString().concat(sb1.toString());
			valueDecoded = Base64.decodeBase64(bytesEncoded);
		}
		return new String(valueDecoded).substring(2);
	}
}