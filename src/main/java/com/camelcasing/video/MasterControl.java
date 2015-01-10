package com.camelcasing.video;

/*TODO
 * - drag and drop shows and dates to reorganise
 * - after first load will not save new dates?
 */

import java.io.*;
import java.net.*;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class MasterControl implements ChangeListener, FileChooserListener{

		private Logger logger = LogManager.getLogger(MasterControl.class);
	
		private BorderPane root;
		private List<String> shows;
		private List<String> dates;
		private DateViewer view;
		private ShowList showList;
		private AirDates airDates;
		private OptionsPane options;
		private Progress progressPane;
		private MenuBar menuBar;
		private UpdateXMLFile udXML;
		
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
		getShowsAndDates();
		
		airDates = new AirDates();
		airDates.addChangeListener(this);
		
		createMenuBar();
		
		options = new OptionsPane();
		options.setMenuBar(menuBar);
		options.init();
		options.getGoButton().setOnAction(e -> {
			if(!airDates.isUpdateing()){
				if(!isConnectedToInternet){
					if(!testInternetConnection())return;
				}
				airDates.generateShowData(options.isUpdateTBA(), options.isUpdateAll(), shows, dates);
				progressPane.addProgressBar();
				logger.debug(airDates.isUpdateing());
			}else{
				logger.debug("isUpdating");
			}
		});
		
		view = new DateViewer();
		addShowsAndDatesToView();
		
		root.setTop(options.getPanel());
		root.setCenter(view.getDisplayPane());
		root.setBottom(progressPane.getProgressPane());
		
		testInternetConnectionAndUpdate();
	}
	
	public void getShowsAndDates(){
		shows = showList.getShowList();
		logger.debug("shows.size() = " + shows.size());
		dates = showList.getDateList();
	}
	
	public void addShowsAndDatesToView(){
		if(shows.size() == 0 || shows.get(0).equals("Problem reading shows.xml file")){
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
				if(!isConnectedToInternet){
					if(!testInternetConnection())return;
				}
				AirDateParser ap = new AirDateParser().parse(sad.getShowName());
				String newDate;
				if(ap.isAiring()){
					newDate = "TODAY!";
				}else{
					newDate = AirDateUtils.englishDate(ap.getNextAirDate());
				}
				if(newDate != sad.getDate() && !newDate.equals("01/01/1970")){
					logger.debug("rightClick updating date");
					airDates.signialUpdated();
					updateDate(sad.getShowName(), newDate, true);
				}
			});
			sad.setRightClickMenuItem(rightClickMenuItem);
			view.addShowAndDate(sad);
		}
	}
	
	public void createMenuBar(){
		menuBar = new MenuBar();
		Menu fileItem = new Menu("File");
		MenuItem setXmlFile = new MenuItem("Set Xml File");
		setXmlFile.setOnAction(e -> {
			setXmlFileLocationAndReset();
		});
		MenuItem exit = new MenuItem("Close");
		exit.setOnAction(e -> Platform.exit());
		fileItem.getItems().addAll(setXmlFile, exit);
		menuBar.getMenus().add(fileItem);
	}
	
	public void setXmlFileLocationAndReset(){
		udXML = UpdateXMLFile.getInstance();
		udXML.setFileChooserListener(this);
		udXML.show();
		
	}
	
	public boolean testInternetConnectionAndUpdate(){
		testInternetConnection();
		if(isConnectedToInternet){
			airDates.generateShowData(false, false, shows, dates);
		}
		return isConnectedToInternet;
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
		return true;
	}
	
	public BorderPane getRootPane(){
		return root;
	}

	@Override
	public void updateDate(String show, String date, boolean save){
		if(isConnectedToInternet == false){
			removeProgressBar();
			logger.error("failed to update as not connected to the Internet!\n"
				+ "Check Connection and try again");
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
				}
				if(save) saveDates();
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
		if(!airDates.hasUpdated()){
			logger.debug("Lists are the same"); 
			removeProgressBar();
		}else{
			showList.setDateList(dates);
			showList.writeNewAirDates();
			removeProgressBar();
		}
	}

	@Override
	public void submitPressed(){
		File newXmlFile = udXML.getNewXmlLocation();
		if(newXmlFile.exists()){
			showList.setXmlFile(newXmlFile);
			view.removeAll();
			showList.createShowList();
			getShowsAndDates();
			if(shows.size() > 0) airDates.setUpdatingStatus(false);
			addShowsAndDatesToView();
		}
		udXML = null;
	}
}
