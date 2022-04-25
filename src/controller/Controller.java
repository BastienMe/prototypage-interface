package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Button;
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
//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
public class Controller{
	
	@FXML TextField textFieldChat;
	@FXML VBox vBoxChat;
	@FXML AnchorPane anchorPaneChat;
	@FXML ScrollPane scrollPaneChat;
	@FXML VBox vBoxToutMessagerie;
	@FXML Button buttonVoyageurValiderVoyage;
	@FXML Button buttonHoteValiderVoyage;
	@FXML Button buttonRefuserVoyage;
	boolean hote = false;
	
	
	public void OnContacterClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/DiscussionVoyageur.fxml"));
		Parent root = (Parent) loader.load();
		Controller secController = loader.getController();
		secController.lireFichierChat(12,344);
		
		Stage stage = new Stage();
		stage.setTitle("Communication");
		stage.initModality(Modality.APPLICATION_MODAL);  
		stage.setScene(new Scene(root));
		stage.show();

	}
	public void onConnexionClientClicked() {
		hote = false;
	}
	public void onConnexionHoteClicked() {
		hote = true;
	}
	public void OnEnvoyerMessageClicked(ActionEvent event) throws IOException {
		int idVoyage = 12;
		int idUtilisateur = 344;
		
		if(textFieldChat.getText().length() != 0) {
			String textFinal = textFieldChat.getText();
			textFieldChat.clear();
			Text text = new Text(textFinal);
			text.setWrappingWidth(250);
			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(611);
			BorderPane borderPane1 = new BorderPane();
			borderPane.setLeft(borderPane1);
			borderPane1.setLeft(text);
			
			borderPane1.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 5px;");
			creeFichierChat(idVoyage,idUtilisateur);
			ecrireFichierChat(idVoyage,idUtilisateur,hote,textFinal);
		
			
			vBoxChat.getChildren().add(borderPane);
			vBoxChat.autosize();
			
			anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
			scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
			
			scrollPaneChat.vvalueProperty().bind(vBoxChat.heightProperty());
			
			
		}
	}
	
	public void creeFichierChat(int idVoyage, int idUtilisateur) {
		try {
	      File file = new File("Discussions/"+ idVoyage + "_"+ idUtilisateur +".txt");
	      if(!file.exists()) {
	    	  file.createNewFile();
		      FileWriter fileWriter = new FileWriter("Discussions/"+ idVoyage + "_"+ idUtilisateur +".txt",true);
		      fileWriter.write(idVoyage+"\n");
		      fileWriter.write(idUtilisateur+"\n");
		      fileWriter.close();
	      }
	      
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
	
	public void ecrireFichierChat(int idVoyage, int idUtilisateur, boolean hote, String message) {
		try {
	      FileWriter fileWriter = new FileWriter("Discussions/"+ idVoyage+"_"+ idUtilisateur +".txt",true);
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
	
	public void lireFichierChat(int idVoyage, int utilisateur) {
		try {
	      File file = new File("Discussions/"+ idVoyage +"_"+ utilisateur +".txt");
	      if(file.exists()) {
		      Scanner scanner = new Scanner(file);
		      int ligne = 0;
		      while (scanner.hasNextLine()) {
		        String data = scanner.nextLine();
		        if(ligne > 1) {
			        if(data.charAt(0) == '*') { // Message de l'hote
			        	if(data.contains("|vh|")) {
			        		StringBuilder stringBuilder = new StringBuilder(data);
				        	stringBuilder.delete(0,5);
				        	setValidation("|vh|",stringBuilder.toString());
			        	} else if(data.contains("|rh|")) {
			        		StringBuilder stringBuilder = new StringBuilder(data);
				        	stringBuilder.delete(0,5);
				        	setValidation("|rh|",stringBuilder.toString());
			        	} else {
				        	StringBuilder stringBuilder = new StringBuilder(data);
				        	stringBuilder.deleteCharAt(0);
				        	setChat(true,stringBuilder.toString());
			        	}
			        }else if(data.contains("|vv|")){ // Message du voyageur
			        	if(!hote) buttonVoyageurValiderVoyage.setDisable(true);
			        	StringBuilder stringBuilder1 = new StringBuilder(data);
			        	stringBuilder1.delete(0, 4);
			        	setValidation("|vv|",stringBuilder1.toString());
			        }else setChat(false,data);
			        
		        }
		        ligne += 1;
		      }
		      scanner.close();
	      }
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    }
		anchorPaneChat.setPrefHeight(vBoxChat.getHeight()+30);
		scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPaneChat.setVvalue(1.0); 
	}
	
	public void setChat(boolean messageHote, String message) {
		
		Text text = new Text(message);
		text.setWrappingWidth(250);
		BorderPane borderPane = new BorderPane();
		borderPane.setPrefWidth(611);
		BorderPane borderPane1 = new BorderPane();
		if(!messageHote) {
			if(!hote) {
				borderPane.setLeft(borderPane1);
				borderPane1.setLeft(text);
				
				borderPane1.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 5px;");
			} else {
				borderPane.setRight(borderPane1);
				borderPane1.setRight(text);
				
				borderPane1.setStyle("-fx-background-color: #6e97f0; -fx-background-radius: 5px;");
			}
		} else {
			if(hote) {
				borderPane.setLeft(borderPane1);
				borderPane1.setLeft(text);
				
				borderPane1.setStyle("-fx-background-color: #D3D3D3; -fx-background-radius: 5px;");
			} else {
				borderPane.setRight(borderPane1);
				borderPane1.setRight(text);
				
				borderPane1.setStyle("-fx-background-color: #6e97f0; -fx-background-radius: 5px;");
			}
		}
		vBoxChat.getChildren().add(borderPane);
		vBoxChat.autosize();
		
		anchorPaneChat.setPrefHeight(vBoxChat.getHeight()+10);
		scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPaneChat.setVvalue(1.0); 
	}
	
	public void setValidation(String code, String message) {
		if(code.equals("|vv|")) {
			Text text = new Text(message);
			text.setWrappingWidth(250);
			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(611);
			BorderPane borderPane1 = new BorderPane();
		
			borderPane.setCenter(borderPane1);
			borderPane1.setCenter(text);
			
			borderPane1.setStyle("-fx-background-color: #0DFF00; -fx-background-radius: 5px;");
			
			vBoxChat.getChildren().add(borderPane);
			vBoxChat.autosize();
			
			anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
			scrollPaneChat.setVvalue(1.0); 
			
			
			
			// Debloque les bouton de l'hote
			if(buttonHoteValiderVoyage != null) {
				buttonHoteValiderVoyage.setDisable(false);
				buttonRefuserVoyage.setDisable(false);
			}	
		}else if(code.equals("|vh|")) {
			Text text = new Text(message);
			text.setWrappingWidth(250);
			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(611);
			BorderPane borderPane1 = new BorderPane();
		
			borderPane.setCenter(borderPane1);
			borderPane1.setCenter(text);
			
			borderPane1.setStyle("-fx-background-color: #0DFF00; -fx-background-radius: 5px;");
			
			vBoxChat.getChildren().add(borderPane);
			vBoxChat.autosize();
			
			anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
			scrollPaneChat.setVvalue(1.0); 
			
			if(buttonHoteValiderVoyage != null) {
				buttonHoteValiderVoyage.setDisable(true);
				buttonRefuserVoyage.setDisable(true);
			}	
			
		}else if(code.equals("|rh|")) {
			
			Text text = new Text(message);
			text.setWrappingWidth(250);
			BorderPane borderPane = new BorderPane();
			borderPane.setPrefWidth(611);
			BorderPane borderPane1 = new BorderPane();
		
			borderPane.setCenter(borderPane1);
			borderPane1.setCenter(text);
			
			borderPane1.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 5px;");
			
			vBoxChat.getChildren().add(borderPane);
			vBoxChat.autosize();
			
			anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
			scrollPaneChat.setVvalue(1.0); 
			
		
			if(buttonRefuserVoyage != null)	buttonRefuserVoyage.setDisable(true);
			if(buttonVoyageurValiderVoyage != null)	buttonVoyageurValiderVoyage.setDisable(false);
		}
	}
	
	public void onMessagerieClicked() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/ListeDiscussions.fxml"));
		Parent root = (Parent) loader.load();
		Controller secController = loader.getController();
		
		//secController.net = net;
		//secController.easyModel = easyModel;
		secController.setUpMessagerie();
		secController.hote = hote;
		Stage stage = new Stage();
		stage.setTitle("Messagerie");
		stage.initModality(Modality.APPLICATION_MODAL);  
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public static ArrayList<String> readDiscussionsFolder() {
		ArrayList<String> listeNomFichiers = new ArrayList<String>();
		File folder = new File("Discussions");
		if(folder.listFiles() != null) {
			for (final File fileEntry : folder.listFiles()) {
				//System.out.println(fileEntry.getName());
				listeNomFichiers.add(fileEntry.getName());
		    }
		}
		
		return listeNomFichiers;
	}
	
	public void setUpMessagerie() {
		List<String> nomMessageries = readDiscussionsFolder();
		for (String nomMessagerie : nomMessageries) {
			//System.out.println(nomMessagerie);
			StringBuilder stringBuilder = new StringBuilder(nomMessagerie);
			stringBuilder.delete(nomMessagerie.length()-4, nomMessagerie.length());
			//System.out.println(stringBuilder.toString());
			String[] tokens = stringBuilder.toString().split("_");
			
			int ligne = 0;
			String nomVoyage ="";
			String nomUtilisateur="";
			String dernierMessage="";
			try {
		      File file = new File("Discussions/"+nomMessagerie);
		      if(file.exists()) {
			      Scanner scanner = new Scanner(file);
			      while (scanner.hasNextLine()) {
			        String data = scanner.nextLine();
			        if(ligne == 0) nomVoyage = data;
			        else if(ligne == 1) nomUtilisateur = data;
			        else {
			        	StringBuilder stringBuilder1 = new StringBuilder(data);
			        	if(data.contains("|vv|")) {
			        		stringBuilder1.delete(0, 4);
			        	} else if(data.contains("|vh|") || data.contains("|rh|")) {
			        		stringBuilder1.delete(0, 5);
			        	}
			        	if(stringBuilder1.charAt(0) == '*')
			        		stringBuilder1.deleteCharAt(0);
			        	dernierMessage = stringBuilder1.toString();
			        }
			        ligne += 1;
			      }
			      scanner.close();
		      }
		    } catch (FileNotFoundException e) {
		      e.printStackTrace();
		    }
				
			Text textNomVoyage = new Text(nomVoyage);
			Text textNomUtilisateur = new Text(nomUtilisateur);
			Text textDernierMessage = new Text(dernierMessage);
			VBox vBoxMessagerie = new VBox();
			vBoxMessagerie.getChildren().add(textNomVoyage);
			vBoxMessagerie.getChildren().add(textNomUtilisateur);
			vBoxMessagerie.getChildren().add(textDernierMessage);
			vBoxMessagerie.setStyle("-fx-background-color: #6e97f0; -fx-background-radius: 5px;");
			vBoxMessagerie.setOnMouseClicked(event -> {
				FXMLLoader loader = null;
				
				if(hote) 
					loader = new FXMLLoader(getClass().getResource("../views/DiscussionHote.fxml"));
				else 
					loader = new FXMLLoader(getClass().getResource("../views/DiscussionVoyageur.fxml"));
				Parent root;
				try {
					root = (Parent) loader.load();
					Controller secController = loader.getController();
					
					secController.hote = hote;
					secController.lireFichierChat(Integer.parseInt(tokens[0]),Integer.parseInt(tokens[1]));
					
					Stage stage = new Stage();
					stage.setTitle("Communication");
					stage.initModality(Modality.APPLICATION_MODAL);  
					stage.setScene(new Scene(root));
					stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			});
			vBoxToutMessagerie.getChildren().add(vBoxMessagerie);
	    }
	}
	
	public void onVoyageurValiderVoyageClicked() {
		buttonVoyageurValiderVoyage.setDisable(true);
		int idVoyage = 12;
		int idUtilisateur = 344;
		//buttonHoteValiderVoyage.setDisable(false);
		Text text = new Text("Voyage validé par le voyageur");
		text.setWrappingWidth(250);
		BorderPane borderPane = new BorderPane();
		borderPane.setPrefWidth(611);
		BorderPane borderPane1 = new BorderPane();
	
		borderPane.setCenter(borderPane1);
		borderPane1.setCenter(text);
		
		borderPane1.setStyle("-fx-background-color: #0DFF00; -fx-background-radius: 5px;");
		creeFichierChat(idVoyage,idUtilisateur);
		ecrireFichierChat(idVoyage,idUtilisateur,false,"|vv|Voyage validé par le voyageur");
	
		vBoxChat.getChildren().add(borderPane);
		vBoxChat.autosize();
		
		anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
		scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		scrollPaneChat.vvalueProperty().bind(vBoxChat.heightProperty());
	}
	
	public void onHoteValiderVoyageClicked() {
		buttonHoteValiderVoyage.setDisable(true);
		int idVoyage = 12;
		int idUtilisateur = 344;
		//buttonHoteValiderVoyage.setDisable(false);
		Text text = new Text("Voyage validé par l'hôte");
		text.setWrappingWidth(250);
		BorderPane borderPane = new BorderPane();
		borderPane.setPrefWidth(611);
		BorderPane borderPane1 = new BorderPane();
	
		borderPane.setCenter(borderPane1);
		borderPane1.setCenter(text);
		
		borderPane1.setStyle("-fx-background-color: #0DFF00; -fx-background-radius: 5px;");
		creeFichierChat(idVoyage,idUtilisateur);
		ecrireFichierChat(idVoyage,idUtilisateur,true,"|vh|Voyage validé par l'hôte");
	
		vBoxChat.getChildren().add(borderPane);
		vBoxChat.autosize();
		
		anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
		scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		scrollPaneChat.vvalueProperty().bind(vBoxChat.heightProperty());
	}
	public void onRefuserVoyageClicked() {
		buttonRefuserVoyage.setDisable(true);
		buttonHoteValiderVoyage.setDisable(true);
		int idVoyage = 12;
		int idUtilisateur = 344;
		//buttonHoteValiderVoyage.setDisable(false);
		Text text = new Text("Voyage refusé par l'hôte");
		text.setWrappingWidth(250);
		BorderPane borderPane = new BorderPane();
		borderPane.setPrefWidth(611);
		BorderPane borderPane1 = new BorderPane();
	
		borderPane.setCenter(borderPane1);
		borderPane1.setCenter(text);
		
		borderPane1.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 5px;");
		creeFichierChat(idVoyage,idUtilisateur);
		ecrireFichierChat(idVoyage,idUtilisateur,true,"|rh|Voyage refusé par l'hôte");
		
		vBoxChat.getChildren().add(borderPane);
		vBoxChat.autosize();
		
		anchorPaneChat.setPrefHeight(vBoxChat.getHeight());
		scrollPaneChat.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
		
		scrollPaneChat.vvalueProperty().bind(vBoxChat.heightProperty());
	}
	
}
