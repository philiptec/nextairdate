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
	
	public void addBefore(ShowDateListNode sd){
		sd.setNext(this);
		sd.setPrevious(getPrevious());
		setPrevious(sd);
	}
	
	public void addAfter(ShowDateListNode sd){
		sd.setNext(getNext());
		sd.setPrevious(this);
		setNext(sd);
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
}
