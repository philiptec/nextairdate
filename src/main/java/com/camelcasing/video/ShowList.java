package com.camelcasing.video;

import java.io.*;
import java.util.prefs.Preferences;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ShowList{
	
		private Logger logger = LogManager.getLogger(getClass());

		private ShowDateList showDateList;
		private File showsFile;
		private boolean writing;
		
		private final String PREFS_NAME = "com/camelcasing/video";
		
	public ShowList(){
		retrieveXmlFileFromPreferences();
		if(showsFile != null) createShowList();
	}
	
	protected void createShowList(){
		if(showDateList == null){
			showDateList = new ShowDateList();
		}else{
			showDateList.clear();
		}
		try {
			logger.debug("ShowFile = " + showsFile.getAbsolutePath());
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(showsFile);
			NodeList results = doc.getElementsByTagName("show");
				for(int i = 0; i < results.getLength(); i++){
					Node n = results.item(i);
					Node a = n.getAttributes().item(0);
					showDateList.add(n.getTextContent(), AirDateUtils.getDateFromString(a.getTextContent()));
				}
			logger.debug(showDateList.size() + " shows after reading xmlFile" );
		} catch (IOException | ParserConfigurationException e) {
			logger.error("Problem reading shows.xml file");
		} catch (SAXException e) {
			logger.error("Problem reading shows.xml file");
		}
	}
	
	public boolean writeNewAirDates(){
		try {
			writing = true;
			logger.debug("Writing to xml started");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("shows");
			for(ShowDateListNode node : showDateList){
				Element e = doc.createElement("show");
				e.setTextContent(node.getShow());
				e.setAttribute("date", checkForFailCodes(AirDateUtils.englishDate(node.getDate())));
				root.appendChild(e);
			}
			doc.appendChild(root);
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			trans.transform(new DOMSource(doc), new StreamResult(showsFile));
			
		} catch (ParserConfigurationException | TransformerFactoryConfigurationError | TransformerException e) {
			logger.error("Failed to update xml");
			writing = false;
			return false;
		}finally{
			writing = false;
		}
		logger.debug("writing xml completed");
		return true;
	}

	private String checkForFailCodes(String date){
		if(date.equals("FAIL")) return "01/01/2170";
		return date;
	}
	
	public void retrieveXmlFileFromPreferences(){
		Preferences prefs = Preferences.userRoot().node(PREFS_NAME);
		String fileName = prefs.get("xmlFile", "notFound");
		if(fileName.equals("notFound")){
			logger.info("xmlFile location not found in preferences");
			return;
		}
		File xmlFile = new File(fileName);
		if(xmlFile.exists()){
			this.showsFile = xmlFile;
			logger.info("XML file is " + xmlFile.getAbsolutePath());
		}else{
			logger.error("XML file found in preferences but no longer exists");
		}
	}
	
	public void setXmlFileInPreferences(){
		if(!showsFile.exists()){
			logger.error("Trying to set xmlFile to preferences but file does not exists");
			return;
		}
		String filePath = showsFile.getAbsolutePath();
		Preferences prefs = Preferences.userRoot().node(PREFS_NAME);
		logger.debug("setting \"" + filePath + "\" to preferences");
		prefs.put("xmlFile", filePath);
	}
	
	public void setXmlFile(File newFile){
		showsFile = newFile;
	}
	
	public void addShow(String showName){
		addShowAndDate(showName, "01/01/1970");
	}
	
	public void addShowAndDate(String showName, String dateValue){
		showDateList.add(showName, AirDateUtils.getDateFromString(dateValue));
	}
	
	public void removeShow(String show){
		showDateList.remove(show);
	}
	
	public boolean isWriting(){
		return writing;
	}
	
	public ShowDateList getShowDateList(){
		return showDateList;
	}
	
	public void setShowDateList(ShowDateList showDateList){
		this.showDateList = showDateList;
	}
}
