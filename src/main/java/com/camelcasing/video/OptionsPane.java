package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class OptionsPane {

		private CheckBox updateTBA, updateAll;
		private BorderPane pane;
		private Button getResults;
		
	public OptionsPane(){
		pane = new BorderPane();
		pane.setPadding(new Insets(20, 20, 20, 20));
		
		updateTBA = new CheckBox("Update TBA");
		updateAll = new CheckBox("Update All");
		
		getResults = new Button("Get Air Dates");
		
		FlowPane options = new FlowPane();
		options.setHgap(10);
		options.getChildren().addAll(updateTBA, updateAll);
		
		pane.setCenter(options);
		BorderPane.setAlignment(getResults, Pos.CENTER);
		pane.setRight(getResults);
	}
	
	public Button getGoButton(){
		return getResults;
	}
	
	public boolean isUpdateTBA(){
		return updateTBA.isSelected();
	}
	
	public boolean isUpdateAll(){
		return updateAll.isSelected();
	}

	public BorderPane getPanel(){
		return pane;
	}
}
