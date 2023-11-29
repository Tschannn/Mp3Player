package scenes;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import application.Main;
import business.MP3Player;
import business.Track;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


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
	Track currentSong;

	Button playPauseButton;
	Image playImage, pauseImage;
	ImageView pauseimg, playimg;

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
		fowardButton = mainView.fowardButton;
		backwardButton = mainView.backwardButton;
		loopButton = mainView.loopButton;
		shuffleButton = mainView.shuffleButton;
		volSlider = mainView.volSlider;
		timeLabel = mainView.timeLabel;
		endTimeLabel = mainView.endTimeLabel;
		timeSlider = mainView.timeSlider;
		coverView = mainView.coverView;
		pauseimg = mainView.pausebutton1;
		playimg = mainView.playbutton1;
		playPauseButton = mainView.playPauseButton;

		playImage = mainView.playImage;
		pauseImage = mainView.pauseImage;
		currentSong = player.currentSong;

		root = mainView;

		initialize();

	}

	public void initialize() {

		timeLabel.setText(MP3Player.formatTime(0));
		endTimeLabel.setText("-" + MP3Player.formatTime(player.currentSong.getDuration()));

		setImage();
		setSongInfo();

		playPauseButton.addEventHandler(ActionEvent.ACTION, event -> {

			if (!player.playing) {
				player.play();

			} else {
				player.pause();

			}
			updatePlayPauseButton();
			setSongInfo();
			setImage();
		});

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
					try {
						timeSlider.setValue((double) newValue.intValue() * 100 / player.currentSong.getDuration());
						String sekunden = MP3Player
								.formatTime((int) (player.currentSong.getDuration() - newValue.intValue()));
						endTimeLabel.setText(sekunden);
					} catch (NullPointerException e) {
						System.out.println("");
					}

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

		player.isShufflingProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean neuValue) {
				Platform.runLater(() -> {
					setSongInfo();
					coverView.setImage(new Image(new ByteArrayInputStream(player.currentSong.getAlbumImage())));
				});
			}
		});

		timeSlider.setOnMousePressed(event -> { /* so lange slider gedrückt, pausieren */
			player.pause();
		});

		timeSlider.setOnMouseDragged(event -> {
			double sliderValue = timeSlider.getValue();
			int currentTimeMillis = (int) (sliderValue * player.currentSong.getDuration());
			timeLabel.setText(MP3Player.formatTime(currentTimeMillis / 100));

			int remainingMilis = player.currentSong.getDuration() - currentTimeMillis / 100;
			endTimeLabel.setText("-" + MP3Player.formatTime(remainingMilis));

			player.setStoredPlaybackPosition(currentTimeMillis / 100);
		});
		timeSlider.setOnMouseReleased(event -> { /* wenn slider losgelassen, dasnn wieder abspielen */
			player.play();
		});
	}

	private void updatePlayPauseButton() {
		Platform.runLater(() -> {

			if (player.playing) {
				playPauseButton.setGraphic(pauseimg);
			} else {
				playPauseButton.setGraphic(playimg);

			}
		});
	}

	public void setImage() {
		try {
			coverView.setImage(new Image(new ByteArrayInputStream(player.currentSong.getAlbumImage())));
		} catch (NullPointerException e) {
			try {
				coverView.setImage(new Image(new FileInputStream("assets/spotify.jpg")));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public void setSongInfo() {
		titleLabel.setText(player.currentSong.getTitle());
		albumLabel.setText(player.currentSong.getArtist());
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
