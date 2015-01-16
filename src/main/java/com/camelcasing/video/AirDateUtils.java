package com.camelcasing.video;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AirDateUtils {
	
		private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		public final static LocalDate ERROR_DATE = LocalDate.of(1970,  1,  1);
		public final static LocalDate TODAY = LocalDate.now();
		public final static LocalDate TBA_DATE = LocalDate.of(2170, 1, 1);

	public static LocalDate getDateFromString(String date){
		if(date.equals("TBA")){
			return TBA_DATE;
		}
		String[] dates = date.split("/");
		int day = Integer.valueOf(dates[0]);
		int month = Integer.valueOf(dates[1]);
		int year = Integer.valueOf(dates[2]);
		return LocalDate.of(year, month, day);
	}
	
	public static String englishDate(LocalDate date){
		if(date == null) return "TBA";
		return DATE_FORMATTER.format(date);
	}
}