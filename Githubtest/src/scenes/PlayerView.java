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
	Button loopButton;
	Button shuffleButton;
	Slider timeSlider;
	Label timeLabel;
	Label endTimeLabel;
	
	
	
	
	ImageView coverView;
	
	public PlayerView() {
	
		VBox header = new VBox();
		Label titleLabel = new Label("title");
		titleLabel.getStyleClass().add("main-text");
		
		final Image playlistbbutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/playlist3x.png");
		ImageView playlistbbutton1 = new ImageView(playlistbbutton); 
		playlistbbutton1.setFitHeight(25);
		playlistbbutton1.setFitWidth(25);
		
		final Image playbutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/play3x.png");
		ImageView playbutton1= new ImageView(playbutton); 
		playbutton1.setFitHeight(25);
		playbutton1.setFitWidth(25);
		
		final Image pausebutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/pause3x.png");
		ImageView pausebutton1= new ImageView(pausebutton); 
		pausebutton1.setFitHeight(25);
		pausebutton1.setFitWidth(25);
		
		final Image fowardbutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/skip_next3x.png");
		ImageView fowardbutton1= new ImageView(fowardbutton); 
		fowardbutton1.setFitHeight(25);
		fowardbutton1.setFitWidth(25);
		
		final Image backwardbutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/skip_back3x.png");
		ImageView backwardbutton1= new ImageView(backwardbutton); 
		backwardbutton1.setFitHeight(25);
		backwardbutton1.setFitWidth(25);
		
		final Image loopbutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/repeat3x.png");
		ImageView loopbutton1= new ImageView(loopbutton); 
		loopbutton1.setFitHeight(25);
		loopbutton1.setFitWidth(25);
		
		final Image shufflebutton = new Image("file:///C:/Users/Berha/git/repository3/Githubtest/assets/shuffle3x.png");
		ImageView shufflebutton1= new ImageView(shufflebutton); 
		shufflebutton1.setFitHeight(25);
		shufflebutton1.setFitWidth(25);
		
		
		Label albumLabel = new Label("album");
		albumLabel.getStyleClass().add("second-text");
		header.getChildren().addAll(titleLabel, albumLabel);
	
		
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
		switchButton = new Button();
		switchButton.setGraphic(playlistbbutton1);
		playButton = new Button();
		playButton.setGraphic(playbutton1);
		pauseButton = new Button();
		pauseButton.setGraphic(pausebutton1);
		fowardButton = new Button();
		fowardButton.setGraphic(fowardbutton1);
		backwardButton = new Button();
		backwardButton.setGraphic(backwardbutton1);
		loopButton = new Button();
		loopButton.setGraphic(loopbutton1);
		shuffleButton = new Button();
		shuffleButton.setGraphic(shufflebutton1);
		switchButton.getStyleClass().add("playlist-button");
		playButton.getStyleClass().add("control-button");
		pauseButton.getStyleClass().add("control-button");
		fowardButton.getStyleClass().add("control-button");
		backwardButton.getStyleClass().add("control-button");
		loopButton.getStyleClass().add("control-button");
		shuffleButton.getStyleClass().add("control-button");
		controller.getChildren().addAll(switchButton,backwardButton,playButton,pauseButton,fowardButton,loopButton,shuffleButton,slider);
		controller.setSpacing(10);
		controller.setId("controller");
		
		bottomPane.getChildren().addAll(timeLine,controller);
		this.setBottom(bottomPane);
	}

}
