package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Platform;

public class CLIController{
		
	public CLIController(String[] args){
		
		LocalDate compareToDate = AirDateUtils.TODAY;
		int i = 0;
		if(args[0].equals("-y")){
			i = 1;
			compareToDate = compareToDate.minusDays(1);
		}else if(args[0].equals("--help")){
			System.out.println(
					"NAME:\n    airdate\n\n"
					+"SYNOPSIS:\n    airdate [options] <args>\n\n"
					+"OPTIONS:\n    -y\n       Air dates found will be compare to yesterday\n\n"
					+"AUTHOR:\n    Philip Teclaff");
			Platform.exit();
			System.exit(0);
		}
		
		for(; i < args.length; i++){
			String showName = args[i];
			AirDateParser parser = new AirDateParser();
			LocalDate next = parser.parse(showName);
			String date;
				if(next == null){
					date = "TBA";
				}else if(next.equals(AirDateUtils.TODAY)){
					date = "TODAY!";
				}else if(next.compareTo(AirDateUtils.ERROR_DATE) == 0) {
					date = "Probelm connecting to site";
				}else{
					date = AirDateUtils.englishDate(next);
				}
			System.out.println(showName + ": " + date);
		}
		Platform.exit();
		System.exit(0);
	}
}
