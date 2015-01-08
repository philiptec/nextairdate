package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DateViewer {
	
		private VBox pane;
		private ScrollPane scrollPane;
		
	public DateViewer(){
		pane = new VBox();
		pane.prefWidthProperty().bind(AirDate.stage.widthProperty().subtract(20));
		pane.setPadding(new Insets(0, 10, 10, 10));
		pane.setSpacing(10);
		
		scrollPane = new ScrollPane(pane);
	}
	
	public ScrollPane getDisplayPane(){
		return scrollPane;
	}
	
	public void addShowAndDate(ShowAndDate sad){
		pane.getChildren().add(sad);
	}
	
	public String getDate(int index){
		return ((ShowAndDate)pane.getChildren().get(index)).getDate();
	}
	
	public void updateDate(String date, int row){
		((ShowAndDate)pane.getChildren().get(row)).setDate(date);
	}
}
