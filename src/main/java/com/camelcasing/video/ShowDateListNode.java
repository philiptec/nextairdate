package com.camelcasing.video;

import java.time.LocalDate;

public class ShowDateListNode{
	
		private ShowDateListNode next, previous;
		private final String show;
		private final String episode;
		private final LocalDate date;
		
		private int updateNumber = -1;
	
	public ShowDateListNode(String showName, LocalDate airDate, String epi, ShowDateListNode pre, ShowDateListNode nxt){
		show = showName;
		date = airDate;
		next = nxt;
		episode = epi;
		previous = pre;
	}
	
	public ShowDateListNode(String shouldBeNull, LocalDate shouldAlsoBeNull, String shouldAswellBeNull){
		show = shouldBeNull;
		date = shouldAlsoBeNull;
		episode = shouldAswellBeNull;
		next = this;
		previous = this;
	}
	
	public ShowDateListNode addBefore(ShowDateListNode sd){
		sd.setNext(this);
		sd.setPrevious(previous);
		previous.setNext(sd);
		previous = sd;
		return sd;
	}
	
	public ShowDateListNode addAfter(ShowDateListNode sd){
		sd.setNext(next);
		sd.setPrevious(this);
		next.setPrevious(sd);
		next = sd;
		return sd;
	}
	
	public ShowDateListNode getNext(){
		return next;
	}

	public void setNext(ShowDateListNode next){
		this.next = next;
	}

	public ShowDateListNode getPrevious(){
		return previous;
	}

	public void setPrevious(ShowDateListNode previous){
		this.previous = previous;
	}

	public String getShow(){
		return show;
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	public String getEpisode(){
		return episode;
	}
	
	public String getDateAsString(){
		return AirDateUtils.englishDate(date);
	}
	
	public void setUpdateNumber(int updateNumber){
		this.updateNumber = updateNumber;
	}
	
	public int getUpdateNumber(){
		return updateNumber;
	}
	
	@Override
	public String toString(){
		return show + " " + date + " " + episode;
	}
}
