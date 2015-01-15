package com.camelcasing.video;

import java.time.LocalDate;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirDates implements ChangeController{

		private Logger logger = LogManager.getLogger(getClass());
	
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
		private  boolean threadIsUpdating = false;
		private LocalDate compareToDate = AirDateUtils.TODAY;
		private boolean updated;
	
	public void generateShowData(boolean updateTBA, boolean updateAll, ShowDateList showDateList){
		Thread t = new Thread(() -> {
			threadIsUpdating = true;
				for(ShowDateListNode showAndDate : showDateList){
					String show = showAndDate.getShow();
					String d  = AirDateUtils.englishDate(showAndDate.getDate());
					
					if((d.equals("TBA")) && (!updateTBA && !updateAll)){
						logger.debug(show + " equal to null and not updateTBA");
					}else if(AirDateUtils.getDateFromString(d).compareTo(compareToDate) >= 0 && !updateAll){
						logger.debug(show + " date greater and not updateAll");
					}else{
						logger.debug("updateing " + show);
						updated = true;
						String date = updateShow(show);
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
	
	public LocalDate getShowAirDate(String show){
		AirDateParser parser = new AirDateParser(compareToDate);
		return parser.parse(show).getNextAirDate();
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
	public void updateListeners(String show, String date, boolean save){
		for(ChangeListener l : listeners) l.updateDate(show, date, save);
	}
}
