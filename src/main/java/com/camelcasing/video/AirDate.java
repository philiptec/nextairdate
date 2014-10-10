package com.camelcasing.video;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AirDate{
	
		private static Logger logger = LogManager.getLogger(AirDate.class);
		private static final String BASE_URL = "http://www.epguides.com/Arrow";
		private final Pattern DATE_STRING = Pattern.compile("[0-9]{2}/[A-Z]{1}[a-z]{2}/[0-9]{2}");
		private boolean airingToday = false;
	
		public static ArrayList<String> monthList;
		private Document doc;
		
	public AirDate() throws IOException{
		createMonthArrayList();
		createXMLReader();
		parseSite();
		printResult();
	}
	
	private void printResult(){
		System.out.println(airingToday);
	}
	
	private void parseSite(){
		Elements els = doc.getElementsByTag("pre");
		Element el = els.get(0);
		String result = el.text();
		String[] words = result.split(" ");
		Matcher matcher;
		for(int i = 0; i < words.length; i++){
			matcher = DATE_STRING.matcher(words[i]);
			if(matcher.matches()){					
				String date = words[i];
				
				int day = Integer.parseInt(date.substring(0, 2));
				int month = monthList.indexOf(date.substring(3, 6)) + 1;
				int year = Integer.parseInt(date.substring(7)) + 2000;
				
				LocalDate airDate = LocalDate.of(year, month, day);
				LocalDate today = LocalDate.now().minusDays(1);
				
					if((today.getYear() == airDate.getYear())){
						if((today.getMonthValue() == airDate.getMonthValue())){
							if((today.getDayOfWeek().equals(airDate.getDayOfWeek()))){
								airingToday = true;
							}
						}
					}
			}
		}
	}
	
	private void createXMLReader() throws IOException{
		doc = Jsoup.connect(BASE_URL).get();
	}
	
	private void createMonthArrayList(){
		monthList = new ArrayList<String>();
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
		
	public static void main(String[] args){
		try {
			new AirDate();
		} catch (IOException e) {
			logger.error("unable to read sit data", e);
		}
	}
}
