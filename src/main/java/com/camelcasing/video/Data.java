package com.camelcasing.video;

import java.time.LocalDate;
import java.util.Iterator;

import javafx.collections.ObservableList;

public interface Data<E> extends Iterable<E>{

	int add(String show, LocalDate date, String episode);
	void clear();
	int indexOf(String show);
	int remove(String show);
	ObservableList<ShowAndDate> getShowDateEpisodeList();
	int size();
	E getFirst();
	Iterator<E> iterator();
}
