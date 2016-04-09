package com.AngelBarreraSanchez.ccam.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;
import com.AngelBarreraSanchez.ccam.fileGenerator.CCCAMFileGenerator;
import com.AngelBarreraSanchez.ccam.fileGenerator.impl.LoadTemplate;
import com.AngelBarreraSanchez.ccam.fileGenerator.impl.PlainCCCAMFileGenerator;
import com.AngelBarreraSanchez.ccam.fileGenerator.impl.RDS580CCCAMFileGenerator;
import com.AngelBarreraSanchez.ccam.scrapper.FreeClinesScrapper;
import com.AngelBarreraSanchez.ccam.scrapper.impl.AllCccam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.BambooCCcam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.BossCCcamEMAIL;
import com.AngelBarreraSanchez.ccam.scrapper.impl.CccamWorld;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Cccamgenerator;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Cgenerator;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Chaherdztv;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Damidi;
import com.AngelBarreraSanchez.ccam.scrapper.impl.FC003;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Greencccamfree;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Jokercccam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Kahira;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Khaled;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Madvengers;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Maniaforu;
import com.AngelBarreraSanchez.ccam.scrapper.impl.New0;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Sat2arab;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Shashatv;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Topreviewapps;
import com.AngelBarreraSanchez.ccam.scrapper.impl.WebSat;
import com.AngelBarreraSanchez.ccam.util.ClineTester;

/**
 * The main class. 
 * @author Angel Barrera Sanchez
 */
public class RDS580Application {
	
	private final static String DEFAULT_HOPES = "0";
	
	/**
	 * Main function of the program
	 * @param 	args -> args[0] must be the output path (mandatory)
	 * 			args -> args[1] must be format. plain or rds580 (optional. rds580 by default)
	 * 			args -> args[2] path of the lines to be loaded at first (optional)
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if(args.length<1){
			System.err.println("Needs at least 1 parameter. The output path.");
		}else{
			List<CCCAMEntity> clines = new ArrayList<>();
			List<CCCAMEntity> clinesOnline = new ArrayList<>();
			
			if (args.length==3) {
				LoadTemplate.loadFile(clines, args[2]);
			} 
			
			FreeClinesScrapper maniaforu = new Maniaforu(DEFAULT_HOPES);
			clines.addAll(maniaforu.getLines());
			
			FreeClinesScrapper allCccam = new AllCccam(DEFAULT_HOPES);
			clines.addAll(allCccam.getLines());
			
			FreeClinesScrapper new0 = new New0(DEFAULT_HOPES);
			clines.addAll(new0.getLines());
			
			FreeClinesScrapper shashatv = new Shashatv(DEFAULT_HOPES);
			clines.addAll(shashatv.getLines());
			
			FreeClinesScrapper cccamWorld = new CccamWorld(DEFAULT_HOPES);
			clines.addAll(cccamWorld.getLines());
			
			FreeClinesScrapper cgenerator = new Cgenerator(DEFAULT_HOPES);
			clines.addAll(cgenerator.getLines());
			
			FreeClinesScrapper bambooCCcam = new BambooCCcam(DEFAULT_HOPES);
			clines.addAll(bambooCCcam.getLines());
			
			//WORKS ONLY ONCE PER IP
			FreeClinesScrapper khaled = new Khaled(DEFAULT_HOPES);
			clines.addAll(khaled.getLines());
			
			//ANALIZE
			FreeClinesScrapper webSat = new WebSat(DEFAULT_HOPES);
			clines.addAll(webSat.getLines());
			
			FreeClinesScrapper bossCCcamEMAIL = new BossCCcamEMAIL(DEFAULT_HOPES);
			clines.addAll(bossCCcamEMAIL.getLines());
			
			FreeClinesScrapper damidi = new Damidi(DEFAULT_HOPES);
			clines.addAll(damidi.getLines());
			
			//FROM HERE, COULD NOT WORK. IT GOES ON BUTTOM
			FreeClinesScrapper chaherdztv = new Chaherdztv(DEFAULT_HOPES);
			clines.addAll(chaherdztv.getLines());
			
			FreeClinesScrapper sat2arab = new Sat2arab(DEFAULT_HOPES);
			clines.addAll(sat2arab.getLines());
			
			//ANALIZE
			FreeClinesScrapper fc003 = new FC003(DEFAULT_HOPES);
			clines.addAll(fc003.getLines());
			
			FreeClinesScrapper jokercccam = new Jokercccam(DEFAULT_HOPES);
			clines.addAll(jokercccam.getLines());
			
			FreeClinesScrapper cccamgenerator = new Cccamgenerator(DEFAULT_HOPES);
			clines.addAll(cccamgenerator.getLines());
			
			FreeClinesScrapper greencccam = new Greencccamfree(DEFAULT_HOPES);
			clines.addAll(greencccam.getLines());
			
			FreeClinesScrapper topreviewapps = new Topreviewapps(DEFAULT_HOPES);
			clines.addAll(topreviewapps.getLines());
			
			FreeClinesScrapper madvengers = new Madvengers(DEFAULT_HOPES);
			clines.addAll(madvengers.getLines());
			
			FreeClinesScrapper kahira = new Kahira(DEFAULT_HOPES);
			clines.addAll(kahira.getLines());
			
			for(CCCAMEntity line : clines){
				if(ClineTester.getInstance().isClineActive(line)){
					clinesOnline.add(line);
				}
			}
			
			CCCAMFileGenerator fileGen = null;
			if(args.length>=2 && args[1].equals("plain")){
				fileGen = new PlainCCCAMFileGenerator();
			}else{
				fileGen = new RDS580CCCAMFileGenerator();
			}
			fileGen.generateFile(clinesOnline, args[0]);
		}
	}
}