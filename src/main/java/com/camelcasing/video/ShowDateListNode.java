package com.camelcasing.video;

import java.time.LocalDate;

public class ShowDateListNode{
	
		private ShowDateListNode next, previous;
		private String show, episode;
		private LocalDate date;
	
	public ShowDateListNode(String showName, LocalDate airDate, String epi, ShowDateListNode pre, ShowDateListNode nxt){
		show = showName;
		date = airDate;
		episode = epi;
		next = nxt;
		previous = pre;
	}
	
	public ShowDateListNode(String shouldBeNull, LocalDate shouldAlsoBeNull, String shouldAswellBeNull){
		show = null;
		date = null;
		episode = null;
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
	
	public void setDate(LocalDate newDate){
		date = newDate;
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	public String getEpisode(){
		return episode;
	}
	
	public void setEpisode(String newEpisode){
		episode = newEpisode;
	}
	
	@Override
	public String toString(){
		return show + " " + date + " " + episode;
	}
}
