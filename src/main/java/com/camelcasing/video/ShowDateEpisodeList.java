package com.camelcasing.video;

import java.time.LocalDate;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShowDateEpisodeList implements Data<ShowAndDate>{
	
		private Logger logger = LogManager.getLogger(getClass());
	
		private ObservableList<ShowAndDate> showDateEpisodeList;
	
	public ShowDateEpisodeList(){
		showDateEpisodeList = FXCollections.observableArrayList();
	}

	@Override
	public int add(String show, LocalDate date, String episode){
		int count = size();
		int i = count - 1;
		if(count == 0) i = -1;
		for(; i > -1; i--){
			if(AirDateUtils.todayOrAfter(date, showDateEpisodeList.get(i).getDateAsLocalDate())){
				logger.debug(show + " added after" + showDateEpisodeList.get(i).getShow());
				showDateEpisodeList.add(count, new ShowAndDate(show, date, episode));
				return count;
			}else{
				if(date.isBefore(AirDateUtils.TODAY)) break;
				count--;
				logger.debug(show + " skipping " + showDateEpisodeList.get(i).getShow());
			}
		}
		logger.debug(show + " added to end");
		showDateEpisodeList.add(0, new ShowAndDate(show, date, episode));
		return 0;
	}
	
	
	public ObservableList<ShowAndDate> getShowDateEpisodeList(){
		return showDateEpisodeList;
	}

	@Override
	public void clear(){
		showDateEpisodeList.clear();
	}

	@Override
	public int indexOf(String show){
		for(int i = 0; i < showDateEpisodeList.size(); i++){
			if(showDateEpisodeList.get(i).getShow().equals(show)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public int remove(String show){
		for(int i = 0; i < showDateEpisodeList.size(); i++){
			if(showDateEpisodeList.get(i).getShow().equals(show)){
				showDateEpisodeList.remove(i);
				return i;
			}
		}
		return -1;
	}

	@Override
	public int size(){
		return showDateEpisodeList.size();
	}

	@Override
	public ShowAndDate getFirst(){
		return showDateEpisodeList.get(0);
	}

	@Override
	public Iterator<ShowAndDate> iterator(){
		return showDateEpisodeList.iterator();
	}
}
