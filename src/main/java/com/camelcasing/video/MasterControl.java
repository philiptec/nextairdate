package com.camelcasing.video;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

public class MasterControl implements ChangeListener{

		private BorderPane root;
		private ArrayList<String> shows;
		private DateViewer view;
		
	public MasterControl(){
		root = new BorderPane();
		root.setMaxHeight(750);
		root.setPrefWidth(400);
		root.setId("pane");
		
		ShowList showList = new ShowList();
		shows = showList.getShowList();
		
		AirDates airDates = new AirDates(shows);
		airDates.addChangeListener(this);
		
		OptionsPane options = new OptionsPane(airDates);
		
		view = new DateViewer();
		for(int i = 0; i < shows.size(); i++) view.addShow(shows.get(i), i);
		
		root.setTop(options.getPanel());
		root.setCenter(view.getDisplayPane());
	}
	
	public BorderPane getRootPane(){
		return root;
	}

	@Override
	public void updateDate(String show, String date) {
		Platform.runLater(() ->{
			view.addDate(date, shows.indexOf(show));
		});
	}
}
