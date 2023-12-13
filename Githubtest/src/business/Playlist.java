package business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist {

	public ArrayList<Track> tracklist = new ArrayList<Track>();

	private int currentIndex;

	File myObj = new File("Playlist.m3u");

	public Playlist() {
		this.currentIndex = 0;
		trackslesen();
	}

	private void trackslesen() {
		String input = " ";

		try {

			try (Scanner myReader = new Scanner(myObj)) {

				while (myReader.hasNextLine()) {
					input = myReader.nextLine();
					tracklist.add(new Track(input));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Track get(int num) {
		return tracklist.get(num);

	}

	public Track get() {

		return tracklist.get(currentIndex);
	}

	public int getIndex(Track track) {
		for (Track t : tracklist) {
			if (t.getFileName() == track.getFileName()) {
				return tracklist.indexOf(track);
			}
		}

		return -1;
	}
}