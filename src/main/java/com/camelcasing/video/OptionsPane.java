package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;

public class OptionsPane {

		private RadioButton useYesterday;
		private BorderPane pane;
		private Button getResults;
		private AirDates airDatesPanel;
		
	public OptionsPane(AirDates adp){
		this.airDatesPanel = adp;
		pane = new BorderPane();
		pane.setPadding(new Insets(20, 20, 20, 20));
		
		useYesterday = new RadioButton("Use Yesterday");
		
		getResults = new Button("Get Air Dates");
		getResults.setOnAction(e -> {
			if(!airDatesPanel.isUpdateing()){
				airDatesPanel.generateShowData(useYesterday.isSelected());
			}
		});
		
		pane.setLeft(useYesterday);
		pane.setRight(getResults);
	}
	
	public BorderPane getPanel(){
		return pane;
	}
	
	public boolean useYesterday(){
		return useYesterday.isSelected();
	}
}
