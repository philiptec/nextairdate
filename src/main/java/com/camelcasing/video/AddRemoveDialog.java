package com.camelcasing.video;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class AddRemoveDialog implements AddRemoveController{
	
		private Logger logger = LogManager.getLogger(getClass());
	
		private static AddRemoveDialog instance;
		private static List<String> shows;
		private AddRemoveListener addRemoveListener;
		private Stage stage;
		private BorderPane pane, centrePane;
		private Button execute, cancel, add;
		private ComboBox<String> showSelectionBox;
		private TextField addShowField;
		private ScrollPane updatesPane;
		private VBox leftPane, updatesBox;
		private HBox addBox, removeBox;
		private Label addLabel, removeLabel;
		private List<String> addShows, deleteShows;
		
		private boolean active;

	private AddRemoveDialog(){
		stage = new Stage();
		pane = new BorderPane();
		pane.setPrefSize(600, 400);
		centrePane = new BorderPane();
		leftPane = new VBox();
		leftPane.setAlignment(Pos.CENTER);
		leftPane.setSpacing(40);
		
		updatesBox = new VBox();
		
		addLabel = new Label("Add Show");
		removeLabel = new Label("Remove Show");
		
		addBox = new HBox();
		addBox.setSpacing(20);
		addBox.setPadding(new Insets(10, 10, 10, 10));
		removeBox = new HBox();
		removeBox.setSpacing(20);
		removeBox.setPadding(new Insets(10, 10, 10, 10));
		
		add = new Button("Add");
		add.setOnAction(e -> addAddItem());
		
		addShowField = new TextField();
		addShowField.setOnAction(e -> addAddItem());
		
		updatesPane = new ScrollPane(updatesBox);
		
		showSelectionBox = new ComboBox<String>();
		showSelectionBox.setPromptText("SelectShow");
		showSelectionBox.setOnAction(e ->{
			addDeleteItem(showSelectionBox.getValue());
		});
		
		execute = new Button("Update");
		execute.setOnAction(e -> {
			logger.debug("shows List size = " + addShows.size());
			logger.debug("delete List size = " + deleteShows.size());
			addRemoveListener.executeAddRemoveQuery(addShows, deleteShows);
			active = false;
			stage.close();
		});
		cancel = new Button("Cancel");
		cancel.setOnAction(e -> {
			active = false;
			stage.close();
		});
		
		HBox buttonLayout = new HBox();
		buttonLayout.setAlignment(Pos.BOTTOM_RIGHT);
		buttonLayout.setSpacing(20);
		buttonLayout.setPadding(new Insets(10,10,10,10));
		buttonLayout.getChildren().addAll(execute, cancel);
		
		addBox.getChildren().addAll(addLabel, addShowField, add);
		removeBox.getChildren().addAll(removeLabel, showSelectionBox);
		
		leftPane.getChildren().addAll(addBox, removeBox);
		
		centrePane.setLeft(leftPane);
		centrePane.setCenter(updatesPane);
		
		pane.setCenter(centrePane);
		pane.setBottom(buttonLayout);
		stage.setScene(new Scene(pane));
	}
	
	private void populateComboBox(){
		for(String s : shows) showSelectionBox.getItems().add(s);
	}
	
	public void addAddItem(){
		String show = addShowField.getText();
		if(show != null && show.length() > 0){
			updatesBox.getChildren().add(createAddItem(show));
			addShowField.setText("");
		}
	}
	
	public void addDeleteItem(String show){
		if(!deleteShows.contains(show)){
			updatesBox.getChildren().add(createDeleteItem(show));
		}
	}
	
	public HBox createAddItem(String show){
		addShows.add(show);
		HBox b = new HBox();
		b.setPadding(new Insets(2, 5, 2, 5));
		b.setSpacing(10);
		Text t = new Text("+");
		t.setFont(new Font(16));
		t.setFill(Color.GREEN);
		b.getChildren().addAll(t, new Label(show));
		return b;
	}
	
	public HBox createDeleteItem(String show){
		deleteShows.add(show);
		HBox b = new HBox();
		b.setPadding(new Insets(2, 5, 2, 5));
		b.setSpacing(10);
		Text t = new Text(" -");
		t.setFont(new Font(16));
		t.setFill(Color.RED);
		b.getChildren().addAll(t, new Label(show));
		return b;
	}
	
	public void reset(){
		addShows = new ArrayList<String>();
		deleteShows = new ArrayList<String>();
		updatesBox.getChildren().clear();
	}
	
	public void show(List<String> shows){
		if(!active){
			active = true;
			reset();
			AddRemoveDialog.shows = shows;
			populateComboBox();
			stage.show();
		}
	}
	
	public static AddRemoveDialog getInstance(){
		if(instance == null){
			instance = new AddRemoveDialog();
		}
		return instance;
	}

	@Override
	public void setAddRemoveListener(AddRemoveListener l){
		addRemoveListener = l;
	}
}
