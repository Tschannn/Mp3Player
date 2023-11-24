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

		root = mainView;

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

			} else {
				player.resume();

			}
			setSongInfo();
			setImage();
		});

		pauseButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.pause();
		}

		);

		fowardButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.skip();
			setSongInfo();
			setImage();
		});

		backwardButton.addEventFilter(ActionEvent.ACTION, event -> {
			player.skipBack();
			setSongInfo();
			setImage();
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
				double value = newValue.floatValue();

			}

		});
		
		player.isPlayingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) { // If the song has ended
                Platform.runLater(() -> {
                    setSongInfo();
                    setImage();
                });
            }
        });

		player.currentTimeProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(() -> {
					timeLabel.setText(MP3Player.formatTime(newValue.intValue()));
					timeSlider.setValue((double) newValue.intValue() * 100 / player.aktuellerSong.getDuration());
//		            System.out.println("timeslider thing"+ newValue.intValue()/player.aktuellerSong.getDuration());
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

		timeSlider.setOnMouseDragged(event -> {
			double sliderValue = timeSlider.getValue();
			int currentTimeMillis = (int) (sliderValue * player.aktuellerSong.getDuration());
			System.out.println(currentTimeMillis);
			timeLabel.setText(MP3Player.formatTime(currentTimeMillis));

			int remainingMilis = player.aktuellerSong.getDuration() - currentTimeMillis;
			endTimeLabel.setText("-" + MP3Player.formatTime(remainingMilis));
		});

		player.isShufflingProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean neuValue) {
				Platform.runLater(() -> {
					setSongInfo();
					coverView.setImage(new Image(new ByteArrayInputStream(player.aktuellerSong.getAlbumImage())));
				});
			}
		});
	}

	public void setImage() {
		try {
			coverView.setImage(new Image(new ByteArrayInputStream(player.aktuellerSong.getAlbumImage())));
		} catch (NullPointerException e) {
			System.err.println("Dieses Lied hat kein Bild");

		}
	}

	public void setSongInfo() {
		titleLabel.setText(player.aktuellerSong.getTitle());
		albumLabel.setText(player.aktuellerSong.getArtist());
	}

	public void setSongInfo(Track song) {
		player.setAktuell(player.getPlaylist().getIndex(song));
		setImage();
		titleLabel.setText(song.getTitle());
		albumLabel.setText(song.getArtist());
	}

	public Pane getRoot() {
		return root;
	}
}
