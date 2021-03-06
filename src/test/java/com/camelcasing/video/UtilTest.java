package com.camelcasing.video;

import java.time.LocalDate;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class UtilTest{
	
	@Test
	public void changeUpdateNumberTest(){
		int num = AirDateUtils.changeUpdateNumber();
		int num2 = AirDateUtils.changeUpdateNumber();
		assertTrue(num != num2);
	}

	@Test
	public void dateFormatTest(){
		LocalDate date = LocalDate.of(2015, 12, 25);
		String time = AirDateUtils.formattedDate(date);
		assertEquals("25/12/2015", time);
	}
	
	@Test
	public void viewDateFormatTest(){
		String date1 = "TBA";
		String date2 = "TODAY!";
		String date3 = "15/02/1991";
		assertEquals(AirDateUtils.TODAY, AirDateUtils.getViewDateFromString(date2));
		assertEquals(LocalDate.of(2170, 01, 01), AirDateUtils.getViewDateFromString(date1));
		assertEquals(LocalDate.of(1991, 02, 15), AirDateUtils.getViewDateFromString(date3));
	}
	
	@Test
	public void webAddressTest(){
		assertEquals("http://www.epguides.com/Cheers", AirDateUtils.getShowAddress("Cheers"));
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
		
		String date2 = "01/01/2013";
		LocalDate lDate2 = LocalDate.of(2013, 01, 01);
		assertTrue(lDate2.equals(AirDateUtils.getDateFromString(date2)));
		
		String date3 = "01/11/2012";
		LocalDate lDate3 = LocalDate.of(2013, 11, 01);
		assertFalse(lDate3.equals(AirDateUtils.getDateFromString(date3)));
	}
}
