package com.camelcasing.video;

import java.time.LocalDate;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirDates implements ChangeController{

		private Logger logger = LogManager.getLogger(getClass());
	
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
		private  boolean threadIsUpdating = false;
		private boolean updated;
	
	public void generateShowData(boolean updateTBA, boolean updateAll, ShowDateList showDateList){
		Thread t = new Thread(() -> {
			threadIsUpdating = true;
				for(ShowDateListNode showAndDate : showDateList){
					String show = showAndDate.getShow();
					String d  = AirDateUtils.englishDate(showAndDate.getDate());
					
					if((d.equals("TBA")) && (!updateTBA && !updateAll)){
						logger.debug(show + " equal to null and not updateTBA");
					}else if(AirDateUtils.getDateFromString(d).compareTo(AirDateUtils.TODAY) >= 0 && !updateAll){
						logger.debug(show + " date greater and not updateAll");
					}else{
						logger.debug("updateing " + show);
						updated = true;
						LocalDate date = updateShow(show);
						updateListeners(show, date, false);
						logger.debug("update -> " + show + " " + date);
					}
				}
			for(ChangeListener l : listeners) l.saveDates();
			threadIsUpdating = false;
			logger.debug("finished updating");
		});
		t.start();
	}
	
	public LocalDate updateShow(String showName){
		AirDateParser parser = new AirDateParser();
		return parser.parse(showName);
	}
	
	public LocalDate getShowAirDate(String show){
		AirDateParser parser = new AirDateParser();
		return parser.parse(show);
	}
	
	public boolean datesAreDifferent(String oldDate, String newDate){
		if(!newDate.equals("FAIL")){
			boolean change = !oldDate.equals(newDate);
			if(change) updated = true;
			return change;
		}
		return false;
	}
	
	public boolean threadIsUpdateing(){
		return threadIsUpdating;
	}
	
	public void setThreadUpdatingStatus(boolean updating){
		threadIsUpdating = updating;
	}
	
	public void setUpdated(boolean b){
		updated = b;
	}
	
	public boolean hasUpdated(){
		return updated;
	}

	@Override
	public void addChangeListener(ChangeListener l){
		listeners.add(l);
	}

	@Override
	public void updateListeners(String show, LocalDate date, boolean save){
		for(ChangeListener l : listeners) l.updateDate(show, date, save);
	}
}
