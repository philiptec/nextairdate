package com.camelcasing.video;

import java.time.LocalDate;
import java.util.Iterator;

public class ShowDateList implements Iterable<ShowDateListNode>{
	
		private ShowDateListNode sentinal, first, last;
		private int size;

	public ShowDateList(){
		sentinal = new ShowDateListNode(null, null, null, null);
	}
	
	public int add(String show, LocalDate date){
		size++;
		int count = 0;
		ShowDateListNode current = sentinal.getNext();
		while(current != null){
			if(date.isBefore(current.getDate())){
				ShowDateListNode newNode = new ShowDateListNode(show, date, null, null);
				current.addBefore(newNode);
				return count;
			}else{
				count++;
				current = current.getNext();
			}
		}
		last.setNext(new ShowDateListNode(show, date, last, null));
		return(count);
	}
	
	public int remove(String show){
		int count = 0;
		if(sentinal.getNext() == null) return -1;
		ShowDateListNode current = first;
		do{
			if(current.getShow().equals(show)){
				current.getPrevious().setNext(current.getNext());
				current = null;
				size--;
				return count;
			}else{
				count++;
				current = current.getNext();
			}
		}while(current.getNext() != null);
		return -1;
	}
	
	public int indexOf(String show){
		int count = 0;
		ShowDateListNode current = sentinal.getNext();
		while(current != null){
			if(current.getShow().equals(show)){
				return count;
			}else{
				count++;
				current = current.getNext();
			}
		}
		return -1;
	}
	
	public int size(){
		return size;
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
			return current.getNext() != null;
		}

		@Override
		public ShowDateListNode next(){
			ShowDateListNode r = current.getNext();
			current = r;
			return r;
		}
		
	}
}
