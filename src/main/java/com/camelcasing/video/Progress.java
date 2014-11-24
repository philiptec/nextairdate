package com.camelcasing.video;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;

public class Progress {

		private ProgressBar progressBar;
		private HBox pane;
	
	public Progress(){
		pane = new HBox();
		progressBar = new ProgressBar();
		progressBar.prefWidthProperty().bind(pane.widthProperty());
	}
	
	public void addProgressBar(){
		pane.getChildren().add(progressBar);
	}
	
	public void removeProgressBar(){
		pane.getChildren().remove(0);
	}
	
	public HBox getProgressPane(){
		return pane;
	}
	
	public void updateProgress(double inc){
		progressBar.setProgress(inc);
	}
}
