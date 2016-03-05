package com.AngelBarreraSanchez.ccam.fileGenerator.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;

public final class LoadTemplate {

	public static void loadFile(List<CCCAMEntity> clines, String path) throws FileNotFoundException, IOException {
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line, word;
			StringTokenizer elementos;
			String[] datos = new String[5];
			int i;
			List<CCCAMEntity> cline = new ArrayList<CCCAMEntity>();

			while ((line = b.readLine()) != null) {
				if (line.length() > 6 && line.substring(0, 6).equals("CCCAM:")) {
					line = line.substring(6).replace(" ", "");
					i = 0;
					elementos = new StringTokenizer(line, "{}");
					while (i < 5 && elementos.hasMoreTokens()) {
						word = elementos.nextToken();
						datos[i++] = word;
					}
					cline.add(new CCCAMEntity(datos[0], datos[1], datos[2], datos[3], datos[4]));
				}
			}
			clines.addAll(cline);
			System.out.println("Plantilla original cargada");
			b.close();
		} catch (FileNotFoundException e) {
			System.out.println("No se ha podido abrir la plantilla original.");
		} catch (Exception e) {
			System.out.println("Ocurrio un error en la carga del fichero!");
		}
	}
}