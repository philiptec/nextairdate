package com.camelcasing.video;

import java.time.LocalDate;

import javafx.scene.control.ListView;

import org.apache.logging.log4j.*;

public class DateViewer {
	
		private ListView<ShowAndDate> pane;
		private Logger logger = LogManager.getLogger(getClass());
		
	public DateViewer(){
		pane = new ListView<ShowAndDate>();
		pane.setPrefHeight(600);
	}
	
	public ListView<ShowAndDate> getDisplayPane(){
		return pane;
	}
	
	public void reorganise(ShowDateList showDateList){
		logger.debug("DateViewer reorganising");
		int index = 0;
		for(ShowDateListNode node : showDateList){
			ShowAndDate current = ((ShowAndDate)pane.getItems().get(index));
			String showName = node.getShow();
			if(!node.getShow().equals(current.getShowName())){
				removeShow(index);
				addShowAndDate(current, showDateList.indexOf(showName));
			}
			index++;
		}
	}
	
	public void addShowAndDate(ShowAndDate sad, int index){
		logger.debug("adding " + sad.getShowName()  + " to index " + index);
		pane.getItems().add(index, sad);
	}
	
	public void removeAll(){
		pane.getItems().clear();
	}
	
	public void removeShow(int index){
		pane.getItems().remove(index);
	}
	
	public LocalDate getDate(int index){
		return ((ShowAndDate)pane.getItems().get(index)).getDate();
	}
	
	public void updateDate(LocalDate date, int row, int newIndex){
		ShowAndDate sad = ((ShowAndDate)pane.getItems().get(row));
		pane.getItems().remove(row);
		sad.setDate(date);
		pane.getItems().add(newIndex, sad);
	}
}
