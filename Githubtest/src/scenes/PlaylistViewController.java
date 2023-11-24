package scenes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.stage.FileChooser;

public class PlaylistViewController {
	Main application;
	Pane root;
	
	PlayerView mainview;
	PlayerViewController playerviewcontroller; 
	ListView<Track> playlistView;
	Button zuruckButton;
	Button filechooser;
	Label albumLabel;
	Label titleLabel;
	
	MP3Player player;
	Playlist playlist;

	public PlaylistViewController(Main application, MP3Player player, Playlist playlist, PlayerViewController playerviewcontroller) {
		this.player = player;
		this.application = application;
		this.playlist = playlist;
		this.playerviewcontroller=playerviewcontroller; 
		System.out.println();


		PlaylistView view = new PlaylistView();
		PlayerView mainview = new PlayerView();
		
		playlistView = view.playlistView;
		zuruckButton = view.zuruckButton;
		filechooser = view.filechooser;
		albumLabel = mainview.albumLabel;
		titleLabel = mainview.titleLabel;

		root = view;

		initialize();
	}

	public void initialize() {
		ArrayList<Track> trackList = new ArrayList<Track>();
		trackList = playlist.tracklist;

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

				player.setSong(newTrack);
				playerviewcontroller.setSongInfo(newTrack);; 
				if (player.playing) {
					player.pause();
					player.setStoredPlaybackPosition(0);
					System.out.println(newTrack.getArtist());
					System.out.println(newTrack.getTitle());
					setSongInfo();
					
					player.play();
				} else {
					player.play();
					setSongInfo();
				}
			}
		});

		// setzen des Darzustellenden Inhalts
		ObservableList<Track> playlistModel = playlistView.getItems();
		playlistModel.addAll(trackList);

		playlistView.setId("table");

		zuruckButton.addEventHandler(ActionEvent.ACTION, event -> {
			application.switchScene("PlayerView");
		});
		
		
//------------------------------------------------------------------------------
		filechooser.addEventHandler(ActionEvent.ACTION, event -> {

			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setCurrentDirectory(new File(".//Songs//"));
			fileChooser.setFileFilter(new FileNameExtensionFilter(".mp3", "mp3"));
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int response = fileChooser.showSaveDialog(null); // select file to save

			if (response == JFileChooser.APPROVE_OPTION) {
				System.out.println(playlist.tracklist);
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				System.out.println(file);
				playlist.tracklist.add(new Track(file.getAbsolutePath()));
				System.out.println(playlist.tracklist);
				changedSong();
			}

			// System.out.println("Hallo Palestine");
		});

	}

	public void changedSong() {
		ObservableList<Track> playlistModel = playlistView.getItems();
		playlistModel.setAll(playlist.tracklist);
		
	    playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Track>() {
	        @Override
	        public void changed(ObservableValue<? extends Track> observable, Track oldTrack, Track newTrack) {
	            playerviewcontroller.setSongInfo(newTrack);
	        }
	    });
	}

	

	public Pane getRoot() {
		return root;
	}
	
	 public void setSongInfo() {
	        titleLabel.setText(player.aktuellerSong.getTitle());
	        albumLabel.setText(player.aktuellerSong.getArtist()); 
	    }
	 
	 
	 
}	
