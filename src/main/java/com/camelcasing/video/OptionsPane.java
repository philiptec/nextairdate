package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class OptionsPane {

		private CheckBox useYesterday, updateTBA, updateAll;
		private BorderPane pane;
		private Button getResults;
		private AirDates airDatesPanel;
		
	public OptionsPane(AirDates adp){
		this.airDatesPanel = adp;
		pane = new BorderPane();
		pane.setPadding(new Insets(20, 20, 20, 20));
		
		useYesterday = new CheckBox("Use Yesterday");
		updateTBA = new CheckBox("Update TBA");
		updateAll = new CheckBox("Update All");
		
		getResults = new Button("Get Air Dates");
		getResults.setOnAction(e -> {
			if(!airDatesPanel.isUpdateing()){
				airDatesPanel.generateShowData(useYesterday.isSelected(), updateTBA.isSelected(), updateAll.isSelected());
			}
		});
		
		FlowPane options = new FlowPane();
		options.setHgap(10);
		options.getChildren().addAll(useYesterday, updateTBA, updateAll);
		
		pane.setLeft(options);
		BorderPane.setAlignment(getResults, Pos.CENTER);
		pane.setRight(getResults);
	}
	
	public BorderPane getPanel(){
		return pane;
	}
	
	public boolean useYesterday(){
		return useYesterday.isSelected();
	}
}
