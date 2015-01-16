package com.camelcasing.video;

import java.io.*;
import java.net.*;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class MasterControl implements ChangeListener, FileChooserListener, AddRemoveListener{

		private Logger logger = LogManager.getLogger(MasterControl.class);
	
		private BorderPane root;
		private ShowDateList showDateList;
		private DateViewer view;
		private ShowList showList;
		private AirDates airDates;
		private OptionsPane options;
		private Progress progressPane;
		private MenuBar menuBar;
		private Menu fileItem;
		private UpdateXMLFile udXML;
		private boolean overrideSave = false;
		protected static boolean isConnectedToInternet;
		
		private final static String TEST_URL = "http://www.epguides.com";
		
	public MasterControl(){
		
		AirDate.stage.setOnCloseRequest(we -> {
			if(showList.isWriting()){
				we.consume();
				logger.info("Writing to disk, exit consumed");
			}else{
				Platform.exit();
			}
		});
		
		root = new BorderPane();
		
		progressPane = new Progress();
		createMenuBar();
		
		options = new OptionsPane();
		options.setMenuBar(menuBar);
		options.init();
		options.getGoButton().setOnAction(e -> standardUpdateIfInternetConnection());
		
		view = new DateViewer();
		
		root.setTop(options.getPanel());
		root.setCenter(view.getDisplayPane());
		root.setBottom(progressPane.getProgressPane());
	}
	
	public void init(){
		showList = new ShowList();
		getShowsAndDates();
		airDates = new AirDates();
		airDates.addChangeListener(this);

		addShowsAndDatesToView();
		activateButtons();
		testInternetConnectionAndUpdate();
	}
	
	public void activateButtons(){
		options.enableUpdateButton();
		fileItem.setDisable(false);
	}
	
	public boolean standardUpdateIfInternetConnection(){
		if(!airDates.threadIsUpdateing()){
			if(!isConnectedToInternet){
				if(!testInternetConnection()) return isConnectedToInternet;
			}
			airDates.generateShowData(options.isUpdateTBA(), options.isUpdateAll(), showList.getShowDateList());
			progressPane.addProgressBar();
		}else{
			logger.debug("threadIsUpdating");
		}
		return isConnectedToInternet;
	}
	
	public void getShowsAndDates(){
		showDateList = showList.getShowDateList();
		logger.debug("shows.size() = " + showDateList.size());
	}
	
	public void addShowsAndDatesToView(){
		ShowDateListNode node = showDateList.getFirst();
		for(int i = 0; i < showDateList.size(); i++){
			ShowAndDate sad;
			String show = node.getShow();
			LocalDate date = node.getDate();
			sad = createShowAndDate(show, date);
			view.addShowAndDate(sad, i);
			node = node.getNext();
		}
	}
	
	public ShowAndDate createShowAndDate(String show, LocalDate date){
		ShowAndDate sad = new ShowAndDate(show, date);
		MenuItem rightClickMenuItem = new MenuItem("Update " + show);
		rightClickMenuItem.setOnAction(e -> {
			if(!isConnectedToInternet){
				if(!testInternetConnection())return;
			}
			LocalDate newDate = new AirDateParser().parse(sad.getShowName());

			if(!newDate.equals(sad.getDate())){
				logger.debug("rightClick updating date");
				overrideSave = true;
				updateDate(sad.getShowName(), newDate, true);
			}
		});
		sad.setRightClickMenuItem(rightClickMenuItem);
		return sad;
	}
	
	public void createMenuBar(){
		menuBar = new MenuBar();
		fileItem = new Menu("File");
		
		fileItem.setDisable(true);
		
		MenuItem setXmlFile = new MenuItem("Set Xml File");
		setXmlFile.setOnAction(e -> setXmlFileLocationAndReset());
		
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(e -> {
			if(showList.isWriting()) return;
			Platform.exit();
		});
		
		MenuItem addRemove = new MenuItem("Add and Remove");
		addRemove.setOnAction(e -> newAddRemoveDialog());
		
		fileItem.getItems().addAll(setXmlFile, addRemove, exit);
		menuBar.getMenus().addAll(fileItem);
	}
	
	public void newAddRemoveDialog(){
		AddRemoveDialog ard = AddRemoveDialog.getInstance();
		ard.setAddRemoveListener(this);
		ard.show(showDateList);
	}
	
	public void setXmlFileLocationAndReset(){
		udXML = UpdateXMLFile.getInstance();
		udXML.setFileChooserListener(this);
		udXML.show();
		
	}
	
	public boolean testInternetConnectionAndUpdate(){
		testInternetConnection();
		if(isConnectedToInternet){
			airDates.generateShowData(false, false, showDateList);
		}
		return isConnectedToInternet;
	}
	
	public boolean testInternetConnection(){
		if(isConnectedToInternet) return true;
		try{
			URLConnection urlC = new URL(TEST_URL).openConnection();
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
	public void updateDate(String show, LocalDate newDate, boolean save){
		if(isConnectedToInternet == false){
			removeProgressBar();
			logger.error("failed to update as not connected to the Internet!\n"
				+ "Check Connection and try again");
			return;
		}
		int oldIndex = showDateList.indexOf(show);
		LocalDate oldDate = view.getDate(oldIndex);
			
		if(!newDate.equals(oldDate)){
				showDateList.remove(show);
				int newIndex =  showDateList.add(show, newDate);
				logger.debug("new index = " + newIndex);
				
				Platform.runLater(() -> {
					logger.debug("date updated for " + show);
					view.updateDate(newDate, oldIndex, newIndex);
				});	
		}
		if(save) saveDates();
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
		if(!airDates.hasUpdated() && !overrideSave){
			logger.debug("Lists are the same"); 
			removeProgressBar();
		}else{
			Platform.runLater(() -> view.reorganise(showDateList));
			showList.setShowDateList(showDateList);
			showList.writeNewAirDates();
			removeProgressBar();
		}
		overrideSave = false;
		airDates.setUpdated(false);
	}

	@Override
	public void updateXmlFile(boolean save){
		File newXmlFile = udXML.getNewXmlLocation();
		if(!newXmlFile.exists()){
			try {
				if(!newXmlFile.createNewFile()) return;
			} catch (IOException e) {
				logger.error("Failed to create new XML file");
			}
		}
			showList.setXmlFile(newXmlFile);
			if(save) showList.setXmlFileInPreferences();
			view.removeAll();
			showList.createShowList();
			getShowsAndDates();
			if(showDateList.size() > 0) airDates.setThreadUpdatingStatus(false);
			addShowsAndDatesToView();
			testInternetConnectionAndUpdate();
	}

	@Override
	public void executeAddRemoveQuery(List<String> add, List<String> remove){
		if(remove.size() > 0){
			for(String s : remove){
				int index = showDateList.indexOf(s);
				view.removeShow(index);
				showDateList.remove(s);
			}
		}
		if(add.size() > 0){
			for(String show : add){
				if(testInternetConnection()){
					LocalDate date = airDates.getShowAirDate(show);
					int index = showDateList.add(show, date);
					view.addShowAndDate(createShowAndDate(show, date), index);
				}else{
					showDateList.add(show, AirDateUtils.ERROR_DATE);
					view.addShowAndDate(createShowAndDate(show, AirDateUtils.ERROR_DATE), showDateList.size() - 1);
				}
			}
		}
		if(add.size() != 0 || remove.size() != 0){
			overrideSave = true;
			saveDates();
		}
	}
}
