package com.camelcasing.video;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

public class MasterControl implements ChangeListener{

		private Logger logger = LogManager.getLogger(getClass());
	
		private BorderPane root;
		private ArrayList<String> shows;
		private ArrayList<String> dates;
		private DateViewer view;
		private ShowList showList;
		private OptionsPane options;
		
	public MasterControl(){
		
		AirDate.stage.setOnCloseRequest(we -> {
			if(showList.isWriting()){
				we.consume();
				showWritingMessage();
			}
		});
		
		root = new BorderPane();
		root.setMaxHeight(650);
		root.setMinHeight(135);
		root.setPrefWidth(400);
		root.setId("pane");
		
		showList = new ShowList();
		shows = showList.getShowList();
		dates = showList.getDateList();
		
		AirDates airDates = new AirDates(shows, dates);
		airDates.addChangeListener(this);
		
		options = new OptionsPane(airDates);
		
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
	}
	
	public void showWritingMessage(){
		
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
			}
			dates.set(index, da);
			if(shows.indexOf(show) == shows.size() - 1){
				showList.setDateList(dates);
				showList.writeNewAirDates();
			}
		});
	}
}
