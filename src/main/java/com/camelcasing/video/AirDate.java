package com.camelcasing.video;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AirDate extends Application{

		private OptionsPane options;
	
	@Override
	public void start(Stage stage) throws Exception{
		
		BorderPane root = new BorderPane();
		root.setMaxWidth(650);
		root.setMaxHeight(750);
		
		options = new OptionsPane();
		root.setTop(options.getPanel());

		ShowListPanel showListPane = new ShowListPanel();
		AirDatesPanel airDatesPane = new AirDatesPanel(showListPane.getShowList());
		
		root.setLeft(new ScrollPane(showListPane.getShowListPane()));
		root.setRight(new ScrollPane(airDatesPane.getAirDatePane()));
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("stylesheet.css");
		stage.setScene(scene);
		stage.setTitle("Air Dates");
		stage.show();
	}
	
	public static void main(String args[]){
		Application.launch(args);
	}
}
