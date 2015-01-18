package com.camelcasing.video;

import java.time.LocalDate;

public interface ChangeController {
	void addChangeListener(ChangeListener l);
	void updateListeners(String show, LocalDate date, boolean save);
}
