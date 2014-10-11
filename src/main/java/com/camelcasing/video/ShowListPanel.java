package com.camelcasing.video;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class ShowListPanel{

		private ArrayList<String> shows;
		private GridPane pane;
		
	public ShowListPanel(){
		createShowList();
		pane = new GridPane();
		pane.setPadding(new Insets(20, 0, 0, 20));
		
		for(int i = 0; i < shows.size(); i++){
			Text show = new Text(shows.get(i));
			show.setId("showText");
			GridPane.setMargin(show, new Insets(0, 0, 20, 0));
			pane.add(show, 0, i);
		}
	}
	
	public GridPane getShowListPane(){
		return pane;
	}
	
	private void createShowList(){
		shows = new ArrayList<String>();
		shows.add("Reign");
		shows.add("Arrow");
		shows.add("BigBangTheory");
		shows.add("Elementary");
		shows.add("WalkingDead");
		shows.add("GameOfThrones");
		shows.add("Scorpion");
		shows.add("OrphanBlack");
		shows.add("OnceUponaTime");
		shows.add("AgentsofSHIELD");
		shows.add("Legends");
		shows.add("Defiance");
		shows.add("Homeland");
		shows.add("Gotham");
		shows.add("Hannibal");
		shows.add("Flash_2014");
	}
	
	public void setShowList(ArrayList<String> shows){
		this.shows = shows;
	}
	
	public ArrayList<String> getShowList(){
		return shows;
	}
}
