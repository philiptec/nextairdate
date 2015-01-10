package com.camelcasing.video;

import java.time.LocalDate;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirDates implements ChangeController{

		private Logger logger = LogManager.getLogger(getClass());
	
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
		private  boolean isUpdating = false;
		private LocalDate compareToDate = LocalDate.now();
		private String lastShow;
		private boolean updated;
		
	public AirDates(){
		logger.debug("final show is " + lastShow);
	}
	
	public void generateShowData(boolean updateTBA, boolean updateAll, List<String> shows, List<String> dates){
		Thread t = new Thread(() -> {
			isUpdating = true;
			logger.debug("updating");
			String lastShow = shows.get(shows.size() -1);
			
			if(shows.size() > 0) lastShow = shows.get(shows.size() -1);
				for(int i = 0; i < shows.size(); i++){
					String show = shows.get(i);
					String d  = dates.get(i);
					
					if((d.equals("TBA")) && (!updateTBA && !updateAll)){
						logger.debug(show + " equal to null and not updateTBA");
					}else if(AirDateUtils.getDateFromString(d).compareTo(compareToDate) >= 0 && !updateAll){
						logger.debug(show + " date greater and not updateAll");
					}else{
						logger.debug("updateing " + show);
						updated = true;
						String date = updateShow(show);
						boolean isLastShow = show.equals(lastShow);
						updateListeners(show, date, isLastShow);
						logger.debug("update -> " + show + " " + date);
					}
				}
			isUpdating = false;
			if(updated) for(ChangeListener l : listeners) l.saveDates();
			updated = false;
			logger.debug("finished updating");
		});
		t.start();
	}
	
	public String updateShow(String showName){
		AirDateParser parser = new AirDateParser(compareToDate);
		LocalDate next = parser.parse(showName).getNextAirDate();
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
		return date;
	}
	
	public boolean datesAreDifferent(String oldDate, String newDate){
		if(!newDate.equals("FAIL")){
			boolean change = !oldDate.equals(newDate);
			if(change) updated = true;
			return change;
		}
		return false;
	}
	
	public boolean isUpdateing(){
		return isUpdating;
	}
	
	public void setUpdatingStatus(boolean updating){
		isUpdating = updating;
	}
	
	public void signialUpdated(){
		updated = true;
	}
	
	public boolean hasUpdated(){
		return updated;
	}

	@Override
	public void addChangeListener(ChangeListener l){
		listeners.add(l);
	}

	@Override
	public void updateListeners(String show, String date, boolean save){
		for(ChangeListener l : listeners) l.updateDate(show, date, save);
	}
}
