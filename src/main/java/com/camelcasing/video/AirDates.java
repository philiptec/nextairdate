package com.camelcasing.video;

import java.time.LocalDate;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AirDates implements ChangeController{

		private Logger logger = LogManager.getLogger(getClass());
	
		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
		private boolean updated;
		private AirDateParser parser;
		
	public AirDates(){
		parser = new AirDateParser();
	}
	
	public void generateShowData(boolean updateTBA, boolean updateAll, Data<ShowDateListNode> showDateList){
		Thread t = new Thread(() -> {
				for(ShowDateListNode showAndDate : showDateList){
					String show = showAndDate.getShow();
					LocalDate date = showAndDate.getDate();
					
					if((date.equals(AirDateUtils.TBA_DATE)) && (updateTBA)){
						updateShow(show);
						continue;
					}else if(AirDateUtils.todayOrAfter(date) && updateAll){
						updateShow(show);
						continue;
					}else{
						logger.debug(show + " skipped");
					}
				}
			for(ChangeListener l : listeners) l.saveDates();
			logger.debug("finished updating");
		});
		t.setDaemon(true);
		t.start();
	}
	
	public void updateShow(String show){
		logger.debug("updateing " + show);
		updated = true;
		LocalDate newDate = getShowAirDate(show);
		updateListeners(show, newDate, false);
		logger.debug("update -> " + show + " " + newDate);
	}
	
	public LocalDate getShowAirDate(String show){
		return parser.parse(show);
	}
	
	public void setUpdated(boolean b){
		updated = b;
	}
	
	public boolean hasUpdated(){
		return updated;
	}

	public void updateListeners(String show, LocalDate date, boolean save){
		for(ChangeListener l : listeners) l.updateDate(show, date, save);
	}
	
	@Override
	public void addChangeListener(ChangeListener l){
		listeners.add(l);
	}
}
