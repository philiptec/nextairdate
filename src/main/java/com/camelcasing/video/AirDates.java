package com.camelcasing.video;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;

public class AirDates implements ChangeController{

		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	
		private  ArrayList<String> shows;
		private  int index = 0;
		private  boolean isUpdating = false;
		private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	public AirDates(ArrayList<String> shows){
		this.shows = shows;
		if(shows.get(0).equals("Problem reading shows.xml file")) isUpdating = true;
	}
	
	public void generateShowData(boolean useYesterday){
		
		ScheduledService<String> service = new ScheduledService<String>(){
			protected Task<String> createTask(){
				return new Task<String>(){
					protected String call(){
						isUpdating = true;
						if(index == shows.size()){
							isUpdating = false;
							this.cancel();
						}
						LocalDate compareToDate = LocalDate.now();
						if(useYesterday){
							compareToDate = compareToDate.minusDays(1);
						}
						AirDateParser parser = new AirDateParser(compareToDate);
						String show = shows.get(index);
						LocalDate next = parser.parse(show).getNextAirDate();
						String date;
							if(parser.isAiring()){
								date = "TODAY!";
							}else if(next == null){
								date = "TBA";
							}else{
								date = englishDate(next);
							}
						index++;
						updateListeners(show, date);
						return null;
					};
				};
			};
		};
		service.start();
	}
	
	public static String englishDate(LocalDate ld){
		return DATE_FORMATTER.format(ld);
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
}
