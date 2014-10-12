package com.camelcasing.video;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class AirDatesPanel {

		private  GridPane pane;
		private  ArrayList<String> shows;
		private  int index = 0;
		private  boolean isUpdating = false;
		private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
	public AirDatesPanel(ArrayList<String> shows){
		this.shows = shows;
		if(shows.get(0).equals("Problem reading shows.xml file")) isUpdating = true;
		pane = new GridPane();
		pane.setId("myhbox");
		pane.setPadding(new Insets(20, 20, 0, 0));
	}
	
	public void generateShowData(boolean useYesterday){
		
		ScheduledService<Text> service = new ScheduledService<Text>(){
			protected Task<Text> createTask(){
				return new Task<Text>(){
					protected Text call(){
						isUpdating = true;
						if(index == shows.size()){
							isUpdating = false;
							this.cancel();
						}
						LocalDate compareToDate = LocalDate.now();
						if(useYesterday){
							compareToDate = compareToDate.minusDays(1);
						}
						AirDateParser parser = new AirDateParser(compareToDate);
						LocalDate next = parser.parse(shows.get(index)).getNextAirDate();
						Text text = new Text();
						GridPane.setMargin(text, new Insets(0, 20, 20, 0));
						text.setId("showText");
							if(parser.isAiring()){
								text.setText("TODAY!");
							}else if(next == null){
								text.setText("TBA");
							}else{
								text.setText(englishDate(next));
							}
//						System.out.println(index);
						index++;
						Platform.runLater(() -> {
							pane.add(text, 0, index);
//							Text text = new Text("Place Holder");
//							GridPane.setMargin(text, new Insets(0, 20, 20, 0));
//							text.setId("showText");
//							pane.add(text, 0, index);
						});
						return null;
					};
				};
			};
		};
		service.start();
	}
	
	public static String englishDate(LocalDate ld){
		return DATE_FORMATTER.format(ld);
	}
	
	public boolean isUpdateing(){
		return isUpdating;
	}
	
	public GridPane getAirDatePane(){
		return pane;
	}
}
