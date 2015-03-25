package com.camelcasing.video;

import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ShowAndDate{

		private LocalDate d; 
		private StringProperty show;
		private StringProperty date;
		private StringProperty episode;
//		private StringProperty dayOfWeek;
		
	public ShowAndDate(String show, LocalDate date, String episode){
		super();
		
		this.d = date;
		this.episode = new SimpleStringProperty(episode);
		this.show = new SimpleStringProperty(show);
		this.date = new SimpleStringProperty(checkForSpecial(date));
//		setDayOfWeek();
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
		return show.getValue();
	}
	
	public String getDate(){
		return date.getValue();
	}
	
	public String getEpisode(){
		return episode.getValue();
	}
	
//	public String getDayOfWeek(){
//		return dayOfWeek.getValue();
//	}
	
//	public void setDayOfWeek(){
//		AirDateUtils.logger.debug("d = " + d);
//		if(d.isAfter(AirDateUtils.ONE_WEEK_LATER)){
//			dayOfWeek = new SimpleStringProperty("");
//			return;
//		}
//		dayOfWeek = new SimpleStringProperty(d.getDayOfWeek().toString());
//	}
	
	public LocalDate getDateAsLocalDate(){
		return d;
	}
	
	public void setEpisode(String episode){
		this.episode.setValue(episode);
	}

	public void setDate(LocalDate date){
		d = date;
		this.date.setValue(checkForSpecial(date));
		this.show.setValue(show.getValue() + " (updated)");
//		setDayOfWeek();
	}
	
	@Override
	public String toString(){
		return show + " " + date;
	}
}
