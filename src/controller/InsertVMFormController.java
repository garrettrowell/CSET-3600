package controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import application.Data;
import application.Graphics;
import application.VM;
import application.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InsertVMFormController implements Initializable{

	@FXML
	Button btnFinish, btnCancel;
	
	@FXML
	TextField tfName, tfVer, tfSrc;
	
	@FXML
	ChoiceBox<String> cbOs, cbSrc;
	
	@FXML
	VBox infRow;
	
	int index = 1;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//list of the possible operating system users are allow to add
		ObservableList<String> osList = FXCollections.observableArrayList("LINUX", "WINDOW", "UNIX");
		cbOs.setItems(osList);
		
		//list of sources the users are allow to add
		ObservableList<String> srcList = FXCollections.observableArrayList("/srv/VMLibrary/JeOS");
		cbSrc.setItems(srcList);
	}
	
	@FXML
	private void closeForm(){
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}
	

	@FXML
	private void addInfRow(){
		//format string for the eth# label
		String eth = "eth";
		String infName = eth + Integer.toString(index) + ":";
		
		//add more rows for more interfaces 
		Graphics.addInfRow(infName, infRow);
		index++;
		
		//extend window size
		infRow.getScene().getWindow().sizeToScene();
	}
	
	@FXML
	private void submitForm(){
		//grab all the values from the input fields
		String vmName = tfName.getText();
		String vmOs = cbOs.getValue();
		String vmVer = tfVer.getText();
		String vmSrc = cbSrc.getValue();
		TreeMap<String, String> vmInf = getInterfaces(infRow);
		
		//validate test the values
		boolean validateName = Validator.validateName(vmName);
		boolean validateOs = Validator.validateOs(vmOs); 
		boolean validateVer = Validator.validateVer(vmVer);
		boolean validateSrc = Validator.validateSrc(vmSrc);
		boolean validateInterfaces = true;
		
		//validate each interface
		for(Map.Entry<String, String> entry : vmInf.entrySet()) {
			String currentIp = entry.getValue();
			if(!Validator.validateIp(currentIp)) {
				validateInterfaces = false;
			}
		}
		
		//only insert new VM object if all test passes
		if(validateName && validateOs && validateVer && validateSrc && validateInterfaces) {
			VM vmObject = new VM();
			vmObject.setName(vmName);
			vmObject.setOs(vmOs);
			vmObject.setSrc(vmSrc);
			vmObject.setVer(Double.parseDouble(vmVer));
			vmObject.setInterfaces(vmInf);
			
			Data.vmMap.put(vmName, vmObject);
			Stage stage = (Stage) btnFinish.getScene().getWindow();
			stage.close();
		}else {
			//create a popup to warn user
			//also highlight the input fields that are invalid
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Input");
			alert.setHeaderText("Error Input");
			alert.setContentText("Please check over your input parameters and resubmit.");
			alert.showAndWait();
				
			if(!validateName) {
				tfName.getStyleClass().add("vmform-invalid-field");
			}
			
			if(!validateOs) {
				cbOs.getStyleClass().add("vmform-invalid-field");
			}
			
			if(!validateVer) {
				tfVer.getStyleClass().add("vmform-invalid-field");
			}
			
			if(!validateSrc) {
				cbSrc.getStyleClass().add("vmform-invalid-field");
			}
			
			if(!validateInterfaces) {
				for(Node node : infRow.getChildren()) {
					if(node instanceof HBox) {
						for(Node innerNode : ((HBox) node).getChildren()) {
							if(innerNode instanceof TextField) {
								innerNode.getStyleClass().add("vmform-invalid-field");
							}
						}
					}
				}
			}
		}
	}
	
	//this method looks into the infRow (which contains more values if they continue to add more rows)
	//and create a Treemap from the ones that don't have empty textfields
	private TreeMap<String, String> getInterfaces(VBox formInfRow){
		TreeMap<String, String> infTree = new TreeMap<String, String>();
		for(Node node : formInfRow.getChildren()){
			String key = null;
			String value = null;
			if(node instanceof HBox){
				for(Node innerNode : ((HBox) node).getChildren()){
					if(innerNode instanceof Label){
						key = ((Label) innerNode).getText();
					}
					if(innerNode instanceof TextField){
						value = ((TextField) innerNode).getText().trim();
					}
					if(key != null && value != null){
						infTree.put(key, value);
					}
				}
			}
		}
		return infTree;
	}
}
