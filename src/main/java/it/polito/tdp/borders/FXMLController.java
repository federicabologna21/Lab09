
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    private ComboBox<Country> cmbStati;
    

    @FXML // fx:id="btnStatiRaggiungibili"
    private Button btnStatiRaggiungibili; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {

    	txtResult.clear();
    	
    	// PUNTO A 
    	String inserisciS = txtAnno.getText();
    	int inserisci;
    	try {
    		inserisci = Integer.parseInt(inserisciS);
    		
    		if (inserisci < 1816 || inserisci >2016) {
    			txtResult.setText("INSERIRE UN ANNO COMPRESO TRA 1816 E 2016");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("INSERIRE UN NUMERO VALIDO");
    		return;
    	}
    	
    	List<Border> confini = this.model.getCountryPairs(inserisci);
    	for (Border b: confini) {
    		txtResult.appendText(b.toString());
    	}
    	
    	if (confini.size() == 0) {
    		txtResult.setText("NON ESISTONO CONFINI VIA TERRA PER QUESTO ANNO");
    	}
    	
    	
    	// PUNTI B e C
    	// --> CREO IL GRAFO
    	this.model.creaGrafo(inserisci);
    	this.cmbStati.getItems().addAll(this.model.getDao().getVertici(inserisci, model.getIdMap()));
    	txtResult.appendText("GRAFO CREATO!"+"\n");
    	txtResult.appendText("# NUM VERTICI: "+this.model.getNumeroVertici()+"\n");
    	txtResult.appendText("# NUM ARCHI: "+this.model.getNumeroArchi()+"\n");
    	
    	txtResult.appendText("Elenco Stati:"+"\n"+this.model.getGradoVertici());
    	
    	txtResult.appendText("Il numero di componenti connesse al grafo è: "+this.model.getNumeroComponentiConnesse());
    	
    }
    
    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.clear();
    	Country scelta = cmbStati.getValue();
    	
    	if(scelta == null) {
    		txtResult.appendText("ERRORE! SELEZIONARE UNO STATO");
    		return;
    	}
    	
    	List<Country> elenco = this.model.trovaPercorso(scelta);
    	
    	if (elenco.size()==0) {
    		txtResult.appendText("NON CI SONO CONFINI PER LO STATO: "+scelta);
    		return;
    	}
    	
    	txtResult.appendText("Stati raggiungibili per: "+scelta+"\n");
    	for (Country c: elenco) {
    		if(!scelta.equals(c)) {
	    		txtResult.appendText(c.toString()+"\n");
    		}
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbStati != null : "fx:id=\"cmbStati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnStatiRaggiungibili != null : "fx:id=\"btnStatiRaggiungibili\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    }
}
