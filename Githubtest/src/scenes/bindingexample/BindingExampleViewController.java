package scenes.bindingexample;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class BindingExampleViewController {
	Pane root;
	
	Label titleLabel;
	Button backButton;
	TextField textField;
	Label textExample;

	
	public BindingExampleViewController() {
		BindingExampleView view = new BindingExampleView();
		
		titleLabel = view.headerLabel;
		backButton = view.backButton;
		
		textField = view.textField;
		textExample = view.textExample;
		
		root = view;
		
		initialize();
	}
	
	public void initialize() {
		
	}

	public Pane getRoot() {
		return root;
	}

}
	
