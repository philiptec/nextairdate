package com.camelcasing.video;

import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javafx.scene.text.Font;

public class AirDateUtils {
	
		private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		private final static String TEST_URL = "http://www.epguides.com";
		public final static LocalDate ERROR_DATE = LocalDate.of(1970,  1,  1);
		public final static LocalDate TODAY = LocalDate.now();
		public final static LocalDate TBA_DATE = LocalDate.of(2170, 1, 1);
		public final static String BLANK_EPISODE_DATE = "0-0";
		public final static Font font = new Font(16);

		private static int updateNumber;
		public static boolean isConnectedToInternet;
		
	public static LocalDate getDateFromString(String date){
		String[] dates = date.split("/");
		int day = Integer.valueOf(dates[0]);
		int month = Integer.valueOf(dates[1]);
		int year = Integer.valueOf(dates[2]);
		return LocalDate.of(year, month, day);
	}
	
	public static int changeUpdateNumber(){
		int newRandomUpdateNumber = new Random().nextInt(1000);
		if(newRandomUpdateNumber == updateNumber) changeUpdateNumber();
		updateNumber = newRandomUpdateNumber;
		return newRandomUpdateNumber;
	}
	
	public static int getUpdateNumber(){
		return updateNumber;
	}
	
	public static String englishDate(LocalDate date){
		if(date == null) return "TBA";
		return DATE_FORMATTER.format(date);
	}
	
	public static boolean todayOrAfter(LocalDate date, LocalDate compareToDate){
		return(date.equals(compareToDate) || date.isAfter(compareToDate));
	}
	
	public static boolean todayOrAfter(LocalDate date){
		if(date.equals(TBA_DATE)) return false;
		return todayOrAfter(date, TODAY);
	}
	
	public static boolean testInternetConnection(){
		if(isConnectedToInternet) return true;
		try{
			URLConnection urlC = new URL(TEST_URL).openConnection();
			urlC.getContent();
		}catch(IOException e){
			return false;
		}
		isConnectedToInternet = true;
		return true;
	}
}