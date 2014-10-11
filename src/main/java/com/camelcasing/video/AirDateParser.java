package com.camelcasing.video;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AirDateParser{
	
		private final Logger logger = LogManager.getLogger(AirDateParser.class);
		private final String BASE_URL = "http://www.epguides.com/";
		private final String DATE_STRING = "[0-9]{2}/[A-Z]{1}[a-z]{2}/[0-9]{2}";
		
		private boolean airing = false;

		private ArrayList<String> monthList;
		private Document htmlPage;
		private LocalDate compareToDate;
		private LocalDate nextAirDate;
		
	public AirDateParser(){
		this(LocalDate.now());
	}
	
	public AirDateParser(LocalDate date){
		createMonthArrayList();
		compareToDate = date;
	}
	
	public AirDateParser parse(String show, LocalDate date){
		compareToDate = date;
		parse(show);
		return this;
	}
	
	public AirDateParser parse(String show){
		reset();
		createXMLReader(show);
		parseSite();
		return this;
	}
	
	public AirDateParser setCompareToDate(LocalDate date){
		this.compareToDate = date;
		return this;
	}
	
	public boolean isAiring(){
		return airing;
	}
	
	public LocalDate getNextAirDate(){
		return nextAirDate;
	}
	
	private void parseSite(){
		if(htmlPage != null){
			Element el = htmlPage.getElementsByTag("pre").get(0);
			String[] words = el.text().split(" ");
			for(int i = 0; i < words.length; i++){	
				if(words[i].matches(DATE_STRING)){
					String date = words[i];
				
					int day = Integer.parseInt(date.substring(0, 2));
					int month = monthList.indexOf(date.substring(3, 6)) + 1;
					int year = Integer.parseInt(date.substring(7)) + 2000;
						
					LocalDate airDate = LocalDate.of(year, month, day);
					if(airDate.compareTo(compareToDate) == 0){
						airing = true;
					}else if(airDate.compareTo(compareToDate) > 0){
						if(nextAirDate == null){
							nextAirDate = airDate;
							return;
						}
					}
				}
			}
		}
	}
	
	private void createXMLReader(String showName){
		try{
			htmlPage = Jsoup.connect(BASE_URL + showName).get();
		}catch(IOException e){
			logger.error("problem with " + showName);
		}
	}
	
	private void reset(){
		airing = false;
		nextAirDate = null;
		htmlPage = null;
	}
	
	private void createMonthArrayList(){
		monthList = new ArrayList<String>(12);
		monthList.add("Jan");
		monthList.add("Feb");
		monthList.add("Mar");
		monthList.add("Apr");
		monthList.add("May");
		monthList.add("Jun");
		monthList.add("Jul");
		monthList.add("Aug");
		monthList.add("Sep");
		monthList.add("Oct");
		monthList.add("Nov");
		monthList.add("Dec");
	}
}
