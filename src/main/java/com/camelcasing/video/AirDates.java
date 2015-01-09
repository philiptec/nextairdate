package com.camelcasing.video;

import java.time.LocalDate;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirDates implements ChangeController{

		private Logger logger = LogManager.getLogger(getClass());
	
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
		private  List<String> shows;
		private  List<String> dates;
		private  boolean isUpdating = false;
		private LocalDate compareToDate = LocalDate.now();
		private final String lastShow;
		private boolean updated, lastShowUpdated;
		
	public AirDates(List<String> shows, List<String> dates){
		this.shows = shows;
		this.dates = dates;
		lastShow = shows.get(shows.size() -1);
		logger.debug("final show is " + lastShow);
		if(shows.get(0).equals("Problem reading shows.xml file")) isUpdating = true;
	}
	
	public void generateShowData(boolean updateTBA, boolean updateAll){
		Thread t = new Thread(() -> {
			isUpdating = true;
			logger.debug("updating");
				for(int i = 0; i < shows.size(); i++){
					String show = shows.get(i);
					String d  = dates.get(i);
					
					if((d.equals("TBA")) && (!updateTBA && !updateAll)){
						logger.debug(show + " equal to null and not update");
					}else if(AirDateUtils.getDateFromString(d).compareTo(compareToDate) >= 0 && !updateAll){
						logger.debug(show + " date greater and not updateAll");
					}else{
						logger.debug("updateing " + show);
						updated = true;
						AirDateParser parser = new AirDateParser(compareToDate);
						LocalDate next = parser.parse(show).getNextAirDate();
						String date;
							if(parser.isAiring()){
								date = "TODAY!";
							}else if(next == null){
								date = "TBA";
							}else if(next.compareTo(AirDate.ERROR_DATE) == 0){
								date = "FAIL";
							}else{
								date = AirDateUtils.englishDate(next);
							}
						lastShowUpdated = show.equalsIgnoreCase(lastShow);
						updateListeners(show, date, lastShowUpdated);
						logger.debug("update -> " + show + " " + date);
					}
				}
			for(ChangeListener l : listeners){
				if(!lastShowUpdated && updated){
					l.saveDates();
				}
			}
			isUpdating = false;
			logger.debug("finished updating");
		});
		t.start();
	}
	
	public boolean isUpdateing(){
		return isUpdating;
	}

	@Override
	public void addChangeListener(ChangeListener l){
		listeners.add(l);
	}

	@Override
	public void updateListeners(String show, String date, boolean lastShow){
		if(lastShow){
			updated = false;
			logger.debug("lastShow Signal");
		}
		for(ChangeListener l : listeners) l.updateDate(show, date, lastShow);
	}
}
