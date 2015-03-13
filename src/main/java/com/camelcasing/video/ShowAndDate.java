package com.camelcasing.video;

import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

public class ShowAndDate extends BorderPane{

		private Label show, date, episode;
		private String showName;
		private ContextMenu rightClickMenu;
		private ChangeListener changeListener;
		
	public ShowAndDate(String show, LocalDate date, ChangeListener changeListener){
		super();
		this.show = new Label(show);
		this.date = new Label(checkForSpecial(date));
		this.showName = show;
		this.changeListener = changeListener;
		this.setPadding(new Insets(2, 0, 2, 0));
		prefWidthProperty().bind(AirDate.stage.widthProperty().subtract(50));
		setLeft(this.show);
		setRight(this.date);
		
		rightClickMenu = new ContextMenu();
		this.setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.SECONDARY)){
				rightClickMenu.show(this, e.getScreenX(), e.getScreenY());
			}
		});
		MenuItem rightClickMenuItem = new MenuItem("Update " + show);
		rightClickMenuItem.setOnAction(e -> {
			if(!AirDateUtils.testInternetConnection())return;
			LocalDate newDate = new AirDateParser().parse(showName);

			if(!newDate.equals(getDate())){
				this.changeListener.updateDate(showName, newDate, true);
			}
		});
		rightClickMenu.getItems().add(rightClickMenuItem);
	}
	
	public String checkForSpecial(LocalDate date){
		if(date.equals(AirDateUtils.ERROR_DATE) || date == null) return "FAIL";
		if(date.equals(AirDateUtils.TBA_DATE)) return "TBA"; 
		int diff = date.compareTo(AirDateUtils.TODAY);
		if(diff == 0){
			return "TODAY!";
		}else if(diff < 0) return "";
		return AirDateUtils.englishDate(date);
	}
	
	public LocalDate getDate(){
		String oldDate = date.getText();
		if(oldDate.equals("")) return AirDateUtils.ERROR_DATE;
		if(oldDate.equals("TBA")) return AirDateUtils.TBA_DATE;
		if(oldDate.equals("FAIL")) return AirDateUtils.ERROR_DATE;
		if(oldDate.equals("TODAY!")) return AirDateUtils.TODAY;
		return(AirDateUtils.getDateFromString(oldDate));
	}
	
	public String getShowName(){
		return showName;
	}
	
	public void setShow(String show){
		this.show.setText(show);
		this.showName = show;
	}
	
	public void setEpisode(String episode){
		this.episode.setText(episode);
	}
	
	public void setDate(LocalDate date){
		this.date.setText(checkForSpecial(date));
		this.show.setText(show.getText() + " (updated)");
	}
	
	@Override
	public String toString(){
		return show.getText() + " " + date.getText();
	}
}
