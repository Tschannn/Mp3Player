package scenes;

import application.Main;
import business.MP3Player;
import business.Track;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PlayerViewController {
	private Main application;
	private Pane root;
	
	Label albumLabel;
	Label titleLabel;
	Label timeLabel;
	Label endTimeLabel;
	Slider slider;
	Slider timeSlider;
	Button playButton;
	Button switchButton;
	Button pauseButton;
	Button fowardButton;
	Button backwardButton;
	
	MP3Player player;
	Track akt;
	
	/* Listener als Memebr-Klasse, um den Listener eventuell auch 
	 * mehrmals erzeugen zu koennen (macht hier dann aber bei dem
	 * Beispiel keine eigentlichen Sinn, da der Listener ja dann wirklich 
	 * nur fuer den einen Button gedacht ist.
	 * 
	 * Zusaetzlich waere hier dann ein Zusatnd vorgesehen, um zwischen
	 * Play und Pause hin und her zu wechseln. Nachtuerlich wurde man hier
	 * dann auch das Icon des Button wechseln, am besten ueber eas Setzen eines
	 * Style-Attributs.
	 */
	private class PlayEventHandler implements EventHandler<ActionEvent>{

		boolean isPlaying;
		
		@Override
		public void handle(ActionEvent event) {
			if (!isPlaying) {
				isPlaying = !isPlaying;
				player.play();
			} else {
				isPlaying = !isPlaying;
				player.pause();
			}
		}

	}
	
	public PlayerViewController(Main application, MP3Player player) {
		this.application = application;
		this.player = player;
		
		// initialisiere (lade) View
		PlayerView mainView = new PlayerView();
		// ... und hole die Referenzen auf einzelne Elemente,
		// um diese dann zu initialisieren bzw. Listener amelden zu koennen
		albumLabel = mainView.albumLabel;
		titleLabel = mainView.titleLabel;
		playButton = mainView.playButton;
		switchButton = mainView.switchButton;
		pauseButton = mainView.pauseButton;
		fowardButton = mainView.fowardButton;
		backwardButton = mainView.backwardButton;
		slider = mainView.slider;
		timeLabel = mainView.timeLabel;
		endTimeLabel = mainView.endTimeLabel;
		timeSlider = mainView.timeSlider;

		akt = player.aktuellerSong;
		
		
		
		
		// Root-View zu haben ist immer eine gute Idee
		root = mainView;

		// nachdem die View in itialisiert ist, wird dann bei JavaFX
		// standardmaessig die initialize-Methode aufgerufen, in der dann
		// das Befuellen der GUI-Elemente und das Anmelden der Listener erfolgt
		initialize();
		
	}
	
	/**
	 * Standard-Methode von JavaFX, die nach der Initialisierung 
	 * ueber den FXML-Loader aufgerufen wird.
	 * 
	 * Hier jetzt nachgeahmt, um einen parallelen Aufbau zu dem 
	 * Controller mit FXML-View aufzuzeigen.
	 */
	public void initialize() {
		
		/* Beispiel wie dann ein Listener als Member-Klasse bei einem 
		 * Button angemeldet wird
		 */
		//playButton.addEventHandler(ActionEvent.ACTION,  new PlayEventHandler());
		
		/* Beispiel eine Listneres als anonyme Klasse mit 
		 * eigenem Zustand isPlaying.
		 */
		
		
		playButton.addEventHandler(ActionEvent.ACTION,
			event ->{

				
			
				if(!player.playing) {
					player.play();
//					Platform.runLater(() -> {
//						endTimeLabel.setText(akt.getLaenge());
//						endTimeLabel.valueProperty().set(akt.doubleValue());
//					});
				}else {
					player.pause();
				
				}
				
//				System.out.println(titleLabel.toString());
			});
		
		pauseButton.addEventFilter(ActionEvent.ACTION,
				event -> {
					player.resume();
				}
				
				);
		
		fowardButton.addEventFilter(ActionEvent.ACTION,
				event -> {
					player.skip();
				}
				
				);
		
		backwardButton.addEventFilter(ActionEvent.ACTION,
				event -> {
					player.skipBack();
				}
				
				);
		
		switchButton.addEventHandler(ActionEvent.ACTION,
				event -> {
					application.switchScene("PlaylistView");
				}
		);
		
		 slider.valueProperty().addListener((observable, oldValue, newValue) -> {
	            float volume = newValue.floatValue();
	            player.volume(volume);
	            System.out.println(volume);
	           // System.out.println("Angelos Angelakis");
	        });
		
	
	timeSlider.valueProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			player.currentTimeProperty();
			
		}
		
	});
	
	
	
	player.currentTimeProperty().addListener(new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			Platform.runLater(() -> {
				timeLabel.setText(newValue.toString());
				timeSlider.valueProperty().set(newValue.doubleValue());
			});
			
		}
		
	});
	}
	public Pane getRoot() {
		return root;
	}

}
