package com.camelcasing.video;
	
import java.time.LocalDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AirDateTest {

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
}
