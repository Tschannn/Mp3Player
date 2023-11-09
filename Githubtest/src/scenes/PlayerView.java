package scenes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import business.MP3Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import presentation.uicomponents.ImageViewPane;

/**
 * PlayerView repraesentiert die Bedienoberflaeche des Players.
 * 
 *  - Anzeige des aktuellen Songs
 *  - Zeitangabe des Songs
 *  - Bedeinelemente des Players
 *  
 * @author berdux
 *
 */
public class PlayerView extends BorderPane {
	/* als Attribute werden die UI-Elemente aufgefuehrt, deren Inhalt man sicherlich
	 * dann setzen will und natuerlich auch reagieren will.
	 */
	Label titleLabel;
	Label albumLabel;
	Slider slider;
	Button playButton;
	Button switchButton;
	Button pauseButton;
	Button fowardButton;
	Button backwardButton;
	Slider timeSlider;
	Label timeLabel;
	Label endTimeLabel;
	
	
	
	
	ImageView coverView;
	
	public PlayerView() {
	
		VBox header = new VBox();
		Label titleLabel = new Label("title");
		// ueber Style-Klassen-Attribute werden dann grundsaetzliche Eigenschaften im CSS gesetzt
		titleLabel.getStyleClass().add("main-text");
		
		Label albumLabel = new Label("album");
		// ueber Style-Klassen-Attribute werden dann grundsaetzliche Eigenschaften im CSS gesetzt
		albumLabel.getStyleClass().add("second-text");
		header.getChildren().addAll(titleLabel, albumLabel);
		// nur als Beispiel, wie man mit der Style-API umgehen wuerde 
		// dann aber im Projekt alles ueber CSS setzen
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(10, 5, 10, 5));
		header.setSpacing(15);
		
		this.setTop(header);
		
		coverView = new ImageView();
		try {
			coverView.setImage(new Image(new FileInputStream("assets/onepiece.jpg")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//coverView.getStyleClass().add("");
		
		// ImageView wird in den ImageViewPane eingehuellt, damit es dann mitskaliert
		ImageViewPane pane = new ImageViewPane(coverView);
		this.setCenter(new ImageViewPane(coverView));
		
		VBox bottomPane = new VBox();
		HBox timeLine = new HBox();
		timeLabel = new Label("0:00");
		timeSlider = new Slider();
		endTimeLabel = new Label("0:00");
		timeSlider.getStyleClass().add("time-slider");
		timeLine.getChildren().addAll(timeLabel,timeSlider,endTimeLabel);
		timeLine.setSpacing(10);
		timeLine.setPadding(new Insets(10));
		HBox.setHgrow(timeSlider, Priority.ALWAYS);
		
		HBox controller = new HBox();
		slider = new Slider(0, 100, 0.50);
		slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMax(1);
        slider.setMin(0);
		switchButton = new Button("Playlist");
		playButton = new Button("");
		pauseButton = new Button("١");
		fowardButton = new Button("٨");
		backwardButton = new Button("٧");
		switchButton.getStyleClass().add("playlist-button");
		playButton.getStyleClass().add("control-button");
		pauseButton.getStyleClass().add("control-button");
		fowardButton.getStyleClass().add("control-button");
		backwardButton.getStyleClass().add("control-button");
		controller.getChildren().addAll(switchButton,backwardButton,playButton,pauseButton,fowardButton,slider);
		controller.setSpacing(10);
		controller.setId("controller");
		
		bottomPane.getChildren().addAll(timeLine,controller);
		this.setBottom(bottomPane);
	}

}
