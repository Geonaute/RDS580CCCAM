package com.AngelBarreraSanchez.ccam.scrapper;

import java.util.Set;

import com.AngelBarreraSanchez.ccam.CCCAMEntity;

/**
 * Clines Scrapper Interface
 * @author Angel Barrera Sanchez
 */
public interface FreeClinesScrapper {
	
	/**
	 * Get the clines from a site
	 * @return a set of clines
	 */
	Set<CCCAMEntity> getLines();
}