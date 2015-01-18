package com.camelcasing.video;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

import javafx.scene.control.*;

public class AddRemoveDialog implements AddRemoveController{
	
		private Logger logger = LogManager.getLogger(getClass());
	
		private static AddRemoveDialog instance;
		private static ShowDateList showDateList;
		private AddRemoveListener addRemoveListener;
		private Stage stage;
		private Button execute, cancel, add;
		private ComboBox<String> showSelectionBox;
		private TextField addShowField;
		private ScrollPane proposedUpdatesScrollPane;
		private VBox proposedUpdatesContainer;
		private Label addLabel, removeLabel;
		private List<String> addShows, deleteShows;
		
		private SimpleStringProperty theme = new SimpleStringProperty();
		
		private boolean active;

	private AddRemoveDialog(){
		
		theme.bind(AirDate.theme);
		
		stage = new Stage();
		stage.setResizable(false);
		stage.setOnCloseRequest(we -> {
			active = false;
			stage.close();
		});

		GridPane backPane = new GridPane();
		backPane.setPadding(new Insets(0, 10, 10, 0));
		
		addLabel = new Label("Add Show");
		removeLabel = new Label("Remove Show");
		
		add = new Button("Add");
		add.setOnAction(e -> addAddItem());
		
		addShowField = new TextField();
		addShowField.setOnAction(e -> addAddItem());
		
		proposedUpdatesContainer = new VBox();
		proposedUpdatesScrollPane = new ScrollPane(proposedUpdatesContainer);
		proposedUpdatesScrollPane.setId("addRemoveScrollPane");
		proposedUpdatesScrollPane.setPrefSize(300, 150);
		
		showSelectionBox = new ComboBox<String>();
		showSelectionBox.setPromptText("SelectShow");
		showSelectionBox.setPrefWidth(220);
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
		
		cancel.setPrefWidth(100);
		execute.setPrefWidth(100);
		
		backPane.setHgap(5);
		backPane.setVgap(5);
		
		backPane.add(addLabel, 1, 4);
		backPane.add(addShowField, 2, 4, 2, 1);
		backPane.add(add, 5, 4);
		
		backPane.add(removeLabel, 1, 6);
		backPane.add(showSelectionBox, 2, 6, 2, 1);
		
		backPane.add(cancel, 2, 9);
		backPane.add(execute, 3, 9);
		
		backPane.add(proposedUpdatesScrollPane, 8, 1, 10, 9);
		
		Scene scene = new Scene(backPane);
		scene.getStylesheets().add(theme.getValue());
		stage.setScene(scene);
	}
	
	private void populateComboBox(){
		for(ShowDateListNode s : showDateList) showSelectionBox.getItems().add(s.getShow());
	}
	
	public void addAddItem(){
		String show = addShowField.getText();
		if(show != null && show.length() > 0){
			proposedUpdatesContainer.getChildren().add(createAddItem(show));
			addShowField.setText("");
		}
	}
	
	public void addDeleteItem(String show){
		if(!deleteShows.contains(show)){
			proposedUpdatesContainer.getChildren().add(createDeleteItem(show));
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
		proposedUpdatesContainer.getChildren().clear();
	}
	
	public void show(ShowDateList showDateList){
		if(!active){
			active = true;
			reset();
			AddRemoveDialog.showDateList = showDateList;
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
