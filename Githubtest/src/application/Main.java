package application;
	
import java.util.HashMap;
import java.util.Map;

import business.MP3Player;
import javafx.application.Application;
import javafx.stage.Stage;
import scenes.PlayerView;
import scenes.PlaylistView;
import scenes.PlaylistViewController;
import scenes.PlayerViewController;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * 5.1 - Beispiel fuer den Aufbau mit separaten Scenes, die als Root-Elemente definiert sind.
 * 
 * Modellierung:
 * - Eigene Screens als View-Komponenten definieren
 * - Einfache Verwaltung der Views ueber einen Katalog (als Map)
 * - Layout & Styling als CCS-Definition
 *
 *
 */
public class Main extends Application {
	//
	private Stage primaryStage;
	
	// Verwaltung der einzelnen Screens
	private Map<String, Pane> scenes;
	
	private MP3Player player;
	

	public void init() {
		
		/* in der Anwednung gibt es einen Player, der dann von allen
		 * Seiten/Views bzw. deren Controllern aus erreichbar ist
		 */
		player = new MP3Player();
	}
	

	public void start(Stage primaryStage) {
		try {
			scenes = new HashMap<String, Pane>();
			
			// Definition der Screens/Scenes
			PlayerViewController playerViewController = new PlayerViewController(this, player);
			scenes.put("PlayerView", playerViewController.getRoot());
			
			PlaylistViewController playlistController = new PlaylistViewController(this,player);

			scenes.put("PlaylistView", playlistController.getRoot());
			
			// Beispielhaft nehmen wir uns aus der Verwaltung jetzt einfach mal den Start-Screen raus
			Pane root = scenes.get("PlayerView");
			
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			/* das Hauptfenster sich zu merken ist eine gute Idee,
			 * um dann zum Beispiel die Views wechseln zu koennen.
			 */
			this.primaryStage = primaryStage;
			primaryStage.getIcons().add(new Image("file:///C:/Users/Berha/eclipse-workspace/Mp3player_neu3/obamna.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode zum Wechsel einer Haupt-View.
	 * 
	 * Die Methode sucht sich die View mit dem Namen und setzt sie
	 * als Root-Element der Scene.
	 * 
	 * @param scene Name der View, die gesetzt werden soll
	 */
	public void switchScene(String scene) {
		if (scenes.containsKey(scene) ) {
			primaryStage.getScene().setRoot(scenes.get(scene));
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
