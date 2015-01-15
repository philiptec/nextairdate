package com.camelcasing.video;

import java.time.LocalDate;

public class ShowDateListNode{
	
		private ShowDateListNode next, previous;
		private final String show;
		private final LocalDate date;
	
	public ShowDateListNode(String showName, LocalDate airDate, ShowDateListNode pre, ShowDateListNode nxt){
		show = showName;
		date = airDate;
		next = nxt;
		previous = pre;
	}
	
	public ShowDateListNode(String shouldBeNull, LocalDate shouldAlsoBeNull){
		show = shouldBeNull;
		date = shouldAlsoBeNull;
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
	
	public String getDateAsString(){
		return AirDateUtils.englishDate(date);
	}
	
	@Override
	public String toString(){
		return show + " " + date;
	}
}
