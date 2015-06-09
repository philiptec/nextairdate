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

		private Data<ShowDateListNode> showDateList;
		private File showsFile;
		private boolean writing;
		
		private final String PREFS_NAME = "com/camelcasing/video";
		
	public ShowList(){
		retrieveXmlFileFromPreferences();
		if(showsFile != null){
			createShowList();
		}else{
			showDateList = new ShowDateList();
		}
	}
	
	protected void createShowList(){
		if(showDateList == null){
			showDateList = new ShowDateList();
		}else{
			showDateList.clear();
		}
		try {
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(showsFile);
			NodeList results = doc.getElementsByTagName("show");
				for(int i = 0; i < results.getLength(); i++){
					Node n = results.item(i);
					Node a = n.getAttributes().item(0);
					Node e = n.getAttributes().item(1);
					showDateList.add(n.getTextContent(), AirDateUtils.getDateFromString(a.getTextContent()), e.getTextContent());
				}
		} catch (IOException | ParserConfigurationException e) {
			logger.error("Problem reading shows.xml file");
		} catch (SAXException e) {
			logger.error("Problem reading shows.xml file");
		}
	}
	
	public boolean writeNewAirDates(){
		try {
			if(!checkXmlFile(showsFile)) return false;
			writing = true;
			logger.debug("Writing to xml started");
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("shows");
			for(ShowDateListNode node : showDateList){
				Element e = doc.createElement("show");
				e.setTextContent(node.getShow());
				e.setAttribute("date", node.getDateAsString());
				e.setAttribute("episode", node.getEpisode());
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
	
	private boolean checkXmlFile(File showsFile){
		return(showsFile != null && showsFile.exists());
	}
	
	public void retrieveXmlFileFromPreferences(){
		Preferences prefs = Preferences.userRoot().node(PREFS_NAME);
		String fileName = prefs.get("xmlFile", "notFound");
		if(fileName.equals("notFound")){
			logger.info("xmlFile location not found in preferences");
			return;
		}
		File xmlFile = new File(fileName);
		if(checkXmlFile(xmlFile)){
			this.showsFile = xmlFile;
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
	
	public String getXmlFileLocation(){
		try {
			if(showsFile == null) return "";
			return showsFile.getCanonicalPath();
		} catch (IOException e) {
			return("");
		}
	}
	
	public boolean isWriting(){
		return writing;
	}
	
	public Data<ShowDateListNode> getShowDateList(){
		return showDateList;
	}
	
	public void setShowDateList(Data<ShowDateListNode> showDateList){
		this.showDateList = showDateList;
	}
}
