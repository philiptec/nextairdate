package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirDate extends Application{
	
	@Override
	public void start(Stage stage) throws Exception{
	
		MasterControl control = new MasterControl();
		Scene scene = new Scene(control.getRootPane());
		scene.getStylesheets().add("stylesheet.css");
		stage.setScene(scene);
		LocalDate todaysDate = LocalDate.now();
		stage.setTitle("Todays Date: " + AirDates.englishDate(todaysDate));
		stage.show();
	}
	
	public static void main(String args[]){
			Application.launch(args);
	}
}
