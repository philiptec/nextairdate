package com.camelcasing.video;

import java.io.*;

import javafx.beans.property.*;
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
		private Button updateButton, cancelButton, fileChooser, openExistingFile;
		private TextField textField;
		private CheckBox oneShot;
		private Label topText = new Label("Enter existing XML file path or click \"Use Existing\" below");
		private static UpdateXMLFile updateXMLFile;
		
		private SimpleBooleanProperty save = new SimpleBooleanProperty(true);	
		
	private UpdateXMLFile(String oldPath){
		
		stage = new Stage();
		stage.setResizable(false);
		topText.setPadding(new Insets(10,10,10,10));
		
		updateButton = new Button("Update");
		cancelButton = new Button("Cancel");
		openExistingFile = new Button("Use Existing");
		
		oneShot = new CheckBox("Save Location");
		oneShot.setSelected(true);
		oneShot.setPadding(new Insets(0, 30, 10, 0));
		save.bind(oneShot.selectedProperty());
		
		fileChooser = new Button();
		ImageView chooserImage = new ImageView(new Image("filesearch.png"));
		chooserImage.setFitWidth(30);
		chooserImage.setFitHeight(30);
		chooserImage.setSmooth(true);
		fileChooser.setGraphic(chooserImage);
		textField = new TextField(oldPath);
		
		cancelButton.setOnAction(e -> {
			stage.close();
		});
		
		fileChooser.setOnAction(e -> {
			File f = null;
			f = new FileChooser().showSaveDialog(stage);
			if(f != null){
				newXmlLocation = f;
				textField.setText(f.getAbsolutePath());
			}
		});
		
		openExistingFile.setOnAction(e -> {
			File f = null;
			f = new FileChooser().showOpenDialog(stage);
			if(f != null){
				newXmlLocation = f;
				textField.setText(f.getAbsolutePath());
			}
		});
		
		updateButton.setOnAction(e -> {
			if(newXmlLocation != null){
				fcl.updateXmlFile(save.getValue());
			}else if(textField.getText().length() > 0){
				newXmlLocation = new File(textField.getText());
				fcl.updateXmlFile(save.getValue());
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
		buttonLayout.setPadding(new Insets(10,10,10,10));
		buttonLayout.setSpacing(20);
		buttonLayout.getChildren().add(oneShot);
		buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);
		buttonLayout.getChildren().addAll(openExistingFile, updateButton, cancelButton);
		pane.setCenter(chooserLayout);
		pane.setBottom(buttonLayout);
		pane.setTop(topText);
		
		Scene scene = new Scene(pane);
		scene.getStylesheets().add("default.css");
		stage.setScene(scene);
	}
	
	public static UpdateXMLFile getInstance(String oldPath){
		if(updateXMLFile == null){
			updateXMLFile = new UpdateXMLFile(oldPath);
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
	
	public void setTopTextMessage(String message){
		topText.setText(message);
	}
}
