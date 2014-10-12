package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Platform;

public class CLIController {
		
	public CLIController(String[] args){
		
		LocalDate compareToDate = LocalDate.now();
		String show = args[0];
		if(show.equals("-y")){
			show = args[1];
			compareToDate = compareToDate.minusDays(1);
		}
		
		AirDateParser parser = new AirDateParser(compareToDate);
		LocalDate next = parser.parse(show).getNextAirDate();
		String date;
			if(parser.isAiring()){
				date = "TODAY!";
			}else if(next == null){
				date = "TBA";
			}else{
				date = AirDates.englishDate(next);
			}
		System.out.println(date);
		Platform.exit();
	}
}
