package com.camelcasing.video;

import java.io.*;

import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;

public class UpdateXMLFile implements FileChooserController{

		private File newXmlLocation;
		private FileChooserListener fcl;
		private Stage stage;
		private Button updateButton, cancelButton, fileChooser;
		private TextField textField;
		private final Label topText = new Label("Enter or select new XML file location");
		private static UpdateXMLFile updateXMLFile;
		
	private UpdateXMLFile(){
		stage = new Stage();
		
		topText.setPadding(new Insets(10,10,10,10));
		
		updateButton = new Button("Update");
		cancelButton = new Button("Cancel");
		
		fileChooser = new Button();
		ImageView chooserImage = new ImageView(new Image("filesearch.png"));
		chooserImage.setFitWidth(30);
		chooserImage.setFitHeight(30);
		chooserImage.setSmooth(true);
		fileChooser.setGraphic(chooserImage);
		textField = new TextField();
		
		cancelButton.setOnAction(e -> {
			stage.close();
		});
		
		fileChooser.setOnAction(e -> {
			File f = null;
			f = new FileChooser().showOpenDialog(stage);
			if(f != null) newXmlLocation = f;
			textField.setText(f.getAbsolutePath());
		});
		
		updateButton.setOnAction(e -> {
			if(newXmlLocation != null){
				fcl.submitPressed();
			}else if(textField.getText().length() > 0){
				newXmlLocation = new File(textField.getText());
				fcl.submitPressed();
			}else{
				stage.close();
			}
			stage.close();
		});
		
		BorderPane pane = new BorderPane();
		
		HBox chooserLayout = new HBox();
		chooserLayout.setAlignment(Pos.CENTER);
		chooserLayout.setPadding(new Insets(10,10,10,10));
		chooserLayout.setSpacing(30);
		textField.setPrefWidth(400);
		chooserLayout.getChildren().addAll(textField, fileChooser);
		
		HBox buttonLayout = new HBox();
		buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);
		buttonLayout.setPadding(new Insets(10,10,10,10));
		buttonLayout.setSpacing(20);
		buttonLayout.getChildren().addAll(cancelButton, updateButton);
		pane.setCenter(chooserLayout);
		pane.setBottom(buttonLayout);
		pane.setTop(topText);
		
		stage.setScene(new Scene(pane));
	}
	
	public static UpdateXMLFile getInstance(){
		if(updateXMLFile == null){
			updateXMLFile = new UpdateXMLFile();
		}
		return updateXMLFile;
	}
	
	public void show(){
		stage.show();
	}

	public File getNewXmlLocation(){
		return newXmlLocation;
	}

	@Override
	public void setFileChooserListener(FileChooserListener fcl) {
		this.fcl = fcl;
	}
}
