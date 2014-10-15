package com.camelcasing.video;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AirDates implements ChangeController{

		private ArrayList<ChangeListener> listeners = new ArrayList<ChangeListener>();
	
		private  ArrayList<String> shows;
		private  boolean isUpdating = false;
		private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	public AirDates(ArrayList<String> shows){
		this.shows = shows;
		if(shows.get(0).equals("Problem reading shows.xml file")) isUpdating = true;
	}
	
	public void generateShowData(boolean useYesterday){
		Thread t = new Thread(() -> {
			isUpdating = true;
			LocalDate compareToDate = LocalDate.now();
			if(useYesterday){
				compareToDate = compareToDate.minusDays(1);
			}
				for(String s : shows){
					AirDateParser parser = new AirDateParser(compareToDate);
					String show = s;
					LocalDate next = parser.parse(show).getNextAirDate();
					String date;
						if(parser.isAiring()){
							date = "TODAY!";
						}else if(next == null){
							date = "TBA";
						}else if(next.compareTo(LocalDate.of(1970, 1, 1)) == 0){
							date = "FAIL";
						}else{
							date = englishDate(next);
						}
					updateListeners(show, date);
				}
		});
		t.setDaemon(true);
		t.start();
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
