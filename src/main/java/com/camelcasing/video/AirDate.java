package com.camelcasing.video;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AirDate extends Application implements ChangeController{
		
		private ArrayList<ChangeListener> listeners;
	
	@Override
	public void start(Stage stage) throws Exception{
		
		listeners = new ArrayList<ChangeListener>();
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
	
	@Override
	public void addChangeListener(ChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void updateListeners() {
		for(ChangeListener l : listeners) l.updateDate();
	}
	
	public static void main(String args[]){
		Application.launch(args);
	}
}
