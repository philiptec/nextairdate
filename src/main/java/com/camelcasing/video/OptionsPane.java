package com.camelcasing.video;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class OptionsPane {

		private RadioButton useYesterday;
		private HBox pane;
		private Button getResults;
		
	public OptionsPane(){
		pane = new HBox();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(20, 20, 20, 20));
		pane.setSpacing(70);
		useYesterday = new RadioButton("Use Yesterday");
		getResults = new Button("Get Air Dates");
		getResults.setOnAction(e -> {
			if(!AirDatesPanel.isUpdateing()){
				AirDatesPanel.generateShowData(useYesterday.isSelected());
			}
		});
		
		pane.getChildren().add(useYesterday);
		pane.getChildren().add(getResults);
	}
	
	public HBox getPanel(){
		return pane;
	}
	
	public boolean useYesterday(){
		return useYesterday.isSelected();
	}
}
