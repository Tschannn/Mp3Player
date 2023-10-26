package scenes;


import business.Track;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TrackCell extends ListCell<Track> {
	HBox root;
	VBox infoPane;
	Label title;
	Label artist;
	CheckBox check;
	
	public TrackCell() {
		// View-Definition
		infoPane = new VBox();
		title = new Label();
		artist = new Label();
		infoPane.getChildren().addAll(title, artist);

		root = new HBox();
		check = new CheckBox();
		root.getChildren().addAll(check, infoPane);
		
	}
	
	@Override
	protected void updateItem(Track item, boolean empty) {
		super.updateItem(item, empty);
		
		if (!empty) {
			title.setText(item.getTitle());
			artist.setText(item.getArtist());
			check.setSelected(false);
			
			this.setGraphic(root);
		} else {
			this.setGraphic(null);
		}
		
	}

}
