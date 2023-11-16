package scenes.bindingexample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class BindingExampleView extends BorderPane {
	Label headerLabel;
	Label textExample;
	TextField textField;
	Button backButton;
	
	public BindingExampleView() {
		headerLabel = new Label("Playlist View");
		this.setTop(headerLabel);
		
		textExample = new Label("Text Binding");
		this.setCenter(textExample);
		
		backButton = new Button("back");
		textField = new TextField();
		this.setBottom(textField);
		
		BorderPane.setAlignment(headerLabel, Pos.CENTER);
		BorderPane.setMargin(headerLabel, new Insets(10));

		BorderPane.setAlignment(backButton, Pos.CENTER);
		BorderPane.setMargin(backButton, new Insets(10));
	}

}
