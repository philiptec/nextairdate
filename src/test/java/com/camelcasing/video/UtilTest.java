package com.camelcasing.video;

import java.time.LocalDate;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@Ignore
public class UtilTest{

	@Test
	public void dateFormatTest(){
		LocalDate date = LocalDate.of(2015, 12, 25);
		String time = AirDateUtils.englishDate(date);
		assertEquals("25/12/2015", time);
	}
	
	@Test
	public void todayOrAfterTest(){
		LocalDate compareToDate = LocalDate.of(1999, 12, 31);
		assertEquals(true, AirDateUtils.todayOrAfter(LocalDate.of(2000, 01, 01), compareToDate));
		assertEquals(true, AirDateUtils.todayOrAfter(LocalDate.of(1999, 12, 31), compareToDate));
		assertEquals(false, AirDateUtils.todayOrAfter(LocalDate.of(1999, 11, 30), compareToDate));
	}
	
	@Test
	public void getDateFromStringTest(){
		String date1 = "01/11/2012";
		LocalDate lDate1 = LocalDate.of(2012, 11, 01);
		assertTrue(lDate1.equals(AirDateUtils.getDateFromString(date1)));
		
		String date2 = "1/1/2013";
		LocalDate lDate2 = LocalDate.of(2013, 01, 01);
		assertTrue(lDate2.equals(AirDateUtils.getDateFromString(date2)));
		
		String date3 = "01/11/2012";
		LocalDate lDate3 = LocalDate.of(2013, 11, 01);
		assertFalse(lDate3.equals(AirDateUtils.getDateFromString(date3)));
	}
}
