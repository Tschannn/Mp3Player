package business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PlaylistManager {
	
	public ArrayList<Track> tracklist = new ArrayList<Track>();
	
	File myObj = new File("Playlist.m3u");
	
	public void trackslesen() {
		String input = " ";
			try {
				try (Scanner myReader = new Scanner(myObj)){
					while(myReader.hasNextLine()) {
						input = myReader.nextLine();
						tracklist.add(new Track(input));
					}
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
	}
}
