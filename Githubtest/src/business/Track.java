package business;

import java.io.IOException;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * Repraesentation eines MP3s mit Metadaten.
 *
 */
public class Track {
	private String title;
	private String artist;
	private String fileName;
	private int duration;
	Mp3File mp3file = null;
	ID3v2 meta;
	byte[] albumImage;

	public Track(String path) {
		try {
			mp3file = new Mp3File(path);
			meta = mp3file.getId3v2Tag();

			this.fileName = path;
			this.title = meta.getTitle();
			this.artist = meta.getArtist();
			this.albumImage = meta.getAlbumImage();
			this.duration = (int) mp3file.getLengthInSeconds() * 1000;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}

	public Track(String title, String artist, String fileName) {
		super();
		this.title = title;
		this.artist = artist;
		this.fileName = fileName;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public byte[] getAlbumImage() {

		return albumImage;
	}

	public void setAlbumImage(byte[] albumImage) throws InvalidDataException {
		this.albumImage = albumImage;
	}

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
		return fileName;
	}

}
