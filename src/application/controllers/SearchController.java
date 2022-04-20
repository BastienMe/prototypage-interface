package application.controllers;

import application.Main;
import application.models.Voyage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SearchController {

    @FXML private TextField txtRecherche2;
    @FXML private DatePicker debut_sejour2;
    @FXML private DatePicker fin_sejour2;
    @FXML private Text txtWarning2;

    @FXML private VBox tripListView = null;
    @FXML private ImageView imgMap;
    @FXML private ScrollPane scrollPaneResults;
    @FXML private Text txtResults;
    private boolean isAddingCards = false;
    private int startIndexToAddCards;

    private ArrayList<Voyage> listAllVoyages;
    private ArrayList<Voyage> listVoyagesResults2;

    public void init() {
        setMap();
        displaySearchResults();
        // todo: initialize map effects
//        for (int i=0; i<nodes.length;i++){ }
    }

    @FXML
    protected void onSearchButtonClick2(ActionEvent event) throws IOException {
        if (txtRecherche2.getText().length() > 1) {
            listVoyagesResults2 = searchVoyagesResults2();
            setListVoyagesResults(listVoyagesResults2);
            displaySearchResults();
        } else {
            displayWarning();
        }
    }

    public void displaySearchResults(){
        scrollPaneResults.setVvalue(0);
        displayResultsQty();
        initializeTripListView();
    }

    public void displayResultsQty(){
        // gère le nombre de résultats
        if (listVoyagesResults2.size() == 0) txtResults.setText("Désolé, nous n'avons trouvé aucun résultat...");
        else txtResults.setText(listVoyagesResults2.size() + " séjours peuvent vous satisfaire");
        imgMap.setVisible(listVoyagesResults2.size() != 0);
        scrollPaneResults.setVisible(listVoyagesResults2.size() != 0);
    }

    public void initializeTripListView(){
        startIndexToAddCards = 0;
        tripListView.getChildren().clear();
        if (listVoyagesResults2.size() > 0){
            int numberOfCardsToAdd = 10;
            if (listVoyagesResults2.size() < 10) numberOfCardsToAdd = listVoyagesResults2.size();
            addCardToTripListView(numberOfCardsToAdd, startIndexToAddCards);
        }
    }

    public void addCardToTripListView(int numberOfCards, int startIndex){
        Node[] cardsToAdd = new Node[numberOfCards];
        // initialize graphics
        for (int i=0; i<cardsToAdd.length;i++){
            try{
                final int j=i;
                cardsToAdd[i] = FXMLLoader.load(Main.class.getResource("views/trip-card-view.fxml"));
                cardsToAdd[i].setStyle("-fx-border-color: lightgrey");

                // bind data
                ImageView iv = (ImageView) cardsToAdd[i].lookup("#image");
                Label titre = (Label) cardsToAdd[i].lookup("#titre");
                Label contrepartie = (Label) cardsToAdd[i].lookup("#contrepartie");
                Label logement = (Label) cardsToAdd[i].lookup("#logement");
                Label heures = (Label) cardsToAdd[i].lookup("#heures");
                Label indexForTest = (Label) cardsToAdd[i].lookup("#indexForTest");

                File file = new File("src/application/assets/images/"+ listVoyagesResults2.get(startIndex+i).getVille() +".png");
                Image image = new Image(file.toURI().toString());
                iv.setImage(image);
                titre.setText(listVoyagesResults2.get(startIndex+i).getContreparties() + " à " + listVoyagesResults2.get(startIndex+i).getVille());
                contrepartie.setText(listVoyagesResults2.get(startIndex+i).getContreparties());
                logement.setText(listVoyagesResults2.get(startIndex+i).getType());
                heures.setText(listVoyagesResults2.get(startIndex+i).getHeure() + " h/j");
                indexForTest.setText("("+(startIndex+i+1)+")");

                // add some effect
                cardsToAdd[i].setOnMouseEntered(event -> {
                    cardsToAdd[j].setStyle("-fx-border-color: grey");
                });
                cardsToAdd[i].setOnMouseExited(event -> {
                    cardsToAdd[j].setStyle("-fx-border-color: lightgrey");
                });

                // add items
                tripListView.getChildren().add(cardsToAdd[i]);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        startIndexToAddCards = startIndex + numberOfCards;
    }

    @FXML void onScrollFinished(ScrollEvent event){
        if (!isAddingCards){
            isAddingCards = true;
            if (scrollPaneResults.getVvalue() == 1 && tripListView.getChildren().size() < listVoyagesResults2.size()){
                System.out.println("adding new cards");
                //define number of cards to add
                int numberOfCardsToAdd = 10;
                if (tripListView.getChildren().size() + 10 > listVoyagesResults2.size()) {
                    numberOfCardsToAdd = listVoyagesResults2.size() - tripListView.getChildren().size();
                }
                addCardToTripListView(numberOfCardsToAdd, startIndexToAddCards);
            }
            isAddingCards = false;
        }
    }

    public void setMap(){
        File file = new File("src/application/assets/map.png");
        Image image = new Image(file.toURI().toString());
        imgMap.setImage(image);
    }

    public void setListVoyagesResults(ArrayList<Voyage> listVoyagesResults2) {
        this.listVoyagesResults2 = listVoyagesResults2;
    }

    public void setListAllVoyages(ArrayList<Voyage> listAllVoyages) {
        this.listAllVoyages = listAllVoyages;
    }

    public void setTxtRecherche2(TextField txtDestination) {
        this.txtRecherche2.setText(txtDestination.getText());
    }

    public void setDebut_sejour2(DatePicker debut_sejour2) {
        this.debut_sejour2.setValue(debut_sejour2.getValue());
    }

    public void setFin_sejour2(DatePicker fin_sejour2) {
        this.fin_sejour2.setValue(fin_sejour2.getValue());
    }

    public ArrayList<Voyage> searchVoyagesResults2(){
        System.out.println(this.txtRecherche2.getText());
        if (debut_sejour2.getValue() != null)	System.out.println(this.debut_sejour2.getValue());
        if (fin_sejour2.getValue() != null)	System.out.println(this.fin_sejour2.getValue());

        // FOR DEMO: if input= "all", results if all 10000 voyages
        if (txtRecherche2.getText().equals("all")){
            return this.listAllVoyages;
        }

        ArrayList<Voyage> results = new ArrayList<Voyage>();

        for (Voyage v: listAllVoyages){
            if ((v.getVille().contains(txtRecherche2.getText()) || v.getContreparties().contains(txtRecherche2.getText()) || v.getType().contains(txtRecherche2.getText()))
                    && (debut_sejour2.getValue() == null || !debut_sejour2.getValue().isBefore(LocalDate.parse(v.getDateArrivee())) )
                    && (fin_sejour2.getValue() == null || !fin_sejour2.getValue().isAfter(LocalDate.parse(v.getDateDepart())) ) ){
                results.add(v);
            }
        }
        System.out.println(results.size());
        return results;
    }

    public void displayWarning(){
        txtWarning2.setVisible(true);
        new Thread( new Runnable() {
            public void run()  {
                try  { Thread.sleep( 3000 ); }
                catch (InterruptedException ie)  {}
                txtWarning2.setVisible(false);
            }
        } ).start();
    }
}
