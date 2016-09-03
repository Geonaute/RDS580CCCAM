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
import com.AngelBarreraSanchez.ccam.scrapper.impl.Cccam24h;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Cccamcafard;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Cccamgenerator;
import com.AngelBarreraSanchez.ccam.scrapper.impl.EuroccEMAIL;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Freecline;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Fullmonsters;
import com.AngelBarreraSanchez.ccam.scrapper.impl.GlobalServerHD;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Greencccamfree;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Khaled;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Madvengers;
import com.AngelBarreraSanchez.ccam.scrapper.impl.New0;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Sat2arab;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Smart4;
import com.AngelBarreraSanchez.ccam.scrapper.impl.SpeedCam;
import com.AngelBarreraSanchez.ccam.scrapper.impl.Sudansat;
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
			
			FreeClinesScrapper allCccam = new AllCccam(DEFAULT_HOPES);
			clines.addAll(allCccam.getLines());
			
			FreeClinesScrapper new0 = new New0(DEFAULT_HOPES);
			clines.addAll(new0.getLines());
			
			FreeClinesScrapper bambooCCcam = new BambooCCcam(DEFAULT_HOPES);
			clines.addAll(bambooCCcam.getLines());
			
			//WORKS ONLY ONCE PER IP
			FreeClinesScrapper khaled = new Khaled(DEFAULT_HOPES);
			clines.addAll(khaled.getLines());
			
			FreeClinesScrapper webSat = new WebSat(DEFAULT_HOPES);
			clines.addAll(webSat.getLines());
			
			FreeClinesScrapper bossCCcamEMAIL = new BossCCcamEMAIL(DEFAULT_HOPES);
			clines.addAll(bossCCcamEMAIL.getLines());
			//SLOW
			FreeClinesScrapper sat2arab = new Sat2arab(DEFAULT_HOPES);
			clines.addAll(sat2arab.getLines());
			
			FreeClinesScrapper cccamgenerator = new Cccamgenerator(DEFAULT_HOPES);
			clines.addAll(cccamgenerator.getLines());
			
			FreeClinesScrapper greencccam = new Greencccamfree(DEFAULT_HOPES);
			clines.addAll(greencccam.getLines());
			
			FreeClinesScrapper madvengers = new Madvengers(DEFAULT_HOPES);
			clines.addAll(madvengers.getLines());
			
			FreeClinesScrapper speedCam = new SpeedCam(DEFAULT_HOPES);
			clines.addAll(speedCam.getLines());
			
			FreeClinesScrapper fullmonsters = new Fullmonsters(DEFAULT_HOPES);
			clines.addAll(fullmonsters.getLines());
			
			FreeClinesScrapper sudansat = new Sudansat(DEFAULT_HOPES);
			clines.addAll(sudansat.getLines());
			
			FreeClinesScrapper cccamcafard = new Cccamcafard(DEFAULT_HOPES);
			clines.addAll(cccamcafard.getLines());
			
			FreeClinesScrapper euroccEMAIL = new EuroccEMAIL(DEFAULT_HOPES);
			clines.addAll(euroccEMAIL.getLines());
			
			FreeClinesScrapper smart4 = new Smart4(DEFAULT_HOPES);
			clines.addAll(smart4.getLines());
			
			//NOT WORK
//			FreeClinesScrapper freecline = new Freecline(DEFAULT_HOPES);
//			clines.addAll(freecline.getLines());
			
			FreeClinesScrapper c24 = new Cccam24h(DEFAULT_HOPES);
			clines.addAll(c24.getLines());
			
			FreeClinesScrapper globalServerHD = new GlobalServerHD(DEFAULT_HOPES);
			clines.addAll(globalServerHD.getLines());

			for(CCCAMEntity line : clines){
				long ini = System.currentTimeMillis();
				System.out.println("@@@@ " + line);
				if(clinesOnline.size()<16 && ClineTester.getInstance().isClineActive(line)){
					System.out.println("OK ->" + line);
					clinesOnline.add(line);
				}else{
					System.out.println("KO ->" + line);
				}
				System.out.println("TIEMPO: " + ((System.currentTimeMillis())-ini) + " ms");
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