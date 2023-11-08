package business;

import business.Playlist;
import business.Track;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import de.hsrm.mi.prog.util.StaticScanner;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MP3Player {
	
	Playlist playlist;
	public Track aktuellerSong;
	private SimpleMinim minim;
	SimpleAudioPlayer audioPlayer;
	public boolean playing;
	private SimpleDoubleProperty currentTime;
	private double endTime;

	
	public MP3Player() {

		this.playlist = new Playlist();

		this.minim = new SimpleMinim(true);
		aktuellerSong = playlist.get();
		currentTime = new SimpleDoubleProperty();
		endTime = aktuellerSong.getLaenge();

	}

	public void play() {
	System.out.println("play");
		
		new Thread() {
			public void run() {
				currentTime.setValue(0);
				audioPlayer = minim.loadMP3File(aktuellerSong.getPath());
				audioPlayer.play();
				playing = true;
		
				while(playing = true) {
					System.out.println(currentTime.get());
					currentTime.setValue(currentTime.getValue() + 1.0);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
					if(playing = false) {
						break;
					}
				}
				
				
			}
		}.start();
		
	}
	
	public void setSong(Track neuerSong) {
		aktuellerSong = neuerSong;
	}

	public void pause() {
		
		audioPlayer.pause();
		
		playing = false;
		
		System.out.println("pause");
	}
	
	public void resume(){
		if (audioPlayer.isPlaying()) {
		  } else {
		    audioPlayer.play();
		    System.out.println("resume");
		  }
	}

	public void skip() {
		pause();
		aktuellerSong = (Track) playlist.getNext();
		play();
		//System.out.println("skip");
	}

	public void skipBack() {
		pause();
		aktuellerSong = (Track) playlist.getPrev();
		
		play();
		//System.out.println("skipBack");
	}

	public void volume(float d) {
		audioPlayer.setGain((float) (10*Math.log10(d)));
		System.out.printf("volume: %1.2f %n", d);
	}

	public SimpleDoubleProperty currentTimeProperty() {
		return currentTime;
	}
	
	public double getCurrentTime() {
		return  currentTime.get();
	}
	
	public double getEndTime() {
		return endTime;
	}
}
