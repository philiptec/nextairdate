package com.camelcasing.video;

import java.time.LocalDate;
import java.util.Iterator;

import org.apache.logging.log4j.*;

public class ShowDateList implements Iterable<ShowDateListNode>{
	
		private Logger logger = LogManager.getLogger(getClass());
	
		private ShowDateListNode sentinal;
		private int size;

	public ShowDateList(){
		sentinal = new ShowDateListNode(null, null);
	}
	
	public int add(String show, LocalDate date){
		int count = size;
		size++;
		ShowDateListNode current = sentinal.getPrevious();
		while(current.getShow() != null){
			if(date.equals(current.getDate()) || date.isAfter(current.getDate())){
				ShowDateListNode newNode = new ShowDateListNode(show, date, null, null);
				current.addAfter(newNode);
				return count;
			}else{
				count--;
				current = current.getPrevious();
			}
		}
		sentinal.addAfter(new ShowDateListNode(show, date, sentinal, sentinal));
		return count;
	}
	
	public void clear(){
		logger.debug("ShowDateList reset");
		sentinal = new ShowDateListNode(null, null);
		size = 0;
	}
	
	public int remove(String show){
		logger.debug("removing: " + show);
		int count = 0;
		ShowDateListNode current = sentinal.getNext();
		do{
			if(current.getShow().equals(show)){
				current.getPrevious().setNext(current.getNext());
				current.getNext().setPrevious(current.getPrevious());
				size--;
				return count;
			}else{
				count++;
				current = current.getNext();
			}
		}while(current.getNext().getShow() != null);
		return -1;
	}
	
	public int indexOf(String show){
		int count = 0;
		ShowDateListNode current = sentinal.getNext();
		while(current.getShow() != null){
			if(current.getShow().equals(show)){
				return count;
			}else{
				count++;
				current = current.getNext();
			}
		}
		logger.debug(show + " not found");
		return -1;
	}
	
	public ShowDateListNode get(String show){
		ShowDateListNode current = sentinal.getNext();
		while(current.getShow() != null){
			if(current.getShow().equals(show)){
				return current;
			}else{
				current = current.getNext();
			}
		}
		return null;
	}
	
	public int size(){
		return size;
	}
	
	public ShowDateListNode getFirst(){
		return sentinal.getNext();
	}

	@Override
	public Iterator<ShowDateListNode> iterator() {
		return new ShowDateIterator();
	}
	
	public class ShowDateIterator implements Iterator<ShowDateListNode>{
		
		private ShowDateListNode current;
		
		ShowDateIterator(){
			current = sentinal;
		}

		@Override
		public boolean hasNext() {
			return current.getNext().getShow() != null;
		}

		@Override
		public ShowDateListNode next(){
			current = current.getNext();
			return current;
		}
	}
}
