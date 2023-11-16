package scenes;

import business.Track;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


/**
 * PlaylistView Fenster
 */
public class PlaylistView extends BorderPane {
	Label headerLabel;
	Button zuruckButton;
	Button filechooser;
	ListView<Track> playlistView;

	public PlaylistView() {
		HBox controll = new HBox();
		headerLabel = new Label("PLAYLIST");
		headerLabel.setId("headerLabel");
		this.setTop(headerLabel);

		playlistView = new ListView<>();
		this.setCenter(playlistView);
		playlistView.setId("table");
		
		final Image zuruckbutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/home1.png");
		ImageView zuruckbutton1= new ImageView(zuruckbutton); 
		zuruckbutton1.setFitHeight(25);
		zuruckbutton1.setFitWidth(25);
		

		zuruckButton = new Button();
		zuruckButton.setGraphic(zuruckbutton1);
		
		filechooser = new Button("Select a new Song");

		BorderPane.setAlignment(headerLabel, Pos.CENTER);
		BorderPane.setMargin(headerLabel, new Insets(10));


		BorderPane.setAlignment(zuruckButton, Pos.CENTER);
		BorderPane.setMargin(zuruckButton, new Insets(10));

	
		zuruckButton.getStyleClass().add("control-button");
		filechooser.getStyleClass().add("control-button"); 
		controll.getChildren().addAll(zuruckButton,filechooser);

		controll.setSpacing(10);
		controll.setId("controller");
		this.setBottom(controll);
	}

}
