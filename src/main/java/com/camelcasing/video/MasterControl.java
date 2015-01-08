package com.camelcasing.video;

/*TODO
 * - no signal to save all if changed
 * - Exception if right click update a second time
 * - right click add "updated" even if nothing changes
 * - drag and drop shows and dates to reorganise
 */

import java.io.IOException;
import java.net.*;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MasterControl implements ChangeListener{

		private Logger logger = LogManager.getLogger(MasterControl.class);
	
		private BorderPane root;
		private List<String> shows;
		private List<String> dates;
		private DateViewer view;
		private ShowList showList;
		private AirDates airDates;
		private OptionsPane options;
		private boolean changed = false;
		private Progress progressPane;
		
		protected static boolean isConnectedToInternet;
		protected static String testURL = "http://www.epguides.com";
		
	public MasterControl(){
		
		AirDate.stage.setOnCloseRequest(we -> {
			if(showList.isWriting()){
				we.consume();
				logger.info("Writing to disk, exit consumed");
			}
		});
		
		root = new BorderPane();
		root.setMaxHeight(600);
		root.setMaxWidth(400);
		root.setId("pane");
		
		progressPane = new Progress();
		
		showList = new ShowList();
		shows = showList.getShowList();
		logger.debug("shows.size() = " + shows.size());
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
		if(shows.get(0).equals("Problem reading shows.xml file")){
			view.addShowAndDate(new ShowAndDate("Problem reading shows.xml file", "01/01/1970"));
		}
		for(int i = 0; i < dates.size(); i++){
			ShowAndDate sad;
			String show = shows.get(i);
			if(dates.get(i) != null){
				sad = new ShowAndDate(show, dates.get(i));
			}else{
				sad = new ShowAndDate(show, "TBA");
			}
			MenuItem rightClickMenuItem = new MenuItem("Update " + show);
			rightClickMenuItem.setOnAction(e -> {
				AirDateParser ap = new AirDateParser().parse(sad.getShowName());
				String newDate;
				if(ap.isAiring()){
					newDate = "TODAY!";
				}else{
					newDate = AirDateUtils.englishDate(ap.getNextAirDate());
				}
				if(newDate != sad.getDate() && !newDate.equals("01/01/1970")){
					logger.debug("rightClick updating date");
					updateDate(sad.getShowName(), newDate, true);
				}
			});
			sad.setRightClickMenuItem(rightClickMenuItem);
			view.addShowAndDate(sad);
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
			logger.info("not connected to Internet");
			return false;
		}
		isConnectedToInternet = true;
		logger.info("is connected to Internet");
		airDates.generateShowData(false, false);
		return true;
	}
	
	public BorderPane getRootPane(){
		return root;
	}

	@Override
	public void updateDate(String show, String date, boolean lastShow){
		if(isConnectedToInternet == false){
			removeProgressBar();
			logger.error("failed to update as not connected to the Internet!\n"
					+ "Check Connection and tru again");
			return;
		}
		Platform.runLater(() ->{
			String da = date;
			int index = shows.indexOf(show);
			String currentDate = view.getDate(index);
			
			if(!currentDate.equals(da)){
				view.updateDate(date, index);
				if(date.equals("TODAY!")){
					LocalDate d = LocalDate.now();
					da = AirDateUtils.englishDate(d);
				};
				if(!date.equals("FAIL")){
					dates.set(index, da);
					changed = true;
				}
				if(lastShow && changed) saveDates();
				logger.debug("finished updating Date");
			}else{
				logger.debug("not updating (updateDate()) as shows are the same");
			}
		});
	}

	
	private void removeProgressBar(){
		Platform.runLater(() -> {
			if(progressPane.hasProgressBar()){
				progressPane.removeProgressBar();
			}
		});
	}
	
	@Override
	public void saveDates(){
		if(!changed){
			logger.debug("Lists are the same"); 
			removeProgressBar();
		}else{
			showList.setDateList(dates);
			changed = false;
			showList.writeNewAirDates();
			removeProgressBar();
		}
	}
}
