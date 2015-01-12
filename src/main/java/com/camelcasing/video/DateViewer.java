package com.camelcasing.video;

import javafx.scene.control.ListView;

public class DateViewer {
	
		private ListView<ShowAndDate> pane;
		
	public DateViewer(){
		pane = new ListView<ShowAndDate>();
		pane.setPrefHeight(600);
	}
	
	public ListView<ShowAndDate> getDisplayPane(){
		return pane;
	}
	
	public void addShowAndDate(ShowAndDate sad){
		pane.getItems().add(sad);
	}
	
	public void removeAll(){
		pane.getItems().clear();
	}
	
	public void removeShow(int index){
		pane.getItems().remove(index);
	}
	
	public String getDate(int index){
		return ((ShowAndDate)pane.getItems().get(index)).getDate();
	}
	
	public void updateDate(String date, int row){
		((ShowAndDate)pane.getItems().get(row)).setDate(date);
	}
}
