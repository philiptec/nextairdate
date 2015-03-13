package com.camelcasing.video;
	
import java.time.LocalDate;

import org.apache.logging.log4j.*;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class AirDateTest {
	
		private final static Logger logger = LogManager.getLogger(AirDateTest.class);
	
	@Test
	public void ShowDateListIterationTest(){
		Data<ShowDateListNode> list = new ShowDateList();
		list.add("first", LocalDate.of(2074, 01, 04));
		list.add("second", LocalDate.of(2070, 02, 01));
		list.add("third", LocalDate.of(2072, 01, 02));
		list.add("fourth", LocalDate.of(2070, 01, 01));
		
		for(ShowDateListNode node : list){
			logger.info(node.toString());
		}
		ShowDateListNode node = list.getFirst();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("fourth 2070-01-01", node.toString());
		node = node.getNext();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("second 2070-02-01", node.toString());
		node = node.getNext();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("third 2072-01-02", node.toString());
		node = node.getNext();
		logger.debug("index of " + node.getShow() + " = " + list.indexOf(node.getShow()));
		assertEquals("first 2074-01-04", node.toString());	
	}
}
