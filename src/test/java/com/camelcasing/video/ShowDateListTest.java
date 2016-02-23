package com.camelcasing.video;
	
import java.time.LocalDate;

import org.apache.logging.log4j.*;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class ShowDateListTest {
	
		private final static Logger logger = LogManager.getLogger(ShowDateListTest.class);
		private Data<ShowDateListNode> list;
	
	@Before
	public void populateList(){
		list = new ShowDateList();
		list.add("first", LocalDate.of(2074, 01, 04), "1-1");
		list.add("second", LocalDate.of(2070, 02, 01), "1-1");
		list.add("third", LocalDate.of(2072, 01, 02), "1-1");
		list.add("fourth", LocalDate.of(2070, 01, 01), "1-1");
	}
	
	@Test
	public void loopTest(){
		int count = 0;
		for(ShowDateListNode n : list){
			count++;
		}
		assertEquals(4, count);
	}
	
	@Test
	public void sizeTest(){
		assertEquals(4, list.size());
	}
	
	@Test
	public void clearTest(){
		list.clear();
		assertEquals(0, list.size());
	}
	
	@Test
	public void removeLast(){
		assertEquals(4, list.size());
		list.remove("first");
		assertEquals(3, list.size());
		ShowDateListNode node = list.getFirst();
		assertEquals("fourth 2070-01-01 1-1", node.toString());
		node = node.getNext();
		assertEquals("second 2070-02-01 1-1", node.toString());
		node = node.getNext();
		assertEquals("third 2072-01-02 1-1", node.toString());
		node = node.getNext();
		assertEquals(null, node.getShow());
	}
	
	@Test
	public void removeMiddle(){
		assertEquals(4, list.size());
		list.remove("second");
		assertEquals(3, list.size());
		ShowDateListNode node = list.getFirst();
		assertEquals("fourth 2070-01-01 1-1", node.toString());
		node = node.getNext();
		assertEquals("third 2072-01-02 1-1", node.toString());
		node = node.getNext();
		assertEquals("first 2074-01-04 1-1", node.toString());	
		node = node.getNext();
		assertEquals(null, node.getShow());
	}
	
	@Test
	public void removeFirst(){
		assertEquals(4, list.size());
		list.remove("fourth");
		assertEquals(3, list.size());
		ShowDateListNode node = list.getFirst();
		assertEquals("second 2070-02-01 1-1", node.toString());
		node = node.getNext();
		assertEquals("third 2072-01-02 1-1", node.toString());
		node = node.getNext();
		assertEquals("first 2074-01-04 1-1", node.toString());	
		node = node.getNext();
		assertEquals(null, node.getShow());
	}
		
	@Test
	public void ShowDateListIterationTest(){

		ShowDateListNode node = list.getFirst();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("fourth 2070-01-01 1-1", node.toString());
		node = node.getNext();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("second 2070-02-01 1-1", node.toString());
		node = node.getNext();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("third 2072-01-02 1-1", node.toString());
		node = node.getNext();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("first 2074-01-04 1-1", node.toString());	
	}
}
