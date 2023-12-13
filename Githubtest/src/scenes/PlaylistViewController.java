package scenes;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import application.Main;
import business.MP3Player;
import business.Playlist;
import business.Track;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class PlaylistViewController {
	Main application;
	Pane root;
	
	PlayerView mainview;
	PlayerViewController playerviewcontroller; 
	ListView<Track> playlistView;
	Button backButton;
	Button filechooser;
	Label albumLabel;
	Label titleLabel;
	
	MP3Player player;
	Playlist playlist;

	public PlaylistViewController(Main application, MP3Player player) {
		this.player = player;
		this.application = application;
		this.playlist = player.getPlaylist();


		PlaylistView view = new PlaylistView();
		PlayerView mainview = new PlayerView();
		
		playlistView = view.playlistView;
		backButton = view.backButton;
		filechooser = view.filechooser;
		albumLabel = mainview.albumLabel;
		titleLabel = mainview.titleLabel;

		root = view;

		initialize();
	}

	public void initialize() {
		ArrayList<Track> trackList = new ArrayList<Track>();
		trackList = playlist.tracklist; //gleich setzung zur eigentlichen Playlist

		// angeben, wie eine Zelle aufgebaut wird
		playlistView.setCellFactory(new Callback<ListView<Track>, ListCell<Track>>() {
			@Override
			public ListCell<Track> call(ListView<Track> param) {
				return new TrackCell();
			}
		});

		// erkennen von selektiertem Track und ändern des Liedes
		playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Track>() {
			
			@Override
			public void changed(ObservableValue<? extends Track> observable, Track oldTrack, Track newTrack) {
				try {
				player.setSong(newTrack);
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
			}catch(NullPointerException e){
				System.out.println("");
			}}
		});

		// setzen des Darzustellenden Inhalts
		ObservableList<Track> playlistModel = playlistView.getItems();
		playlistModel.addAll(trackList);

		playlistView.setId("table");

		backButton.addEventHandler(ActionEvent.ACTION, event -> {
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
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				playlist.tracklist.add(new Track(file.getAbsolutePath()));
//				System.out.println(playlist.tracklist);
				changedSong();
			}

			// System.out.println("Hallo Palestine");
		});

	}

	public void changedSong() {
		ObservableList<Track> playlistModel = playlistView.getItems();
		playlistModel.setAll(playlist.tracklist);
		
	   /* playlistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Track>() {
	        @Override
	        public void changed(ObservableValue<? extends Track> observable, Track oldTrack, Track newTrack) {
	            try {
	        	playerviewcontroller.setSongInfo(newTrack);
	            }catch(NullPointerException e) {
	            	System.out.println("");
	            }
	            }
	    });*/
	}

	

	public Pane getRoot() {
		return root;
	}
	
	 public void setSongInfo() {
	        titleLabel.setText(player.currentSong.getTitle());
	        albumLabel.setText(player.currentSong.getArtist()); 
	    }
	 
	 
	 
}	
