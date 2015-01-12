package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirDate extends Application{
	
		protected static final LocalDate ERROR_DATE = LocalDate.of(1970,  1,  1);
		public static SimpleStringProperty theme = new SimpleStringProperty("default.css"); 
		public static Stage stage;
		public static Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception{
	
		AirDate.stage = stage;
		MasterControl control = new MasterControl();
		scene = new Scene(control.getRootPane());
		scene.getStylesheets().add(theme.getValue());
		stage.setScene(scene);
		stage.setTitle("Today's Date: " + AirDateUtils.englishDate(AirDateUtils.TODAY));
		stage.show();
		control.init();
	}
	
	public static void main(String args[]){
		if(args.length > 0){
			new CLIController(args);
		}else{
			Application.launch(args);
		}
	}
	
	public static void changeTheme(String themeName){
		scene.getStylesheets().clear();
		scene.getStylesheets().add(themeName);
		theme.set(themeName);
	}
}
