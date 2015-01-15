package com.camelcasing.video;
	
import java.time.LocalDate;

import org.apache.logging.log4j.*;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AirDateTest {
	
		private final static Logger logger = LogManager.getLogger(AirDateTest.class);

	@Test
	public void dateFormatTest(){
		LocalDate date = LocalDate.of(2015, 12, 25);
		String time = AirDateUtils.englishDate(date);
		assertEquals("25/12/2015", time);
	}
	
	@Test
	public void datesAreDifferentText(){
		AirDates ad = new AirDates();
		assertEquals(false, ad.datesAreDifferent("01/01/2015", "01/01/2015"));
		assertEquals(false, ad.datesAreDifferent("01/01/2015", "FAIL"));
		assertEquals(true, ad.datesAreDifferent("01/01/2015", "01/02/2015"));
	}
	
	@Test
	public void ShowDateListIterationTest(){
		ShowDateList list = new ShowDateList();
		list.add("first", LocalDate.of(1974, 01, 04));
		list.add("second", LocalDate.of(1970, 02, 01));
		list.add("third", LocalDate.of(1972, 01, 02));
		list.add("fourth", LocalDate.of(1970, 01, 01));
		
		for(ShowDateListNode node : list){
			logger.info(node.toString());
		}
		ShowDateListNode node = list.getFirst();
		assertEquals("fourth 1970-01-01", node.toString());
		node = node.getNext();
		assertEquals("second 1970-02-01", node.toString());
		node = node.getNext();
		assertEquals("third 1972-01-02", node.toString());
		node = node.getNext();
		assertEquals("first 1974-01-04", node.toString());
		
		
	}
}
