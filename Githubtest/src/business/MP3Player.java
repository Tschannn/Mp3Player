package business;

import java.util.Collections;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MP3Player {

	Playlist playlist;
	public Track currentSong;
	private SimpleMinim minim;
	public SimpleAudioPlayer audioPlayer;
	public boolean playing;
	private SimpleIntegerProperty currentTime, endtime;
	private SimpleObjectProperty<Track> track;
	private SimpleBooleanProperty isShuffling, isPlaying;
	private int currentSongIndex, storedPlaybackPosition;
	private float savedVolume;
	private Thread playThread, timeThread;

	public MP3Player() {

		this.playlist = new Playlist();

		this.minim = new SimpleMinim();
		currentSong = playlist.get();
		track = new SimpleObjectProperty<>();
		currentTime = new SimpleIntegerProperty(0);
		endtime = new SimpleIntegerProperty(currentSong.getDuration());
		isShuffling = new SimpleBooleanProperty();
		isPlaying = new SimpleBooleanProperty(false);

	}

	public void play() {
		track.set(currentSong);
		audioPlayer = minim.loadMP3File(currentSong.getPath());

		if (audioPlayer != null) {
			if (storedPlaybackPosition > 0) {
				audioPlayer.cue(storedPlaybackPosition);
			} else {
				audioPlayer = minim.loadMP3File(currentSong.getPath());
				currentTime.setValue(0);

			}

		}

		timeThread = new Thread(() -> {
			while (playing) {
				try {
				currentTime.setValue(audioPlayer.position());
				endtime.setValue(currentSong.getDuration());
				}catch(NullPointerException e) {
					System.out.println("");
				}
			}
		});

		playThread = new Thread(() -> {
			playing = true;
			isPlaying.set(true);
			timeThread.start(); // continuously update the time properties
			audioPlayer.setGain(savedVolume);
			audioPlayer.play();

			
			isPlaying.set(false);
			if (isSongEnd()) {
				skip();
			}

		});
		setIsShuffling(false);
		playThread.start();
		
	}

	public SimpleBooleanProperty isShufflingProperty() {
		return isShuffling;
	}

	public void setIsShuffling(SimpleBooleanProperty isShuffling) {
		this.isShuffling = isShuffling;
	}

	public boolean isShuffling() {
		return isShuffling.get();
	}

	public int getStoredPlaybackPosition() {
		return storedPlaybackPosition;
	}

	public void setIsShuffling(boolean isShuffling) {
		this.isShuffling.set(isShuffling);
	}


	public boolean isSongEnd() {
		return currentTime.get() >= currentSong.getDuration() - 500;
	}


	public void setStoredPlaybackPosition(int storedPlaybackPosition) {
		this.storedPlaybackPosition = storedPlaybackPosition;
	}

	public void setSong(Track neuerSong) {
		currentSong = neuerSong;
		track.set(neuerSong);
		updateEndTime();
	}

	public void pause() {
		if (audioPlayer != null && audioPlayer.isPlaying()) {
			storedPlaybackPosition = currentTime.get();
			audioPlayer.pause();
			playing = false;
			isPlaying.set(false);
		}
	}

	public void loop() {
		audioPlayer.loop();
	}

	public Track shuffle() {
		if (playing) {
			audioPlayer.pause();
		} else {
			minim.stop();
		}
//		Collections.shuffle(playlist.tracklist);
		setIsShuffling(true);
		currentSong = playlist.get((int) (playlist.tracklist.size() * Math.random()));
		setAktuell(playlist.getIndex(currentSong));
		setStoredPlaybackPosition(0);
		play();
		return currentSong;
	}

	public synchronized Track skip() {
		pause();
		setStoredPlaybackPosition(0);
		if (currentSongIndex < playlist.tracklist.size() - 1) {
			currentSongIndex++;
		} else {
			currentSongIndex = 0;
		}
		currentSong = playlist.get(currentSongIndex % playlist.tracklist.size());

		play();

		return currentSong;
	}

	public synchronized Track skipBack() {
		pause();
		setStoredPlaybackPosition(0);

		if (currentSongIndex > playlist.tracklist.size() - 1) {
			currentSongIndex = 0;
		}
		if (currentSongIndex == 0) {
			currentSongIndex = playlist.tracklist.size() - 1;
		} else if (currentSongIndex > 0) {
			currentSongIndex--;
		}

		currentSong = playlist.get(currentSongIndex);

		play();

		return currentSong;

	}

	public void volume(float d) {

		if (audioPlayer != null) {
			audioPlayer.setGain((float) (10 * Math.log10(d)));
			savedVolume = audioPlayer.getGain();
		} else {
			System.err.println("Audioplayer ist noch null");
		}

	}

	public SimpleIntegerProperty currentTimeProperty() {
		return currentTime;
	}

	public int getCurrentTime() {
		return currentTime.get();
	}

	public SimpleBooleanProperty isPlayingProperty() {
		return isPlaying;
	}

	public SimpleIntegerProperty endTimeProperty() {
		return endtime;
	}

	public void updateEndTime() {
		endtime.set(currentSong.getDuration());
	}

	public static String formatTime(int milliseconds) {
		int seconds = milliseconds / 1000;
		int hours = seconds / 3600;
		int minutes = (seconds - (3600 * hours)) / 60;
		int remainingSeconds = seconds - ((hours * 3600) + (minutes * 60));

		return String.format("%01d:%02d:%02d", hours, minutes, remainingSeconds);

	}

	public int getAktuell() {
		return currentSongIndex;
	}

	public void setAktuell(int aktuell) {
		this.currentSongIndex = aktuell;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public Playlist getPlaylist() {
		return playlist;
	}

	public float getsavedVolume() {
		return savedVolume;
	}

	public SimpleObjectProperty<Track> trackProperty() {
		return track;
	}

	public void setTrack(SimpleObjectProperty<Track> track) {
		this.track = track;
	}

}
