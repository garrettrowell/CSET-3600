package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Data;
import application.HUB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InsertHUBFormController implements Initializable {

	@FXML
	VBox formPane;
	
	@FXML
	ChoiceBox<String> cbVlan;
	
	@FXML
	TextField tfName, tfSubnet, tfNetmask;
	
	@FXML
	Button btnFinish, btnCancel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<String> vlanList = FXCollections.observableArrayList("v2");
		cbVlan.setItems(vlanList);
	}
	
	@FXML
	private void closeForm(){
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void submitForm(){
		//parse everything in the textfield and set the appropreiate properties
		//The first part is just the buffer
		HUB hubObject = new HUB();
		String hubName = tfName.getText().trim();
		String hubNetMask = tfNetmask.getText().trim();
		String hubSubnet = tfSubnet.getText().trim(); 
		
		System.out.println(hubName);
		System.out.println(hubNetMask);
		System.out.println(hubSubnet);
		
		if(application.Validator.validateName(hubName) && 
			application.Validator.validateNetmask(hubNetMask) &&
			application.Validator.validateIp(hubSubnet)) {
				//set the properties of the hubObject
				hubObject.setName(hubName);
				hubObject.setNetmask(hubNetMask);
				hubObject.setSubnet(hubSubnet);
				
				//add it to all the other hubObjects in our hashmap
				Data.hubMap.put(hubName, hubObject);
				Stage stage = (Stage) btnFinish.getScene().getWindow();
				stage.close();
		}else {
			
		}
		

	}
}
