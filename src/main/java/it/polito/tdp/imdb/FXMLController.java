/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.time.Year;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import it.polito.tdp.imdb.model.RegistaAdiacente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Year> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	Year a=this.boxAnno.getValue();
    	if(a==null) {
    		this.txtResult.setText("Errore, anno non inserito");
    		return;
    	}
    	String r=this.model.creaGrafo(a);
    	this.txtResult.setText(r);
    	List<Director> v=this.model.getV();
    	Collections.sort(v);
    	this.boxRegista.getItems().clear();
    	this.boxRegista.getItems().addAll(v);
    	this.btnAdiacenti.setDisable(false);
    	this.btnCercaAffini.setDisable(false);
    	
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	this.txtResult.clear();
    	Director d=this.boxRegista.getValue();
    	if(d==null) {
    		this.txtResult.setText("Errore, nessun regista inserito");
    		return;
    	}
    	List<RegistaAdiacente> result=this.model.getVicini(d);
    	this.txtResult.appendText("\n");
    	for(RegistaAdiacente r: result) {
    		this.txtResult.appendText(r.toString()+"\n");
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	this.txtResult.clear();
    	Director d=this.boxRegista.getValue();
    	if(d==null) {
    		this.txtResult.appendText("Errore, direttore non inserito");
    		return;
    	}
    	String num=this.txtAttoriCondivisi.getText();
    	if(num.isEmpty()) {
    		this.txtResult.setText("Errore, nessun numero inserito");
    		return;
    	}
    	Double c;
    	try {
    		c=Double.parseDouble(num);
    		if(c<0) {
        	this.txtResult.setText("Inserito un numero non positivo");
        	return;
        	}
        	
        	List<Director> result=this.model.affini(d, c);
        	this.txtResult.appendText("\n");
        	if(result==null) {
        		this.txtResult.appendText("Percorso non esiste");
        		return;
        	}
        	this.txtResult.appendText(result.toString());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Errore, non inserito un numero");
    	}
    	

    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	List<Year> anni=new LinkedList<>();
    	anni.add(Year.of(2004));
    	anni.add(Year.of(2005));
    	anni.add(Year.of(2006));
    	
    	this.boxAnno.getItems().addAll(anni);
    	this.btnAdiacenti.setDisable(true);
    	this.btnCercaAffini.setDisable(true);
    }
    
}
