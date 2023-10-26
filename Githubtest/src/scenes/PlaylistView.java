package scenes;

//import business.Song;
import business.Track;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * PlaylistView soll dann irgrnedwann mal die aktuelle Playlist anzeigen.
 * 
 * Hier jetzt einfach mal als zweite Dummy-Szene definiert.
 * 
 * @author berdux
 *
 */
public class PlaylistView extends BorderPane {
	Label headerLabel;
	Button skipButton;
	Button zuruckButton;
	ListView<Track> playlistView;

	public PlaylistView() {
		HBox controll = new HBox();
		headerLabel = new Label("Playlist View");
		this.setTop(headerLabel);

		playlistView = new ListView<>();
		this.setCenter(playlistView);
		playlistView.setId("table");

		zuruckButton = new Button("Player");

		skipButton = new Button(">");

		BorderPane.setAlignment(headerLabel, Pos.CENTER);
		BorderPane.setMargin(headerLabel, new Insets(10));

		BorderPane.setAlignment(skipButton, Pos.CENTER);
		BorderPane.setMargin(skipButton, new Insets(10));

		BorderPane.setAlignment(zuruckButton, Pos.CENTER);
		BorderPane.setMargin(zuruckButton, new Insets(10));

		skipButton.getStyleClass().add("control-button");
		zuruckButton.getStyleClass().add("control-button");
		controll.getChildren().addAll(zuruckButton,skipButton);

		controll.setSpacing(10);
		controll.setId("controller");
		this.setBottom(controll);
	}

}
