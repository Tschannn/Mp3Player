package scenes;

import java.io.ByteArrayInputStream;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class PlayerViewController {
	private Main application;
	private Pane root;

	Label albumLabel;
	Label titleLabel;
	Label timeLabel;
	Label endTimeLabel;
	Slider volSlider;
	Slider timeSlider;
	Button playButton;
	Button switchButton;
	Button pauseButton;
	Button fowardButton;
	Button backwardButton;
	Button loopButton;
	Button shuffleButton;
	
	ImageView coverView;

	MP3Player player;
	Track akt;

	/*
	 * Listener als Memebr-Klasse, um den Listener eventuell auch mehrmals erzeugen
	 * zu koennen (macht hier dann aber bei dem Beispiel keine eigentlichen Sinn, da
	 * der Listener ja dann wirklich nur fuer den einen Button gedacht ist.
	 * 
	 * Zusaetzlich waere hier dann ein Zusatnd vorgesehen, um zwischen Play und
	 * Pause hin und her zu wechseln. Nachtuerlich wurde man hier dann auch das Icon
	 * des Button wechseln, am besten ueber eas Setzen eines Style-Attributs.
	 
	private class PlayEventHandler implements EventHandler<ActionEvent> {

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
*/
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
		loopButton = mainView.loopButton;
		shuffleButton = mainView.shuffleButton;
		volSlider = mainView.volSlider;
		timeLabel = mainView.timeLabel;
		endTimeLabel = mainView.endTimeLabel;
		timeSlider = mainView.timeSlider;
		coverView = mainView.coverView;

		akt = player.aktuellerSong;

		// Root-View zu haben ist immer eine gute Idee
		root = mainView;

		// nachdem die View in itialisiert ist, wird dann bei JavaFX
		// standardmaessig die initialize-Methode aufgerufen, in der dann
		// das Befuellen der GUI-Elemente und das Anmelden der Listener erfolgt
		initialize();

	}

	public void initialize() {
		
		timeLabel.setText(MP3Player.formatTime(0));
        endTimeLabel.setText("-" + MP3Player.formatTime(player.aktuellerSong.getDuration()));
        
        setImage();
        setSongInfo();

		
		playButton.addEventHandler(ActionEvent.ACTION, event -> {

			if (!player.playing) {
				player.play();
//					Platform.runLater(() -> {
//						endTimeLabel.setText(akt.getLaenge());
//						endTimeLabel.valueProperty().set(akt.doubleValue());
//					});
				
			} else {
				player.resume();

			}

		});

		pauseButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.pause();
		}

		);

		fowardButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.skip();
		}

		);

		backwardButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.skipBack();
		}

		);

		loopButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.loop();
		}

		);

		shuffleButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.shuffle();
		}

		);

		switchButton.addEventHandler(ActionEvent.ACTION, event -> {
			application.switchScene("PlaylistView");
		});

		
		volSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> oV, Number oldValue, Number newValue) {
				float volume = newValue.floatValue();
				player.volume(volume);
//				System.out.println("Angelos Angelakis");
			}
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
                    timeLabel.setText(MP3Player.formatTime(newValue.intValue()));
                    Duration currentTime = Duration.millis(newValue.intValue());
                    timeSlider.setValue(currentTime.toSeconds());
                    String sekunden = MP3Player.formatTime((int) (player.aktuellerSong.getDuration() - newValue.intValue()));
                    endTimeLabel.setText(sekunden);
                    
                });

            }

        });
	
		player.endTimeProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                endTimeLabel.setText("-" + MP3Player.formatTime(newValue.intValue()));
            });
        });


        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                player.currentTimeProperty();

            }

        });
}
	

	
	public void setImage() {
        try {
            coverView.setImage(new Image (new ByteArrayInputStream(player.aktuellerSong.getAlbumImage()))); 
        } catch (NullPointerException e){
            e.printStackTrace();

        }
    }
    public void setSongInfo() {
        titleLabel.setText(player.aktuellerSong.getTitle());
        albumLabel.setText(player.aktuellerSong.getArtist()); 
    }

	public Pane getRoot() {
		return root;
	}

}
