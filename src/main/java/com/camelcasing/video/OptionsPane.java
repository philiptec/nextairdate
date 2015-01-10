package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class OptionsPane {

		private CheckBox updateTBA, updateAll;
		private BorderPane pane;
		private Button getResults;
		private MenuBar menuBar;
		
	public OptionsPane(){

	}
	
	public void init(){
		pane = new BorderPane();
		
		updateTBA = new CheckBox("Update TBA");
		updateAll = new CheckBox("Update All");
		getResults = new Button("Get Air Dates");
		
		FlowPane options = new FlowPane();
		options.setHgap(10);
		options.setPadding(new Insets(0, 20, 10, 20));
		options.getChildren().addAll(updateTBA, updateAll, getResults);
		
		pane.setCenter(options);
		if(menuBar != null){
			StackPane r = new StackPane(menuBar);
			r.setPadding(new Insets(0, 0, 10, 0));
			pane.setTop(r);
		}
	}
	
	public Button getGoButton(){
		return getResults;
	}
	
	public boolean isUpdateTBA(){
		return updateTBA.isSelected();
	}
	
	public void setMenuBar(MenuBar menuBar){
		this.menuBar = menuBar;
	}
	
	public boolean isUpdateAll(){
		return updateAll.isSelected();
	}

	public BorderPane getPanel(){
		return pane;
	}
}
