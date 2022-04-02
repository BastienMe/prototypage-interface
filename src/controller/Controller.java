package controller;

import java.io.File;
import java.io.IOException;

import controller.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
//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
public class Controller{
	
	@FXML TextField textFieldChat;
	@FXML VBox vBoxChat;
	@FXML AnchorPane anchorPaneChat;
	@FXML ScrollPane scrollPaneChat;
	
	boolean hote = false;
	
	public void OnContacterClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/Discussion.fxml"));
		Parent root = (Parent) loader.load();
		Controller secController = loader.getController();
		//secController.net = net;
		//secController.easyModel = easyModel;
		
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);  
		stage.setScene(new Scene(root));
		stage.show();
		
		
		
		
	}
	
	public void OnEnvoyerMessageClicked(ActionEvent event) throws IOException {
		//scrollPaneChat.setVvalue(1.0);  
		if(textFieldChat.getText().length() != 0) {
			System.out.print(textFieldChat.getText());
			String textFinal = textFieldChat.getText();
			textFieldChat.clear();
			
			/*int newLineCpt = textField.getText().length()/20;
			if(textField.getText().length()>20) {
				textFinal = "";
				int j = 0;
				for(int i = 0; i < textField.getText().length(); i++) {
					if(i == 20 && j == 0) {
						textFinal += "\n";
						j = 2;
					}
					textFinal += textField.getText().charAt(i);
					
				}
				
			}*/
			Text text = new Text(textFinal);
			text.setWrappingWidth(150);
			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(295);
			BorderPane borderPane1 = new BorderPane();
			if(!hote) {
				borderPane.setLeft(borderPane1);
				borderPane1.setLeft(text);
				
				borderPane1.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 5px;");
				hote = !hote;
			} else {
				borderPane.setRight(borderPane1);
				borderPane1.setRight(text);
				
				borderPane1.setStyle("-fx-background-color: #6e97f0; -fx-background-radius: 5px;");
				hote = !hote;
			}
			System.out.println(text.getLayoutBounds().getWidth());
			//vBoxChat.setSpacing(text.getBoundsInParent().getHeight() + 10);
			
			vBoxChat.getChildren().add(borderPane);
			vBoxChat.autosize();
			
			anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
			scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
			//scrollPaneChat.setVvalue(1.0);  
			
			
			scrollPaneChat.vvalueProperty().bind(vBoxChat.heightProperty());
		}
	}
}
