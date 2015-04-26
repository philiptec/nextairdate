package com.camelcasing.video;

import java.time.LocalDate;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.apache.logging.log4j.*;

public class DateViewer {
	
		private TableView<ShowAndDate> pane; 
		private Logger logger = LogManager.getLogger(getClass());
		private final double columnWidth = 200.0;
		private ObservableList<ShowAndDate> showAndDateList;
		
	@SuppressWarnings("unchecked")
	public DateViewer(ObservableList<ShowAndDate> showAndDateList){
		this.showAndDateList = showAndDateList;
		pane = new TableView<ShowAndDate>(this.showAndDateList);
		pane.setPrefHeight(600);
		
		TableColumn<ShowAndDate, String> showColumn = new TableColumn<ShowAndDate, String>("Show");
		showColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("show"));
		showColumn.setPrefWidth(columnWidth);
		
		TableColumn<ShowAndDate, String> episodeColumn = new TableColumn<ShowAndDate, String>("Episode");
		episodeColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("episode"));
		episodeColumn.setPrefWidth(columnWidth - 100);
		
		TableColumn<ShowAndDate, String> dateColumn = new TableColumn<ShowAndDate, String>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("date"));
		dateColumn.setPrefWidth(columnWidth- 75);
		
//		TableColumn<ShowAndDate, String> dayOfWeekColumn = new TableColumn<ShowAndDate, String>("Day");
//		dayOfWeekColumn.setCellValueFactory(new PropertyValueFactory<ShowAndDate, String>("dayOfWeek"));
//		dayOfWeekColumn.setPrefWidth(columnWidth - 75);
		
		pane.getColumns().setAll(showColumn, episodeColumn, dateColumn);//, dayOfWeekColumn);
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
		sad.setEpisode(episode);
		pane.getItems().add(newIndex, sad);
	}
}
