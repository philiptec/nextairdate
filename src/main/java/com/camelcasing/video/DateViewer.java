package com.camelcasing.video;

import java.time.LocalDate;
import java.util.Comparator;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import org.apache.logging.log4j.*;

public class DateViewer {
	
		private TableView<ShowAndDate> pane; 
		private Logger logger = LogManager.getLogger(getClass());
		private final double columnWidth = 200.0;
		
	@SuppressWarnings("unchecked")
	public DateViewer(){

		pane = new TableView<ShowAndDate>();
		pane.setPrefHeight(600);
		
		TableColumn<ShowAndDate, String> showColumn = new TableColumn<ShowAndDate, String>("Show");
		showColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("show"));
//		showColumn.setCellFactory(new Callback<TableColumn<ShowAndDate, String>, TableCell<ShowAndDate, String>>(){
//			@Override
//			public TableCell<ShowAndDate, String> call(TableColumn<ShowAndDate, String> col) {
//				
//				return null;
//			}
//		});
		showColumn.setPrefWidth(columnWidth);
		
		TableColumn<ShowAndDate, String> episodeColumn = new TableColumn<ShowAndDate, String>("Episode");
		episodeColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("episode"));
		episodeColumn.setPrefWidth(columnWidth - 100);
		episodeColumn.setId("centreText");
		
		TableColumn<ShowAndDate, String> dateColumn = new TableColumn<ShowAndDate, String>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("date"));
		dateColumn.setPrefWidth(columnWidth- 75);
		dateColumn.setId("centreText");
		
		pane.getColumns().setAll(showColumn, episodeColumn, dateColumn);
	}
	
	public TableView<ShowAndDate> getDisplayPane(){
		return pane;
	}
	
	public void addShowAndDate(ShowAndDate sad, int index){
		logger.debug("adding " + sad.getShow()  + " to index " + index);
		pane.getItems().add(index, sad);
	}
	
	public void removeAll(){
		pane.getItems().clear();
	}
	
	public void removeShow(int index){
		pane.getItems().remove(index);
	}
	
	public LocalDate getDate(int index){
		return ((ShowAndDate)pane.getItems().get(index)).getDateAsLocalDate();
	}
	
	public void updateDate(LocalDate date, String episode, int row, int newIndex){
		ShowAndDate sad = ((ShowAndDate)pane.getItems().get(row));
		pane.getItems().remove(row);
		sad.setDate(date);
			if(!episode.equals(AirDateUtils.BLANK_EPISODE_DATE)){
				sad.setEpisode(episode);
			}
		pane.getItems().add(newIndex, sad);
	}
}
