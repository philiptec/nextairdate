package com.camelcasing.video;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class ShowAndDate extends BorderPane{

		private Text show, date;
		private String showName;
		
	public ShowAndDate(String show, String date){
		super();
		this.show = createTextNode(show);
		this.date = createTextNode(date);
		this.showName = show;
		prefWidthProperty().bind(AirDate.stage.widthProperty().subtract(50));
		setLeft(this.show);
		setRight(this.date);
	}
	
	public Text createTextNode(String text){
		Text t = new Text(text);
		t.setId("showText");
		return t;
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
	}
}
