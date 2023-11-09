package business;

import java.io.IOException;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * Repraesentation eines MP3s mit Metadaten.
 * 
 * @author berdux
 *
 */
public class Track {
	private String title;
	private String artist;
	private String fileName;
	private double laenge;
	Mp3File mp3file = null;
	ID3v2 meta;
	
	/**
	 * Initialisierungskonstruktor
	 * 
	 * @param title Titel des Songs
	 * @param artist Interpret des Songs
	 * @param fileName Datei-Name der MP3-Datei
	 * 
	 */
	
	public Track(String path) {
		try {
			mp3file = new Mp3File(path);
			meta = mp3file.getId3v2Tag();
			
			this.fileName = path;
			this.title = meta.getTitle();
			this.artist = meta.getArtist();
			this.laenge = meta.getDataLength();

			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Track(String title, String artist, String fileName) {
		super();
		this.title = title;
		this.artist = artist;
		this.fileName = fileName;
	}
	
	/*public Track(String title, String artist) {
		super();
		this.title = title;
		this.artist = artist;
		this.fileName = null;
	}*/

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getFileName() {
		return fileName;
	}
	
	public String toString() {
		return title + " - " + artist;
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return fileName;
	}
	
	public double getLaenge() {
		return laenge;
	}

}

