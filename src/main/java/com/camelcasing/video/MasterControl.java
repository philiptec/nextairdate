package com.camelcasing.video;

import java.io.*;
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
		private Data<ShowDateListNode> showDateList;
		private DateViewer view;
		private ShowList showList;
		private AirDates airDates;
		private OptionsPane options;
		private Progress progressPane;
		private MenuBar menuBar;
		private Menu fileItem;
		private UpdateXMLFile udXML;
		
		private boolean overrideSave = false;
		
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
		root.setCenter(view.getDisplayPane());
		airDates = new AirDates();
		airDates.addChangeListener(this);

		addShowsAndDatesToView();
		activateButtons();
		AirDateUtils.testInternetConnection();
		standardUpdateIfInternetConnection();
	}
	
	public void activateButtons(){
		options.enableUpdateButton();
		fileItem.setDisable(false);
	}
	
	public boolean standardUpdateIfInternetConnection(){
		if(!AirDateUtils.testInternetConnection()){
			logger.info("not connected to internet");
			return AirDateUtils.isConnectedToInternet;
		}
		progressPane.addProgressBar();
		airDates.generateShowData(options.isUpdateTBA(), options.isUpdateAll(), showList.getShowDateList());
		return AirDateUtils.isConnectedToInternet;
	}
	
	public void getShowsAndDates(){
		showDateList = showList.getShowDateList();
		logger.debug("shows.size() = " + showDateList.size());
	}
	
	public void addShowsAndDatesToView(){
		ShowDateListNode node = showDateList.getFirst();
		for(int i = 0; i < showDateList.size(); i++){
			ShowAndDate sad = new ShowAndDate(node.getShow(), node.getDate(), node.getEpisode());
			view.addShowAndDate(sad, i);
			node = node.getNext();
		}
	}
	
	public void createMenuBar(){
		menuBar = new MenuBar();
		fileItem = new Menu("File");
		
		fileItem.setDisable(true);
		
		MenuItem setXmlFile = new MenuItem("Set Save File");
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
		udXML = UpdateXMLFile.getInstance(showList.getXmlFileLocation());
		udXML.setFileChooserListener(this);
		udXML.show();
		
	}
	
	public BorderPane getRootPane(){
		return root;
	}

	@Override
	public void updateDate(String show, LocalDate newDate, String episode, boolean save){
		if(AirDateUtils.isConnectedToInternet == false){
			removeProgressBar();
			logger.error("failed to update as not connected to the Internet!\n"
				+ "Check Connection and try again");
			return;
		}
		int oldIndex = showDateList.indexOf(show);
		LocalDate oldDate = view.getDate(oldIndex);
			
		if(!newDate.equals(oldDate)){
			overrideSave = true;
			showDateList.remove(show);
			int newIndex = showDateList.add(show, newDate, episode);
			logger.debug("new index = " + newIndex);
				
			Platform.runLater(() -> {
				logger.debug("date updated for " + show);
				view.updateDate(newDate, episode, oldIndex, newIndex);
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
			addShowsAndDatesToView();
//			testInternetConnectionAndUpdate();
	}

	@Override
	public void executeAddRemoveQuery(List<String> add, List<String> remove){
		if(remove.size() > 0){
			for(String s : remove){
				int index = showDateList.indexOf(s);
				view.removeShow(index);
				int i = showDateList.remove(s);
				logger.debug("removed: " + s + " index: " + i);
			}
		}
		if(add.size() > 0){
			for(String show : add){
				if(AirDateUtils.testInternetConnection()){
					ShowAndDate date = airDates.getShowAirDate(show, AirDateUtils.BLANK_EPISODE_DATE);
					int index = showDateList.add(show, date.getDateAsLocalDate(), date.getEpisode());
					view.addShowAndDate(date, index);
				}else{
					showDateList.add(show, AirDateUtils.ERROR_DATE, "0-0");
					view.addShowAndDate(new ShowAndDate(show, AirDateUtils.ERROR_DATE, "0-0"), showDateList.size() - 1);
				}
			}
		}
		if(add.size() != 0 || remove.size() != 0){
			overrideSave = true;
			saveDates();
		}
	}
}
