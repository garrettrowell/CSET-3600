package controller;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import application.HUB;
import application.VM;
import application.Data;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MyController implements Initializable {
	@FXML
	TextArea textEditor;
	@FXML
	TabPane tabPane;
	@FXML
	Pane canvas;
	@FXML
	AnchorPane anchorPane;
	
	//These are the toggle button and the VBox container for the form
	public static ToggleButton btnEdit = new ToggleButton("Edit");
	public static VBox formPane = new VBox(5);
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
	}
	

	@FXML
	private void fileClose() {
		Platform.exit();
	}

	@FXML
	private void fileOpen() {
		File selectedFile = application.FileOperations.fileOpenConf();
		if (selectedFile != null) {
			textEditor.setText(application.FileOperations.readFile(selectedFile));
		}
	}

	@FXML
	private void fileSaveAs() {
		File selectedFile = application.FileOperations.fileSaveAsConf();
		if (selectedFile != null) {
			application.FileOperations.writeFile(selectedFile, textEditor.getParagraphs());
		}
	}

	@FXML
	private void fileSave() {

	}
	
	@FXML
	private void drawGraphical() {
		System.out.println("Number of Vm's Present "+application.Data.vmMap.keySet().size());
		System.out.println("Number of Hub's Present "+application.Data.hubMap.keySet().size());
		
		//Draw a Blue rectangle for each hub
		for(Map.Entry<String, HUB> entry : application.Data.hubMap.entrySet()) {
			String currentHubName = entry.getKey();
			HUB currentHub = application.Data.hubMap.get(currentHubName);
			currentHub.setPosX(Data.hubPosX);
			currentHub.setPosY(Data.hubPosY);
			canvas.getChildren().add(application.Graphics.createHUBNode(currentHub));
			Data.hubPosY += 150;
		}
		
		//Draw a Red rectangle for each vm
		for(Map.Entry<String, VM> entry : application.Data.vmMap.entrySet()) {
			String currentVMName = entry.getKey();
			VM currentVM = application.Data.vmMap.get(currentVMName);
			currentVM.setPosX(Data.vmPosX);
			currentVM.setPosY(Data.vmPosY);
			canvas.getChildren().add(application.Graphics.createVMNode(currentVM));
			Data.vmPosY += 150;
		}
				
		//Add ability to go into edit mode for the Textfield
		btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Query the popover setup schema to eventually find the Textfields and set the editable property based off the btnEdit
				for(Node node: formPane.getChildren()) {
					if(node instanceof HBox) {
						for(Node innerNode: ((HBox) node).getChildren()) {
							if(innerNode instanceof TextField) {
								((TextField) innerNode).editableProperty().bindBidirectional(btnEdit.selectedProperty());
								if(btnEdit.isSelected()) {
									innerNode.getStyleClass().remove("popover-form-textfield-inactive");
									innerNode.getStyleClass().add("popover-form-textfield-active");
								}
								
								if(!btnEdit.isSelected()) {
									innerNode.getStyleClass().remove("popover-form-textfield-active");
									innerNode.getStyleClass().add("popover-form-textfield-inactive");
								}
							}
						}
					}
				}
			}
		});
	}
}
