package com.camelcasing.video;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class AirDatesPanel {

		private static GridPane pane;
		private static ArrayList<String> shows;
		private static int index = 0;
		
	public AirDatesPanel(ArrayList<String> shows){
		AirDatesPanel.shows = shows;
		pane = new GridPane();
		pane.setPadding(new Insets(20, 20, 0, 0));
	}
	
	//Upgrade to use javafx.concurrent.ScheduledService
	public static void generateShowData(boolean useYesterday){
		
		ScheduledService<Text> service = new ScheduledService<Text>(){
			protected Task<Text> createTask(){
				return new Task<Text>(){
					protected Text call(){
						if(index > shows.size()) this.cancel();
						LocalDate compareToDate = LocalDate.now();
						if(useYesterday){
							compareToDate = compareToDate.minusDays(1);
						}
						AirDateParser parser = new AirDateParser(compareToDate);
						LocalDate next = parser.parse(shows.get(index)).getNextAirDate();
						Text text = new Text();
						GridPane.setMargin(text, new Insets(0, 0, 20, 0));
						text.setId("showText");
							if(parser.isAiring()){
								text.setText("TODAY!");
							}else if(next == null){
								text.setText("TBA");
							}else{
								text.setText(next.getDayOfMonth() + "/" +
									next.getMonthValue() + "/" + next.getYear());
							}
						index++;
						Platform.runLater(() -> {
							pane.add(text, 0, index);
						});
						return text;
					};
				};
			};
		};
		service.start();
	}
	
	public GridPane getAirDatePane(){
		return pane;
	}
}
