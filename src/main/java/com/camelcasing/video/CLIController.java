package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Platform;

public class CLIController {
		
	public CLIController(String[] args){
		
		LocalDate compareToDate = LocalDate.now();
		int i = 0;
		if(args[0].equals("-y")){
			i = 1;
			compareToDate = compareToDate.minusDays(1);
		}
		
		for(; i < args.length; i++){
			String showName = args[i];
			AirDateParser parser = new AirDateParser(compareToDate);
			LocalDate next = parser.parse(showName).getNextAirDate();
			String date;
				if(parser.isAiring()){
					date = "TODAY!";
				}else if(next == null){
					date = "TBA";
				}else{
					date = AirDates.englishDate(next);
				}
			System.out.println(showName + ": " + date);
		}
		Platform.exit();
	}
}
