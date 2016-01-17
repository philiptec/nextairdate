package com.camelcasing.video;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ShowAndDate{

		private LocalDate d; 
		private final String showName;
		private StringProperty show;
		private StringProperty date;
		private StringProperty episode;
		
	public ShowAndDate(String show, LocalDate date, String episode){
		super();
		
		this.d = date;
		this.episode = new SimpleStringProperty(episode);
		this.show = new SimpleStringProperty(show);
		this.showName = show;
		this.date = new SimpleStringProperty(checkForSpecial(date));
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
	
	public String getShow(){
		return showName;
	}
	
	public StringProperty getShowProperty(){
		return show;
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

	public void setDate(LocalDate date){
		this.date.setValue(checkForSpecial(date));
		this.show.setValue(showName + " (updated)");
	}
	
	@Override
	public String toString(){
		return show + " " + date;
	}
}
