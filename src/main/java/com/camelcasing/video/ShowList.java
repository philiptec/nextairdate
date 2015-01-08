package com.camelcasing.video;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ShowList{
	
		private final Logger logger = LogManager.getLogger(getClass());

		private List<String> shows, dates;
		private File showsFile = new File("/media/camelcasing/ExtraDrive/Java_Files/AirDate/shows.xml");
		private boolean writing;
		
	public ShowList(){
		createShowList();
	}
	
	private void createShowList(){
		shows = new ArrayList<String>(20);
		dates = new ArrayList<String>(20);
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(showsFile);
			NodeList results = doc.getElementsByTagName("show");
				for(int i = 0; i < results.getLength(); i++){
					Node n = results.item(i);
					Node a = n.getAttributes().item(0);
					shows.add(n.getTextContent());
					dates.add(a.getTextContent());
				}
		} catch (IOException | ParserConfigurationException e) {
			logger.error("Problem reading shows.xml file");
			shows.add("Problem reading shows.xml file");
		} catch (SAXException e) {
			logger.error("Problem reading shows.xml file");
			shows.add("Problem reading shows.xml file");
		}
	}
	
	public boolean writeNewAirDates(){
		try {
			writing = true;
			logger.debug("Writing to xml started");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(showsFile);
			NodeList showList = doc.getElementsByTagName("show");
			for(int i = 0; i < showList.getLength(); i++){
				Node n = showList.item(i);
				String date = dates.get(i);
				if(date.equals("FAIL")) date = "01/01/1970";
				n.getAttributes().item(0).setTextContent(date);
			}
			
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			trans.transform(new DOMSource(doc), new StreamResult(showsFile));
			
		} catch (ParserConfigurationException | SAXException | IOException | TransformerFactoryConfigurationError | TransformerException e) {
			logger.error("Failed to update xml");
			writing = false;
			return false;
		}finally{
			writing = false;
		}
		logger.debug("writing xml completed");
		return true;
	}

	public String getDate(String show){
		int index = shows.indexOf(show);
		if(index == -1) return null;
		return dates.get(index);
	}
	
	public boolean updateDate(String show, String newDate){
		int index = shows.indexOf(show);
		if(index == -1) return false;
		
		dates.set(index, newDate);
		return true;
	}
	
	public void setXmlFile(File newFile){
		showsFile = newFile;
	}
	
	public boolean isWriting(){
		return writing;
	}
	
	public List<String> getShowList(){
		return shows;
	}
	
	public List<String> getDateList(){
		return dates;
	}
	
	public void setDateList(List<String> dates){
		this.dates = dates;
	}
	
	public void setShowDates(ArrayList<String> shows){
		this.shows = shows;
	}
}
