package com.camelcasing.video;

import java.time.LocalDate;

public interface ChangeListener {
		void updateDate(String show, LocalDate date, String episode, boolean save);
		void saveDates();
}
