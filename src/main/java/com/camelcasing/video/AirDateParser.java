package com.camelcasing.video;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AirDateParser{
	
		private final Logger logger = LogManager.getLogger(AirDateParser.class);
		
		private static final String BASE_URL = "http://www.epguides.com/";
		private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		
		private Document htmlPage;
	
	public ShowAndDate parse(String show, String episode){
		reset();
		createXMLReader(show);
		return parseSite(show, episode);
	}
	
	private ShowAndDate parseSite(String show, String episode){
		if(htmlPage == null) return new ShowAndDate(show, AirDateUtils.ERROR_DATE, episode);
		Element el = htmlPage.getElementsByTag("pre").get(0);
		List<String> lines = getShowAsLines(el.text());
		
		for(String potentialDate : lines){
			String ep;
			String[] line = potentialDate.split("\\s+");
				int day = Integer.parseInt(line[2]);
				int month = getMonth(line[3]);
				int year = getYear(Integer.parseInt(line[4]));
						
				LocalDate airDate = LocalDate.of(year, month, day);
				
				if(AirDateUtils.todayOrAfter(airDate)){
					ep = line[1];
					ShowAndDate dae = new ShowAndDate(show, airDate, ep);
					logger.debug(dae);
					return dae;
				}
			}
		return new ShowAndDate(show, AirDateUtils.TBA_DATE, episode);
	}
	
	private List<String> getShowAsLines(String wholeTag){
		List<String> lines = new ArrayList<String>(30);
		String[] pre = wholeTag.split("\n");
		for(String s : pre){
			if(s.matches("^[0-9]{1,3}.{1}.*$")){
				lines.add(s);
			}
		}
		return lines;
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
