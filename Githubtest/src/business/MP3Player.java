package business;

import java.util.Collections;
import java.util.Timer;

import business.Playlist;
import business.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import de.hsrm.mi.prog.util.StaticScanner;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MP3Player {

	Playlist playlist;
	public Track aktuellerSong;
	private SimpleMinim minim;
	SimpleAudioPlayer audioPlayer;
	public boolean playing;
	private boolean skipped = false;
	private SimpleIntegerProperty currentTime;
	private SimpleIntegerProperty endtime;
	private SimpleObjectProperty<Track> tracks;
	private SimpleBooleanProperty isShuffling;
	private SimpleBooleanProperty isPlaying;
	private int aktuell;
	private Thread playThread;
	private int storedPlaybackPosition;
	private float savedVolume;

	private Thread timeThread;

	public MP3Player() {

		this.playlist = new Playlist();

		this.minim = new SimpleMinim();
		aktuellerSong = playlist.get();
		tracks = new SimpleObjectProperty<>();
		currentTime = new SimpleIntegerProperty(0);
		endtime = new SimpleIntegerProperty(aktuellerSong.getDuration());
		isShuffling = new SimpleBooleanProperty();
		isPlaying = new SimpleBooleanProperty(false);
		
	}

	public void play() {
		tracks.set(aktuellerSong);
		audioPlayer = minim.loadMP3File(aktuellerSong.getPath());
		

		if (audioPlayer != null) {
			if (storedPlaybackPosition > 0) {
				audioPlayer.cue(storedPlaybackPosition);
			} else {
				audioPlayer = minim.loadMP3File(aktuellerSong.getPath());
				currentTime.setValue(0);

			}

			
		}
		
		timeThread = new Thread(() -> {
			while(playing) {
				currentTime.setValue(audioPlayer.position());
				endtime.setValue(aktuellerSong.getDuration());
			}
		});
		
		
		
		
		playThread = new Thread(() -> {
				playing = true;
				isPlaying.set(true);
				timeThread.start();
				audioPlayer.play();
				
				
				Platform.runLater(new Runnable() {
					public void run() {
						if(isSongEnd()) {
							skip();
						}
					}
			});
				
		
		});
		setIsShuffling(false);
		playThread.start();
		
		savedVolume = audioPlayer.getGain();
		audioPlayer.setGain(savedVolume);
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

	/*-----------------------------------------*/

	public boolean isSongEnd() {
//		System.out.println(currentTime.get());
//		System.out.println(aktuellerSong.getDuration());
		return currentTime.get() >= aktuellerSong.getDuration()-500;
	}

	/*-----------------------------------------*/

	public void setStoredPlaybackPosition(int storedPlaybackPosition) {
		this.storedPlaybackPosition = storedPlaybackPosition;
	}

	public void setSong(Track neuerSong) {
		aktuellerSong = neuerSong;
		updateEndTime();
	}

	public void pause() {
		if (audioPlayer != null && audioPlayer.isPlaying()) {
			storedPlaybackPosition = currentTime.get();
			audioPlayer.pause();
			playing = false;
		} else {
			System.err.println("Sie haben keinen Song zum Pausieren oder der Song ist bereits pausiert.");
		}
	}

	public void resume() {
		if (audioPlayer != null && !audioPlayer.isPlaying()) {
			audioPlayer.cue(storedPlaybackPosition);
			audioPlayer.play();
			playing = true;
		} else {
			System.err.println("Sie haben keinen pausierten Song zum Fortsetzen oder der Song wird bereits abgespielt.");
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
		Collections.shuffle(playlist.tracklist);
		setIsShuffling(true);
		aktuellerSong = playlist.get((int) (playlist.tracklist.size() * Math.random()));
		setAktuell(playlist.getIndex(aktuellerSong));
		setStoredPlaybackPosition(0);
		play();
		return aktuellerSong;
	}

	public synchronized Track skip() {
		pause();
		skipped = true;
		setStoredPlaybackPosition(0);
		aktuellerSong = playlist.get(++aktuell % playlist.tracklist.size());
		System.out.println(aktuellerSong.getTitle());
		
		play();
		audioPlayer.setGain(savedVolume);
		
		skipped = false;
		return aktuellerSong;
	}

	public synchronized Track skipBack() {
		pause();
		skipped = true;
		setStoredPlaybackPosition(0);
		if (aktuell == 0) {
			aktuell = playlist.tracklist.size() - 1;
		} else if (aktuell > 0) {
			aktuell--;
		}
		
		aktuellerSong = playlist.get(aktuell);
		
		play();
		
		audioPlayer.setGain(savedVolume);
		
		skipped = false;

		return aktuellerSong;

	}

	public void volume(float d) {
		audioPlayer.setGain((float) (10 * Math.log10(d)));
//		System.out.printf("volume: %1.2f %n", d);
	}

	public SimpleIntegerProperty currentTimeProperty() {
		return currentTime;
	}

	public int getCurrentTime() {
		return currentTime.get();
	}
	
	/*--------------------------------------*/
	
	 public SimpleBooleanProperty isPlayingProperty() {
	        return isPlaying;
	    }

	/*--------------------------------------*/


	public SimpleIntegerProperty endTimeProperty() {
		return endtime;
	}

	public void updateEndTime() {
		endtime.set(aktuellerSong.getDuration());
	}

	public static String formatTime(int milliseconds) {
		int seconds = milliseconds / 1000;
		int hours = seconds / 3600;
		int minutes = (seconds - (3600 * hours)) / 60;
		int remainingSeconds = seconds - ((hours * 3600) + (minutes * 60));

		return String.format("%01d:%02d:%02d", hours, minutes, remainingSeconds);

	}

	public int getAktuell() {
		return aktuell;
	}

	public void setAktuell(int aktuell) {
		this.aktuell = aktuell;
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

}
