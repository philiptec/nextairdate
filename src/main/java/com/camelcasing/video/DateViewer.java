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
	
	public void reorganise(ShowDateList showDateList){
		ShowDateListNode node = showDateList.getFirst();
		for(int i = 0; i < showDateList.size(); i++){
			ShowAndDate current = ((ShowAndDate)pane.getItems().get(i));
			String showName = node.getShow();
			if(!node.getShow().equals(current.getShowName())){
				removeShow(i);
				addShowAndDate(current, showDateList.indexOf(showName));
			}
			node = node.getNext();
		}
	}
	
	public void addShowAndDate(ShowAndDate sad, int index){
		pane.getItems().add(index, sad);
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
