package com.camelcasing.video;

import java.time.LocalDate;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;

public class ShowAndDate extends BorderPane{

		private Text show, date;
		private String showName;
		private ContextMenu rightClickMenu;
		
	public ShowAndDate(String show, String date){
		super();
		date = checkForToday(date);
		this.show = createTextNode(show);
		this.date = createTextNode(date);
		this.showName = show;
		prefWidthProperty().bind(AirDate.stage.widthProperty().subtract(50));
		setLeft(this.show);
		setRight(this.date);
		
		rightClickMenu = new ContextMenu();
		
		this.setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.SECONDARY)){
				rightClickMenu.show(this, e.getScreenX(), e.getScreenY());
			}
		});
	}
	
	public void setRightClickMenuItem(MenuItem mi){
		rightClickMenu.getItems().add(mi);
	}
	
	public String checkForToday(String date){
		if(date.equals("TBA")) return date; 
		LocalDate ld = AirDateUtils.getDateFromString(date);
		int diff = ld.compareTo(AirDateUtils.TODAY);
		if(diff == 0){
			return "TODAY!";
		}else if(diff < 0){
			return "";
		}else{
			return date;
		}
	}
	
	public Text createTextNode(String text){
		Text t = new Text(text);
		t.setId("showText");
		return t;
	}
	
	public String getDate(){
		return this.date.getText();
	}
	
	public String getShowName(){
		return showName;
	}
	
	public void setShow(String show){
		this.show.setText(show);
		this.showName = show;
	}
	
	public void setDate(String date){
		this.date.setText(date);
		this.show.setText(show.getText() + " (updated)");
	}
}
