package com.camelcasing.video;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AirDateParser{
	
		private final Logger logger = LogManager.getLogger(AirDateParser.class);
		
		private static final String BASE_URL = "http://www.epguides.com/";
		private static final String DATE_PATTERN = "[0-9]{2}/[A-Z]{1}[a-z]{2}/[0-9]{2}";
		private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		
		private Document htmlPage;
	
	public LocalDate parse(String show){
		reset();
		createXMLReader(show);
		return parseSite();
	}
	
	private LocalDate parseSite(){
		if(htmlPage == null) return AirDateUtils.ERROR_DATE;
		Element el = htmlPage.getElementsByTag("pre").get(0);
		String[] words = el.text().split("\\s+");
		for(int i = 0; i < words.length; i++){	
			if(words[i].matches(DATE_PATTERN)){
				String date = words[i];
				
				int day = Integer.parseInt(date.substring(0, 2));
				int month = getMonth(date.substring(3, 6));
				int year = getYear(Integer.parseInt(date.substring(7)));
						
				LocalDate airDate = LocalDate.of(year, month, day);
				if(AirDateUtils.todayOrAfter(airDate)){
					return airDate;
				}
			}
		}
		return AirDateUtils.TBA_DATE;
	}
	
	private int getMonth(String month){
		for(int i = 0; i < 12; i++){
			if(MONTHS[i].equals(month)) return i + 1;
		}
		return -1;
	}
	
	private int getYear(int year){
		if(year > 70) return year + 1900;
		return year + 2000;
	}
	
	private void createXMLReader(String showName){
		try{
			htmlPage = Jsoup.connect(BASE_URL + showName).get();
		}catch(IOException e){
			logger.error("problem with " + showName);
			if(!AirDateUtils.isConnectedToInternet){
				logger.error("Not Connected to Internet");
			}
		}
	}
	
	private void reset(){
		htmlPage = null;
	}
}
