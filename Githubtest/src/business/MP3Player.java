package business;

import java.util.Collections;

import business.Playlist;
import business.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import de.hsrm.mi.prog.util.StaticScanner;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MP3Player {

	Playlist playlist;
	public Track aktuellerSong;
	private SimpleMinim minim;
	SimpleAudioPlayer audioPlayer;
	public boolean playing;
	private SimpleIntegerProperty currentTime;
	private SimpleIntegerProperty endtime;
	private SimpleObjectProperty<Track> tracks;
	private int aktuell;
	private Thread playThread;
	private SimpleIntegerProperty trackIndex;

	public MP3Player() {

		this.playlist = new Playlist();

		this.minim = new SimpleMinim(true);
		aktuellerSong = playlist.get();
		tracks = new SimpleObjectProperty<>();
		currentTime = new SimpleIntegerProperty();
		endtime = new SimpleIntegerProperty(aktuellerSong.getDuration());

	}

	public void play() {
		System.out.println("play");
		tracks.set(aktuellerSong);

		playThread  = new Thread() {
			public void run() {
				currentTime.setValue(0);
				audioPlayer = minim.loadMP3File(aktuellerSong.getPath());
				audioPlayer.play();
				playing = true;

				while (playing = true) {
					// System.out.println(currentTime.get());
					currentTime.setValue(currentTime.getValue() + 1000);
					endtime.setValue(aktuellerSong.getDuration());

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					if (playing = false) {
						break;
					}
				}
			}
		};
		playThread.start();
		System.out.println(aktuellerSong.getLaenge());

	}

	public void setSong(Track neuerSong) {
		aktuellerSong = neuerSong;
		updateEndTime();
	}

	public void pause() {
		try {
		audioPlayer.pause();

		playing = false;

		System.out.println("pause");

		}catch (NullPointerException e) {
			System.err.println("Sie haben kein Song zum pausieren");
		}
	}
	public void resume() {
		if (audioPlayer.isPlaying()) {
		} else {
			audioPlayer.play();
			System.out.println("resume");
		}
	}

	public void loop() {
		audioPlayer.loop();
	}

	public Track shuffle() {

		Collections.shuffle(playlist.tracklist);

		return playlist.get((int) (playlist.tracklist.size() * Math.random()));
	}

	public Track skip() {
		pause();

		aktuellerSong = playlist.get(++aktuell % playlist.tracklist.size());
		play();
		return aktuellerSong;
	}

	public Track skipBack() {
		pause();

		if (aktuell == 0) {
			aktuell = playlist.tracklist.size() - 1;
		} else if (aktuell > 0) {
			aktuell--;
		}
		aktuellerSong = playlist.get(aktuell);
		play();

		return aktuellerSong;

	}

	public void volume(float d) {
		audioPlayer.setGain((float) (10 * Math.log10(d)));
		System.out.printf("volume: %1.2f %n", d);
	}

	public SimpleIntegerProperty currentTimeProperty() {
		return currentTime;
	}

	public int getCurrentTime() {
		return currentTime.get();
	}

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
	
	

	public int getTrackIndex(Track track) {
		for (int i = 0; i < playlist.tracklist.size(); i++) {
			if (playlist.tracklist.get(i).getTitle() == track.getTitle()) {
				trackIndex.setValue(i);
			}
		}
		return trackIndex.getValue();
	}

	public void setTrackIndex(int trackNumber) {
		this.trackIndex.set(trackNumber);
	}
	
	public Playlist getPlaylist() {
		return playlist;
	}
}
