package scenes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import business.MP3Player;
import business.Playlist;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.stage.FileChooser;

public class PlaylistViewController {
	Main application;
	Pane root;
	
	FileChooser files;
	
	ListView<Track> playlistView;
	Button zuruckButton;
	Button filechooser;
	
	MP3Player player;
	Playlist playlist;
	
	public PlaylistViewController(Main application,MP3Player player,Playlist playlist) {
		this.player = player;
		this.application = application;
		this.playlist = playlist;
		System.out.println();
		
		files = new FileChooser();
		files.setInitialDirectory(new File("c.\\"));
		
		PlaylistView view = new PlaylistView();
		playlistView = view.playlistView;
		zuruckButton = view.zuruckButton;
		filechooser = view.filechooser;
		
		root = view;
		
		initialize();
	}
	
	public void initialize() {
		// Dummy-Daten, die eigentlich vom Playlistmanegr kommen
		ArrayList<Track> trackList = new ArrayList<Track>();
		trackList= playlist.tracklist;		
		
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
				
			
				player.setSong(newTrack);
				
				
				if(player.playing) {
					player.pause();
					System.out.println(newTrack.getArtist());
					System.out.println(newTrack.getTitle());
					
					player.play();
				}else{
					player.play();
				}
				
			}
			
		});
		
		
		// setzen des Darzustellenden Inhalts
		ObservableList<Track> playlistModel = playlistView.getItems();
		playlistModel.addAll(trackList);
		
		
		playlistView.setId("table");
		
		zuruckButton.addEventHandler(ActionEvent.ACTION,
				event -> {
					application.switchScene("PlayerView");
				}
		);

		
	/*	// nur als Demo, um zu zeigen was passiert, wenn Zellen nicht jedesmal vollstaendig initialisiert werden 
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
			
			//deleteThread.start();*/
		
		filechooser.addEventHandler(ActionEvent.ACTION,
				event -> {
//					File newfile = files.showOpenDialog();
//					
//					if(newfile != null) {
//						System.out.println(newfile.getAbsolutePath());
//					}
					System.out.println("Hallo Palestine");
				});
		
		
	}

	public Pane getRoot() {
		return root;
	}

}
	
