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
		private final String DATE_PATTERN = "[0-9]{2}/[A-Z]{1}[a-z]{2}/[0-9]{2}";

		private ArrayList<String> monthList;
		private Document htmlPage;

	public AirDateParser(){
		createMonthArrayList();
	}
	
	public LocalDate parse(String show){
		reset();
		createXMLReader(show);
		return parseSite();
	}
	
	private LocalDate parseSite(){
		if(htmlPage == null) return AirDateUtils.ERROR_DATE;
		Element el = htmlPage.getElementsByTag("pre").get(0);
		String[] words = el.text().split(" ");
		for(int i = 0; i < words.length; i++){	
			if(words[i].matches(DATE_PATTERN)){
				String date = words[i];
				
				int day = Integer.parseInt(date.substring(0, 2));
				int month = monthList.indexOf(date.substring(3, 6)) + 1;
				int year = getYear(Integer.parseInt(date.substring(7)));
						
				LocalDate airDate = LocalDate.of(year, month, day);
				if(AirDateUtils.todayOrAfter(airDate)){
					return airDate;
				}
			}
		}
		return AirDateUtils.TBA_DATE;
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
			if(!MasterControl.isConnectedToInternet){
				logger.error("Not Connected to Internet");
			}
		}
	}
	
	private void reset(){
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
