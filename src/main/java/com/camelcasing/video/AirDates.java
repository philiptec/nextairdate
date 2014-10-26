package com.camelcasing.video;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirDates implements ChangeController{

		private Logger logger = LogManager.getLogger(getClass());
	
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
		private  ArrayList<String> shows;
		private  ArrayList<String> dates;
		private  boolean isUpdating = false;
		private LocalDate compareToDate = LocalDate.now();
		private final String lastShow;
		
	public AirDates(ArrayList<String> shows, ArrayList<String> dates){
		this.shows = shows;
		this.dates = dates;
		lastShow = shows.get(shows.size() -1);
		logger.debug("final show is " + lastShow);
		if(shows.get(0).equals("Problem reading shows.xml file")) isUpdating = true;
	}
	
	public void generateShowData(boolean useYesterday, boolean updateTBA, boolean updateAll){
		Thread t = new Thread(() -> {
			isUpdating = true;
			if(useYesterday){
				compareToDate = compareToDate.minusDays(1);
			}
				for(int i = 0; i < shows.size(); i++){
					String show = shows.get(i);
					String d  = dates.get(i);
					
					if((d.equals("TBA")) && (!updateTBA && !updateAll)){
						logger.debug("equal to null and not update");
					}else if(AirDateUtils.getDateFromString(d).compareTo(compareToDate) > 0 && !updateAll){
						logger.debug("date greater and not updateAll");
					}else{
						logger.debug("updateing " + show);
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
						updateListeners(show, date);
					}
					if(show.equals(lastShow)) signalLastShow();
				}
		});
		t.setDaemon(true);
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
	public void updateListeners(String show, String date){
		for(ChangeListener l : listeners) l.updateDate(show, date);
	}
	@Override
	public void signalLastShow() {
		for(ChangeListener l : listeners) l.saveDates();
	}
}
