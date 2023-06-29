/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacente;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

	private boolean grafoCreato = false;

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	//txtResult.appendText("Cerco cammino peso massimo...");
    	
    	if (!this.grafoCreato) {
    		txtResult.setText("Non e' stato cretao il nodo");
    	}
    	
    	String input = this.txtPassi.getText();
    	String porzione  =this.boxPorzioni.getValue();
    	
    	if (input == "" || porzione == "") {
    		txtResult.setText("Non e' stato inserito un valore");
    	}
    	
    	int N = 0;
    	try {
    		N = Integer.parseInt(input);
    	}catch (NumberFormatException e ) {
    		txtResult.appendText("Non è stato inserito un valore accettabile");
    		return;
    	}
    	
    	List<String> cammino = this.model.getCammino(N, porzione);
       	if(cammino.isEmpty()) {
    		txtResult.appendText("Non ho trovato un cammino di lunghezza N\n");
       	}
    	for (String nodo : cammino ) {
    		txtResult.appendText(nodo + "\n") ;
    	}
    	
    	txtResult.appendText("Il peso totale e' pari a " + this.model.getPesoMax() + "\n");
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	//txtResult.appendText("Cerco porzioni correlate...");
    	
    	if (!this.grafoCreato)  {
    		txtResult.setText("IL grafo non e' stato creato");
    	}
    	
    	String porzione = this.boxPorzioni.getValue();
    	
    	if (porzione == null) {
    		txtResult.setText("Non ci sono  valori");
    	}
    	
    	List<Adiacente> adiacenti = this.model.getAdiacenti(porzione);
    	
    	for (Adiacente a : adiacenti) {
    		txtResult.appendText(a.toString() + "\n");
    	}
    	
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	
    	String input  = this.txtCalorie.getText();
    	
    	if (input == "") {
    		this.txtResult.setText("Errore: inserire un valore");
    	}
    	
    	Double calorie = 0.0;
    	
    	try {
    		
    		calorie = Double.parseDouble(input);
    	
    		
    	}catch (NumberFormatException e ) {
    		txtResult.setText("Errore: non e' stato inserito un valore accettabile");
    		return;
    	}
    	
    	model.creaGrafo(calorie);
    	this.grafoCreato  = true;
    	txtResult.appendText("Grafo creato!\n");
    	txtResult.appendText("#Vertici: " + this.model.getNumVertici() + "\n#Archi: " + this.model.getNumArchi());
    	
    	
    	
    	this.boxPorzioni.getItems().addAll(this.model.getVertici());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
