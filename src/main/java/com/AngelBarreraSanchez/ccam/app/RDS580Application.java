package com.AngelBarreraSanchez.ccam.app;

import java.util.ArrayList;
import java.util.List;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.fileGenerator.CCCAMFileGenerator;
import com.AngelBarreraSanchez.ccam.fileGenerator.impl.PlainCCCAMFileGenerator;
import com.AngelBarreraSanchez.ccam.fileGenerator.impl.RDS580CCCAMFileGenerator;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;
import com.AngelBarreraSanchez.ccam.scrapper.impl.AllCccam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.BambooCCcam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Cccamgenerator;
import com.AngelBarreraSanchez.ccam.scrapper.impl.FC003;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Helala0;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Khaled;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Maniaforu;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Mycccam24;
import com.AngelBarreraSanchez.ccam.scrapper.impl.New0;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Universalcccam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.WebSat;

/**
 * The main class. 
 * @author Angel Barrera Sanchez
 */
public class RDS580Application {
	
	private final static String DEFAULT_HOPES = "1";
	
	/**
	 * Main function of the program
	 * @param args -> args[0] must be the output path
	 */
	public static void main(String[] args) {
		if(args.length<1){
			System.err.println("Needs at least 1 parameter. The output path.");
		}else{
			List<CCCAMEntity> clines = new ArrayList<>();
			
			FreeClinesScrapper maniaforu = new Maniaforu(DEFAULT_HOPES);
			clines.addAll(maniaforu.getLines());
			
			FreeClinesScrapper allCccam = new AllCccam(DEFAULT_HOPES);
			clines.addAll(allCccam.getLines());
			
			FreeClinesScrapper new0 = new New0(DEFAULT_HOPES);
			clines.addAll(new0.getLines());
			
			FreeClinesScrapper helala0 = new Helala0(DEFAULT_HOPES);
			clines.addAll(helala0.getLines());
			
			FreeClinesScrapper fc003 = new FC003(DEFAULT_HOPES);
			clines.addAll(fc003.getLines());
			
			FreeClinesScrapper cccamgenerator = new Cccamgenerator(DEFAULT_HOPES);
			clines.addAll(cccamgenerator.getLines());
			
			//IT CHANGES EVERY DAY THE URL
			//IN QUARENTINE
//			FreeClinesScrapper zetita = new Zetita(DEFAULT_HOPES);
//			clines.addAll(zetita.getLines());
			
			FreeClinesScrapper mycccam24 = new Mycccam24(DEFAULT_HOPES);
			clines.addAll(mycccam24.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper shashatv = new Shashatv(DEFAULT_HOPES);
//			clines.addAll(shashatv.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper cccamWorld = new CccamWorld(DEFAULT_HOPES);
//			clines.addAll(cccamWorld.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper cgenerator = new Cgenerator(DEFAULT_HOPES);
//			clines.addAll(cgenerator.getLines());
			
			FreeClinesScrapper bambooCCcam = new BambooCCcam(DEFAULT_HOPES);
			clines.addAll(bambooCCcam.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper sat2arab = new Sat2arab(DEFAULT_HOPES);
//			clines.addAll(sat2arab.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper cccam4you  = new CCcam4you (DEFAULT_HOPES);
//			clines.addAll(cccam4you.getLines());
			
			FreeClinesScrapper universalcccam = new Universalcccam(DEFAULT_HOPES);
			clines.addAll(universalcccam.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper jokercccam = new Jokercccam(DEFAULT_HOPES);
//			clines.addAll(jokercccam.getLines());
			
			//WORKS ONLY ONCE PER IP
			FreeClinesScrapper khaled = new Khaled(DEFAULT_HOPES);
			clines.addAll(khaled.getLines());
			
			//DOES NOT WORK
//			FreeClinesScrapper topCccam = new TopCccam(DEFAULT_HOPES);
//			clines.addAll(topCccam.getLines());
			
			FreeClinesScrapper webSat = new WebSat(DEFAULT_HOPES);
			clines.addAll(webSat.getLines());
			
			CCCAMFileGenerator fileGen = null;
			if(args.length==2 && args[1].equals("plain")){
				fileGen = new PlainCCCAMFileGenerator();
			}else{
				fileGen = new RDS580CCCAMFileGenerator();
			}
			fileGen.generateFile(clines, args[0]);
		}
	}
}