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
					String episode = showAndDate.getEpisode();
					
					if(updateAll){
						updateShow(show, episode);
						continue;
					}
					
					LocalDate date = showAndDate.getDate();
					
					if(AirDateUtils.todayOrAfter(date)) continue;
					if((date.equals(AirDateUtils.TBA_DATE)) && !updateTBA) continue;
					
					updateShow(show, episode);
				}
			for(ChangeListener l : listeners) l.saveDates();
			logger.debug("finished updating");
		});
		t.setDaemon(true);
		t.start();
	}
	
	private void updateShow(String show, String episode){
		logger.debug("updateing " + show);
		updated = true;
		ShowAndDate newDate = getShowAirDate(show, episode);
		updateListeners(show, newDate.getDateAsLocalDate(), newDate.getEpisode(), false);
		logger.debug("update -> " + show + " " + newDate.getDate() + " " + newDate.getEpisode());
	}
	
	public ShowAndDate getShowAirDate(String show, String episode){
		return parser.parse(show, episode);
	}
	
	public void setUpdated(boolean b){
		updated = b;
	}
	
	public boolean hasUpdated(){
		return updated;
	}

	public void updateListeners(String show, LocalDate date, String episode, boolean save){
		for(ChangeListener l : listeners) l.updateDate(show, date, episode, save);
	}
	
	@Override
	public void addChangeListener(ChangeListener l){
		listeners.add(l);
	}
}
