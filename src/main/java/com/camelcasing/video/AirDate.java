package com.camelcasing.video;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AirDate extends Application{
	
		public static Stage stage;
		public static Scene scene;
		public static MasterControl control;
	
	@Override
	public void start(Stage stage) throws Exception{
	
		AirDate.stage = stage;
		control = new MasterControl();
		scene = new Scene(control.getRootPane());
		scene.getStylesheets().add("default.css");
		stage.setScene(scene);
		stage.setTitle("Today's Date: " + AirDateUtils.englishDate(AirDateUtils.TODAY));
		stage.show();
		control.init();
	}
	
	public static void updateSingleShow(String show, String episode){
		control.updateSingleShow(show, episode);
	}
	
	public static void main(String args[]){
		if(args.length > 0){
			new CLIController(args);
		}else{
			Application.launch(args);
		}
	}
}
