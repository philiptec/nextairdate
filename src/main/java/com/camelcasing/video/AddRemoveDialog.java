package com.camelcasing.video;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class AddRemoveDialog implements AddRemoveController{
	
		private Logger logger = LogManager.getLogger(getClass());
	
		private static AddRemoveDialog instance;
		private Data<ShowDateListNode> showDateList;
		private AddRemoveListener addRemoveListener;
		private Stage stage;
		private Button execute, cancel, add;
		private ComboBox<String> showSelectionBox;
		private TextField addShowField;
		private ScrollPane proposedUpdatesScrollPane;
		private VBox proposedUpdatesContainer;
		private Label addLabel, removeLabel;
		private List<String> addShows, deleteShows;
		
		private boolean active;

	private AddRemoveDialog(){
		
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
		scene.getStylesheets().add("default.css");
		stage.setScene(scene);
	}
	
	private void populateComboBox(){
		for(ShowDateListNode s : showDateList) showSelectionBox.getItems().add(s.getShow());
	}
	
	public void addAddItem(){
		String show = addShowField.getText();
		if(show != null && show.length() > 0 && !addShows.contains(show)){
			proposedUpdatesContainer.getChildren().add(new AddRemoveMenuItem(show, true));
			addShowField.selectAll();
		}
	}
	
	public void addDeleteItem(String show){
		if(!deleteShows.contains(show)){
			proposedUpdatesContainer.getChildren().add(new AddRemoveMenuItem(show, false));
		}
	}
	
	public void reset(){
		addShows = new ArrayList<String>();
		deleteShows = new ArrayList<String>();
		proposedUpdatesContainer.getChildren().clear();
		showSelectionBox.getItems().clear();
		addShowField.setText("");
	}
	
//	private boolean checkShowExists(String show){
//		return true;
//	}
	
	public void show(Data<ShowDateListNode> showDateList){
		if(!active){
			active = true;
			reset();
			this.showDateList = showDateList;
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
	
	public class AddRemoveMenuItem extends HBox{
		
			private ContextMenu menu;
		
		public AddRemoveMenuItem(String show, boolean add){
			super();
			
			setPadding(new Insets(2, 5, 2, 5));
			setSpacing(10);
			
			Text text = new Text();
			text.setFont(new Font(16));
			if(add){
				addShows.add(show);
				text.setFill(Color.GREEN);
				text.setText("+");
			}else{
				deleteShows.add(show);
				text.setFill(Color.RED);
				text.setText(" -");
			}
			menu = new ContextMenu();
			MenuItem mi = new MenuItem("delete " + show);
			mi.setOnAction(e1 -> {
				proposedUpdatesContainer.getChildren().remove(this);
				if(add){
					addShows.remove(show);
				}else{
					deleteShows.remove(show);
				}
			});
			menu.getItems().add(mi);
			
			this.setOnMouseClicked(e -> {
				if(e.getButton().equals(MouseButton.SECONDARY)){
					menu.show(this, e.getScreenX(), e.getScreenY());
				}
			});
			
			getChildren().addAll(text, new Label(show));
		}
	}
}
