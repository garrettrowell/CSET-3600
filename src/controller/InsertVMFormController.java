package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;

import application.Data;
import application.Graphics;
import application.VM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
		ObservableList<String> osList = FXCollections.observableArrayList("LINUX", "WINDOW", "UNIX");
		cbOs.setItems(osList);
		
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
		//parse everything in the textfield and set the appropreiate properties
		//The first part is just the buffer
		VM vmObject = new VM();
		String vmName = tfName.getText().trim();
		String vmOs = cbOs.getValue().trim();
		String vmSrc = tfSrc.getText().trim();
		Double vmVer = Double.parseDouble(tfVer.getText().trim());
		TreeMap<String, String> vmInf = getInterfaces(infRow);
		
		//here for testing
		System.out.println(vmName);
		System.out.println(vmOs);
		System.out.println(vmSrc);
		System.out.println(vmVer);
		System.out.println(vmInf);
		
		//actually setting the properties
		vmObject.setName(vmName);
		vmObject.setOs(vmOs);
		vmObject.setSrc(vmSrc);
		vmObject.setVer(vmVer);
		vmObject.setInterfaces(vmInf);
		
		//add vmObject to all the other vmObjects in our hashmap
		Data.vmMap.put(vmName, vmObject);
		Stage stage = (Stage) btnFinish.getScene().getWindow();
		stage.close();
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
