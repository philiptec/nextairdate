package com.camelcasing.video;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Paint;


public class ShowAndDate{

		private Logger logger = LogManager.getLogger(getClass());
		private LocalDate d; 
		private final String showName;
		private ObjectProperty<Node> show;
		private StringProperty date;
		private StringProperty episode;
		private final Text showText;
		
	public ShowAndDate(String show, LocalDate date, String episode){
		this.d = date;
		this.date = new SimpleStringProperty(checkForSpecial(date));
		this.episode = new SimpleStringProperty(episode);
		this.showName = show;
		this.showText = new Text(show);
		createContextMenu(showText);
		
		showText.setFont(AirDateUtils.font);
		this.show = new SimpleObjectProperty<Node>(showText);
		
	}
	
	public ContextMenu createContextMenu(Node node){
		ContextMenu menu = new ContextMenu();
		MenuItem updateAirDate = new MenuItem("update");
		MenuItem openWebsite = new MenuItem("open in browser");
		
		updateAirDate.setOnAction(e -> {
			AirDate.updateSingleShow(showName, episode.getValue());
		});
		
		openWebsite.setOnAction(e -> {
			try{
				String s = AirDateUtils.getShowAddress(showName);
				URI uri = new URI(s.trim());
				System.out.println(uri);
				Thread t = new Thread(() -> {
					try{
						Desktop.getDesktop().browse(uri);
					}catch(IOException e2){
						logger.error("Open in browser failed");
						JOptionPane.showMessageDialog(null, "Could not find default browser", "Browser not found", JOptionPane.ERROR_MESSAGE);
					}
				});
				t.start();
			}catch(URISyntaxException e2){
				logger.info("incorrect URI");
			}
		});
		
		menu.getItems().addAll(updateAirDate, openWebsite);
		node.setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.SECONDARY)){
				menu.show(node, e.getScreenX(), e.getScreenY());
			}
		});
		return menu;
	}
	
	public String checkForSpecial(LocalDate date){
		if(date.equals(AirDateUtils.ERROR_DATE) || date == null) return "FAIL";
		if(date.equals(AirDateUtils.TBA_DATE)) return "TBA"; 
		int diff = date.compareTo(AirDateUtils.TODAY);
		if(diff == 0){
			return "TODAY!";
		}else if(diff < 0) return "";
		return AirDateUtils.formattedDate(date);
	}
	
	public String getShowName(){
		return showName;
	}
	
	public Node getShow(){
		return show.getValue();
	}
	
	public String getDate(){
		return date.getValue();
	}
	
	public String getEpisode(){
		return episode.getValue();
	}
	
	public String getEpisodeNumber(String e){
		return e.substring(e.indexOf('-') + 1);
	}
	
	public LocalDate getDateAsLocalDate(){
		return d;
	}
	
	public void setEpisode(String episode){
		this.episode.setValue(episode);
	}

	public void setDate(LocalDate date, String episode){
		HBox node = new HBox(5.0);
		createContextMenu(node);
		node.setAlignment(Pos.CENTER_LEFT);
		
		int currentEpisodeNumber = Integer.valueOf(getEpisodeNumber(getEpisode()));
		int newEpisodeNumber = Integer.valueOf(getEpisodeNumber(episode));
		int diff = newEpisodeNumber - currentEpisodeNumber;
		
		if(diff == 0){
			Circle c = new Circle(5.0);
			c.setFill(Paint.valueOf("orange"));
			node.getChildren().add(c);
		}else{
			for(int i = 0; i < diff; i++){
				Circle c = new Circle(5.0);
				c.setFill(Paint.valueOf("green"));
				node.getChildren().add(c);
			}
		}
		
		node.getChildren().add(showText);
		
		this.date.setValue(checkForSpecial(date));
		this.show.setValue(node);
	}
	
	@Override
	public String toString(){
		return showName + " " + date;
	}
}
