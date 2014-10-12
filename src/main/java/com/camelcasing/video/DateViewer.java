package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class DateViewer {
	
		private GridPane pane;
		private ScrollPane scrollPane;
		
	public DateViewer(){
		pane = new GridPane();
		pane.setPadding(new Insets(0, 10, 10, 10));
		pane.setHgap(100);
		pane.setVgap(10);
		
		scrollPane = new ScrollPane(pane);
	}
	
	public ScrollPane getDisplayPane(){
		return scrollPane;
	}
	
	public void addShowAndDate(String show, String date, int row){
		pane.add(getTextNode(show), 0, row);
		pane.add(getTextNode(date), 1, row);
	}
	
	public void addShow(String show, int row){
		pane.add(getTextNode(show), 0, row);
	}
	
	public void addDate(String date, int row){
		pane.add(getTextNode(date), 1, row);
	}
	
	private Text getTextNode(String value){
		Text text = new Text(value);
		text.setId("showText");
		return text;
	}
}
