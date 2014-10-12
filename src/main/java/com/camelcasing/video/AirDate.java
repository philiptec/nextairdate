package com.camelcasing.video;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AirDate extends Application{
	
	@Override
	public void start(Stage stage) throws Exception{
		
		BorderPane root = new BorderPane();
		root.setMaxWidth(660);
		root.setMaxHeight(750);
		root.setId("myhbox");
		
		ShowListPanel showListPane = new ShowListPanel();
		AirDatesPanel airDatesPane = new AirDatesPanel(showListPane.getShowList());
		
		OptionsPane options = new OptionsPane(airDatesPane);
		
		showListPane.getShowListPane().maxHeightProperty().bind(root.maxHeightProperty());
		airDatesPane.getAirDatePane().maxHeightProperty().bind(root.maxHeightProperty());
		
		root.setTop(options.getPanel());
		root.setLeft(new ScrollPane(showListPane.getShowListPane()));
		root.setRight(new ScrollPane(airDatesPane.getAirDatePane()));
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("stylesheet.css");
		stage.setScene(scene);
		LocalDate todaysDate = LocalDate.now();
		stage.setTitle("Todays Date: " + AirDatesPanel.englishDate(todaysDate));
		stage.show();
	}
	
	public static void main(String args[]){
		Application.launch(args);
	}
}
