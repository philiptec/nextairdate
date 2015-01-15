package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirDate extends Application{
	
		protected static final LocalDate ERROR_DATE = LocalDate.of(1970,  1,  1);
		public static Stage stage;
		public static Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception{
	
		AirDate.stage = stage;
		MasterControl control = new MasterControl();
		scene = new Scene(control.getRootPane());
		scene.getStylesheets().add("default.css");
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
}
