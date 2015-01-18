package com.camelcasing.video;

import java.time.LocalDate;
import java.util.Iterator;

public interface Data<E> extends Iterable<E>{

	int add(String show, LocalDate date);
	void clear();
	int indexOf(String show);
	int remove(String show);
	int size();
	E getFirst();
	Iterator<E> iterator();
}
