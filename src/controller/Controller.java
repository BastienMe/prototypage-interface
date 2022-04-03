package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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
		secController.lireFichierChat(0);
		//secController.net = net;
		//secController.easyModel = easyModel;
		
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);  
		stage.setScene(new Scene(root));
		stage.show();
		
		
		
		
	}
	
	public void OnEnvoyerMessageClicked(ActionEvent event) throws IOException {
		if(textFieldChat.getText().length() != 0) {
			String textFinal = textFieldChat.getText();
			textFieldChat.clear();
			Text text = new Text(textFinal);
			text.setWrappingWidth(150);
			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(295);
			BorderPane borderPane1 = new BorderPane();
			if(!hote) {
				borderPane.setLeft(borderPane1);
				borderPane1.setLeft(text);
				
				borderPane1.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 5px;");
				creeFichierChat(0);
				ecrireFichierChat(0,hote,textFinal);
				hote = !hote;
			} else {
				borderPane.setRight(borderPane1);
				borderPane1.setRight(text);
				
				borderPane1.setStyle("-fx-background-color: #6e97f0; -fx-background-radius: 5px;");
				creeFichierChat(0);
				ecrireFichierChat(0,hote,textFinal);
				hote = !hote;
			}
			
			vBoxChat.getChildren().add(borderPane);
			vBoxChat.autosize();
			
			anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
			scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
			
			scrollPaneChat.vvalueProperty().bind(vBoxChat.heightProperty());
			
			
		}
	}
	
	public void creeFichierChat(int idVoyage) {
		try {
	      File file = new File("Discussions/chat_"+ idVoyage +".txt");
	      file.createNewFile();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	public void ecrireFichierChat(int idVoyage, boolean hote, String message) {
		try {
	      FileWriter fileWriter = new FileWriter("Discussions/chat_"+ idVoyage +".txt",true);
	      if(hote) {
	    	  fileWriter.write("*"+message+"\n");
	      } else {
	    	  fileWriter.write(message+"\n");
	      }
	      fileWriter.close();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	public void lireFichierChat(int idVoyage) {
		try {
	      File file = new File("Discussions/chat_"+ idVoyage +".txt");
	      if(file.exists()) {
		      Scanner scanner = new Scanner(file);
		      while (scanner.hasNextLine()) {
		        String data = scanner.nextLine();
		        if(data.charAt(0) == '*') {
		        	setChat(true,data);
		        } else setChat(false,data);
		      }
		      scanner.close();
	      }
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
		
	}
	
	public void setChat(boolean hote, String message) {
		Text text = new Text(message);
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
		vBoxChat.getChildren().add(borderPane);
		vBoxChat.autosize();
		
		anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
		scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPaneChat.setVvalue(1.0); 
	}
}
