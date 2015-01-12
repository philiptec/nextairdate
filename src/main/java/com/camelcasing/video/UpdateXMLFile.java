package com.camelcasing.video;

import java.io.*;

import javafx.beans.property.SimpleStringProperty;
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
		private Button updateButton, cancelButton, fileChooser, createNewFile;
		private TextField textField;
		private CheckBox oneShot;
		private final Label topText = new Label("Enter existing XML file path or click \"Create New\" below");
		private static UpdateXMLFile updateXMLFile;
		
		private boolean save = true;
		
		private SimpleStringProperty theme = new SimpleStringProperty();
		
	private UpdateXMLFile(){
		
		theme.bind(AirDate.theme);
		
		stage = new Stage();
		stage.setResizable(false);
		topText.setPadding(new Insets(10,10,10,10));
		
		updateButton = new Button("Update");
		cancelButton = new Button("Cancel");
		createNewFile = new Button("Create New");
		
		oneShot = new CheckBox("Save Location");
		oneShot.setSelected(true);
		oneShot.setPadding(new Insets(0, 30, 10, 0));
		
		fileChooser = new Button();
		ImageView chooserImage = new ImageView(new Image("filesearch.png"));
		chooserImage.setFitWidth(30);
		chooserImage.setFitHeight(30);
		chooserImage.setSmooth(true);
		fileChooser.setGraphic(chooserImage);
		textField = new TextField();
		
		oneShot.setOnAction(e -> save = oneShot.isSelected());
		
		cancelButton.setOnAction(e -> {
			stage.close();
		});
		
		fileChooser.setOnAction(e -> {
			File f = null;
			f = new FileChooser().showOpenDialog(stage);
			if(f != null){
				newXmlLocation = f;
				textField.setText(f.getAbsolutePath());
			}
		});
		
		createNewFile.setOnAction(e -> {
			File f = null;
			f = new FileChooser().showSaveDialog(stage);
			if(f != null){
				newXmlLocation = f;
				textField.setText(f.getAbsolutePath());
			}
		});
		
		updateButton.setOnAction(e -> {
			if(newXmlLocation != null){
				fcl.updateXmlFile(save);
			}else if(textField.getText().length() > 0){
				newXmlLocation = new File(textField.getText());
				fcl.updateXmlFile(save);
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
		//buttonLayout.setAlignment(Pos.BOTTOM_LEFT);
		buttonLayout.setPadding(new Insets(10,10,10,10));
		buttonLayout.setSpacing(20);
		buttonLayout.getChildren().add(oneShot);
		buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);
		buttonLayout.getChildren().addAll(createNewFile, updateButton, cancelButton);
		pane.setCenter(chooserLayout);
		pane.setBottom(buttonLayout);
		pane.setTop(topText);
		
		Scene scene = new Scene(pane);
		scene.getStylesheets().add(theme.getValue());
		stage.setScene(scene);
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
