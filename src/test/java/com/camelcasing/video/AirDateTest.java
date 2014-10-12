package com.camelcasing.video;
	
import java.time.LocalDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AirDateTest {

	@Test
	public void dateFormatTest(){
		LocalDate date = LocalDate.of(2015, 12, 25);
		String time = AirDates.englishDate(date);
		assertEquals("25/12/2015", time);
	}
}
