package business;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist{
	
	private ArrayList<Track> tracklist = new ArrayList<Track>();

	private int aktuell;
	
	File myObj = new File("Playlist.m3u");
	
	
	

	public Playlist() {
		this.aktuell = 0;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Track getNext() {
		return get(++aktuell % tracklist.size());
	}
	
	public Track getPrev() {
		 if (aktuell == 0) {
	            aktuell = tracklist.size() - 1;
	        } else if (aktuell > 0) {
	            aktuell--;
	        }

	        return get(aktuell);
	}
	
	public Track get(int num) {
		return tracklist.get(num);
		
	}
	
	public Track get() {
		
		return tracklist.get(aktuell);
	}
	
}