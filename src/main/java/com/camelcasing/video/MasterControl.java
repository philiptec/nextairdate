package com.camelcasing.video;

import java.io.IOException;
import java.net.*;
import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

public class MasterControl implements ChangeListener{

		private Logger logger = LogManager.getLogger(MasterControl.class);
	
		private BorderPane root;
		private ArrayList<String> shows;
		private ArrayList<String> dates;
		private DateViewer view;
		private ShowList showList;
		private AirDates airDates;
		private OptionsPane options;
		private boolean changed = false;
		private Progress progressPane;
		
		//private boolean isConnectedToInternet;
		private static String testURL = "http://www.epguides.com";
		
	public MasterControl(){
		
		AirDate.stage.setOnCloseRequest(we -> {
			if(showList.isWriting()){
				we.consume();
				logger.info("Writing to disk, exit consumed");
			}
		});
		
		ShowAndDate.setMasterControl(this);
		
		root = new BorderPane();
		root.setMaxHeight(600);
		root.setMaxWidth(400);
		root.setId("pane");
		
		progressPane = new Progress();
		
		showList = new ShowList();
		shows = showList.getShowList();
		dates = showList.getDateList();
		
		airDates = new AirDates(shows, dates);
		airDates.addChangeListener(this);
		
		options = new OptionsPane();
		options.getGoButton().setOnAction(e -> {
			if(!airDates.isUpdateing()){
				airDates.generateShowData(options.isUpdateTBA(), options.isUpdateAll());
				progressPane.addProgressBar();
				logger.debug(airDates.isUpdateing());
			}else{
				logger.debug("isUpdating");
			}
		});
		
		view = new DateViewer();
		for(int i = 0; i < shows.size(); i++){
			if(dates.get(i) != null){
				view.addShowAndDate(shows.get(i), dates.get(i).toString(), i);
			}else{
				view.addShowAndDate(shows.get(i), "TBA", i);
			}
		}
		
		root.setTop(options.getPanel());
		root.setCenter(view.getDisplayPane());
		root.setBottom(progressPane.getProgressPane());
		
		testInternetConnection();
	}
	
	public boolean testInternetConnection(){
		try{
			URLConnection urlC = new URL(testURL).openConnection();
			urlC.getContent();
		}catch(IOException e){
			logger.info("is NOT connected to Internet");
			return false;
		}
		logger.info("is connected to Internet");
		airDates.generateShowData(false, false);
		return true;
	}
	
	public BorderPane getRootPane(){
		return root;
	}

	@Override
	public void updateDate(String show, String date) {
		Platform.runLater(() ->{
			String da = date;
			int index = shows.indexOf(show);
			view.updateDate(date, index);
			if(date.equals("TODAY!")){
				LocalDate d = LocalDate.now();
				if(options.useYesterday()) d.minusDays(1);
				da = AirDateUtils.englishDate(d);
				dates.set(index, da);
			}else if(date.equals("FAIL")){
			}else{
				dates.set(index, da);
				changed = true;
			}
			dates.set(index, da);
		});
	}

	
	private void removeProgressBar(){
		Platform.runLater(() -> {
			progressPane.removeProgressBar();
		});
	}
	
	@Override
	public void saveDates(){
		if(!changed){
			logger.debug("Lists are the same"); 
			//removeProgressBar();
		}else{
			showList.setDateList(dates);
			changed = false;
			showList.writeNewAirDates();
			//removeProgressBar();
		}
	}
}
