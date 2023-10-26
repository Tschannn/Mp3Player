package scenes;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import application.Main;
import business.MP3Player;
import business.Track;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class PlaylistViewController {
	Main application;
	Pane root;
	
	ListView<Track> playlistView;
	Button skipButton;
	Button zuruckButton;
	
	MP3Player player;
	
	public PlaylistViewController(Main application,MP3Player player) {
		this.player = player;
		this.application = application;
		
		PlaylistView view = new PlaylistView();
		playlistView = view.playlistView;
		skipButton = view.skipButton;
		zuruckButton = view.zuruckButton;
		
		root = view;
		
		initialize();
	}
	
	public void initialize() {
		// Dummy-Daten, die eigentlich vom Playlistmanegr kommen
		List<Track> trackList = new LinkedList<Track>();
		
		trackList.add(new Track("Don't", "Bryson Tiller"));
		trackList.add(new Track("What Is Love ", "Mike O'hearn"));
		trackList.add(new Track("Bring Mich Nach Hause", "Die Toten Hosen"));
		trackList.add(new Track("Drei Worte", "Die Toten Hosen"));
		
		
		
		
		// angeben, wie eine Zelle aufgebaut wird
		playlistView.setCellFactory(new Callback<ListView<Track>, ListCell<Track>>() {
			@Override
			public ListCell<Track> call(ListView<Track> param) {
				return new TrackCell();
			}
		});
		
		// erkennen von selektiertem Track
		playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Track>() {

			@Override
			public void changed(ObservableValue<? extends Track> observable, Track oldTrack, Track newTrack) {
				System.out.println(newTrack);
				
			//	player.setSong(newTrack);
				
				
				if(!player.playing) {
					player.play();
				}
				
			}
			
		});
		
		
		// setzen des Darzustellenden Inhalts
		ObservableList<Track> playlistModel = playlistView.getItems();
		playlistModel.addAll(trackList);
		
		
		// skip in der ListView
		skipButton.setOnAction(new EventHandler<ActionEvent>() {

			int index = 0;
			@Override
			public void handle(ActionEvent event) {
				if (index < playlistModel.size()) {
					playlistView.getSelectionModel().select(index);
				}
				
				index = (index + 1) % playlistModel.size();
				player.pause();
				player.skip();
			}
			
		});
		/*
		@FXML
		public void zuruckAction() {
			application.switchScene("Player");
		}*/
		
		playlistView.setId("table");
		
		zuruckButton.addEventHandler(ActionEvent.ACTION,
				event -> {
					application.switchScene("PlayerView");
				}
		);

		
		// nur als Demo, um zu zeigen was passiert, wenn Zellen nicht jedesmal vollstaendig initialisiert werden 
		// siehe Caching von Zellen
		Thread deleteThread = new Thread(() -> {
			  while(playlistModel.size() > 5) {
				  try {
					Platform.runLater(() -> playlistModel.remove(0));
					Thread.sleep(500);
				  } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				  }
			  }
			});
			
			//deleteThread.start();
		
	}

	public Pane getRoot() {
		return root;
	}

}
	
