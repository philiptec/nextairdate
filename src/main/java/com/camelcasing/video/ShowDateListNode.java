package com.camelcasing.video;

import java.time.LocalDate;

public class ShowDateListNode{
	
		private ShowDateListNode next, previous;
		private String show;
		private LocalDate date;
	
	public ShowDateListNode(String show, LocalDate date, ShowDateListNode previous, ShowDateListNode next){
		this.show = show;
		this.date = date;
		this.next = next;
		this.previous = previous;
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
		setNext(sd);
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
	
	public void setDate(LocalDate date){
		this.date = date;
	}
	
	@Override
	public String toString(){
		return show + " " + date;
	}
}
