package com.camelcasing.video;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class ClassPathTest {

	@Test
	public void test() {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		URL[] classPathURLs = ((URLClassLoader)loader).getURLs();
		for(URL url : classPathURLs){
			System.out.println(url);
		}
	}

}
