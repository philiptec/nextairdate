package com.camelcasing.video;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class ShowList{
	
		private final Logger logger = LogManager.getLogger(getClass());

		private ArrayList<String> shows;
		private File showsFile = new File("/media/camelcasing/ExtraDrive/Java_Files/AirDate/.shows.xml");
		
	public ShowList(){
		createShowList();
	}
	
	private void createShowList(){
		shows = new ArrayList<String>(20);
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(showsFile);
			NodeList results = doc.getElementsByTagName("show");
				for(int i = 0; i < results.getLength(); i++){
					shows.add(results.item(i).getTextContent());
				}
		} catch (IOException | ParserConfigurationException e) {
			logger.error("Problem reading shows.xml file");
			shows.add("Problem reading shows.xml file");
		} catch (SAXException e) {
			logger.error("Problem reading shows.xml file");
			shows.add("Problem reading shows.xml file");
		}
	}
	
	public ArrayList<String> getShowList(){
		return shows;
	}
}
