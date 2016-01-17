package com.camelcasing.video;

import java.time.LocalDate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;


public class ShowAndDate{

		private LocalDate d; 
		private final String showName;
		private ObjectProperty<Node> show;
		private StringProperty date;
		private StringProperty episode;
		private final Text showText;
		
	public ShowAndDate(String show, LocalDate date, String episode){
		super();
		
		this.d = date;
		this.date = new SimpleStringProperty(checkForSpecial(date));
		
		this.episode = new SimpleStringProperty(episode);
		
		this.showText = new Text(show);
		showText.setFont(AirDateUtils.font);
		this.show = new SimpleObjectProperty<Node>(showText);
		this.showName = show;
		
	}
	
	public String checkForSpecial(LocalDate date){
		if(date.equals(AirDateUtils.ERROR_DATE) || date == null) return "FAIL";
		if(date.equals(AirDateUtils.TBA_DATE)) return "TBA"; 
		int diff = date.compareTo(AirDateUtils.TODAY);
		if(diff == 0){
			return "TODAY!";
		}else if(diff < 0) return "";
		return AirDateUtils.englishDate(date);
	}
	
	public String getShowName(){
		return showName;
	}
	
	public Node getShow(){
		return show.getValue();
	}
	
	public String getDate(){
		return date.getValue();
	}
	
	public String getEpisode(){
		return episode.getValue();
	}
	
	public LocalDate getDateAsLocalDate(){
		return d;
	}
	
	public void setEpisode(String episode){
		this.episode.setValue(episode);
	}

	public void setDate(LocalDate date, String episode){
		HBox node = new HBox(5.0);
		Circle c = new Circle(5.0);
		if(episode.equals(this.episode.getValue())){
			c.setFill(Paint.valueOf("orange"));
		}else{
			c.setFill(Paint.valueOf("green"));
		}
		node.setAlignment(Pos.CENTER_LEFT);
		node.getChildren().addAll(c, showText);
		
		this.date.setValue(checkForSpecial(date));
		this.show.setValue(node);
	}
	
	@Override
	public String toString(){
		return showName + " " + date;
	}
}
