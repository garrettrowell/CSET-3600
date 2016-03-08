package controller;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import application.HUB;
import application.VM;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
	ToggleButton btnEdit = new ToggleButton("Edit");
	VBox formPane = new VBox(5);
	
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
	private void scrollXAxis() {
		drawGraphical();
	}
	
	@FXML
	private void scrollYAxis() {
		drawGraphical();
	}
	
	@FXML
	private void drawGraphical() {
		System.out.println("Number of Vm's Present "+application.Data.vmMap.keySet().size());
		System.out.println("Number of Hub's Present "+application.Data.hubMap.keySet().size());
		
		int maxX=100;
		int maxY=100;
		
		//Draw a Blue rectangle for each hub
		int hubx = 50;
		int huby = 50;
		for(Map.Entry<String, HUB> entry : application.Data.hubMap.entrySet()) {
			String currentHubName = entry.getKey();
			
			//Create a rectangle
			Rectangle rec = new Rectangle(maxX,maxY);
			rec.setFill(Color.BLUE);
			
			//Create Text Object
			Text name = new Text(application.Data.hubMap.get(currentHubName).getName().toString());
			
			//Retrieve the the object's properties
			String content = "Name: " + application.Data.hubMap.get(currentHubName).getName().toString() + "\n"
					+ "Inf: " + application.Data.hubMap.get(currentHubName).getInf() + "\n"
					+ "Subnet: " + application.Data.hubMap.get(currentHubName).getSubnet().toString() + "\n"
					+ "Netmask: " + application.Data.hubMap.get(currentHubName).getNetmask().toString();
			
			//Create and insert tooltips for each object
			Tooltip tool = new Tooltip(content);
			Tooltip.install(rec, tool);
			
			//Put both the Rectangle and Text into a stack pane to make it it's own entity
			StackPane stack = new StackPane();
			stack.getChildren().addAll(rec, name);
			
			//eventhandler for when the hubs are clicked
			stack.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					//this VBox is for holding everything together so we can just throw all content onto popover at once
					VBox content = new VBox(5);
					content.getStyleClass().add("popover-content");
					content.setId("contentPane");
					
					//the first row contains the big label and the toggle button
					HBox headerRow = new HBox(25);
					Label lname = new Label(application.Data.hubMap.get(currentHubName).getName().toString());
					lname.getStyleClass().add("popover-label");
					btnEdit.setPrefSize(50, 10);
					headerRow.getChildren().addAll(lname,btnEdit);
					
					//just a line separator
					Separator hr = new Separator(Orientation.HORIZONTAL);
					hr.minWidth(javafx.scene.control.Control.USE_COMPUTED_SIZE);
					
					//setting id for the formPane to be search for(which is declare at the very top)
					formPane.setId("formPane");
					
					//the first row of the form
					//each row of the form has one label and a TextField
					HBox formRow1 = new HBox(15);
					formRow1.getStyleClass().add("popover-form");
					Label lformName = new Label("Name:");
					lformName.getStyleClass().add("popover-form-label");
					TextField tfFormName = new TextField();
					tfFormName.getStyleClass().add("popover-form-textfield");
					tfFormName.setText(application.Data.hubMap.get(currentHubName).getName());
					tfFormName.setEditable(false);
					
					//Add the label and the Textfield to row1
					formRow1.getChildren().addAll(lformName,tfFormName);
					
					//Here is same concept for row1 but for row2
					HBox formRow2 = new HBox(15);
					formRow2.getStyleClass().add("popover-form");
					Label lformSubnet = new Label("Subnet:");
					lformSubnet.getStyleClass().add("popover-form-label");
					TextField tfFormSubnet = new TextField();
					tfFormSubnet.getStyleClass().add("popover-form-textfield");
					tfFormSubnet.setText(application.Data.hubMap.get(currentHubName).getSubnet());
					tfFormSubnet.setEditable(false);
					formRow2.getChildren().addAll(lformSubnet,tfFormSubnet);
					
					//Add both rows into the formPane container so it becomes one form
					formPane.getChildren().addAll(formRow1,formRow2);
					
					//Add all the headerRow, line separator, and the form to one big container
					content.getChildren().addAll(headerRow, hr, formPane);
					
					//Add the large final container into the popover
					PopOver popOver = new PopOver(content);
					popOver.show(stack);
				}
			});
			//Location of entity on the pane
			stack.relocate(hubx, huby);
			canvas.getChildren().add(stack);
			huby=huby+150;
		}
		
		//Draw a Red rectangle for each vm
		int vmx = 200;
		int vmy = 50;
		for(Map.Entry<String, VM> entry : application.Data.vmMap.entrySet()) {
			String currentVMName = entry.getKey();
			
			//Create a rectangle
			Rectangle rec = new Rectangle(maxX,maxY);
			rec.setFill(Color.RED);
			
			//Create Text Object
			Text name = new Text(application.Data.vmMap.get(currentVMName).getName().toString());
			
			//Retrieve the the object's properties
			String content = "Name: " + application.Data.vmMap.get(currentVMName).getName().toString() + "\n"
					+ "OS: " + application.Data.vmMap.get(currentVMName).getOs() + "\n"
					+ "Ver: " + application.Data.vmMap.get(currentVMName).getVer().toString() + "\n"
					+ "Src: " + application.Data.vmMap.get(currentVMName).getSrc().toString() + "\n"
					+ application.Data.vmMap.get(currentVMName).getInterfaces().toString();
			
			//Create and insert tooltips for each object
			Tooltip tool = new Tooltip(content);
			Tooltip.install(rec, tool);
			
			//Put both the Rectangle and Text into a stack pane to make it it's own entity
			StackPane stack = new StackPane();
			stack.getChildren().addAll(rec, name);
			
			//Location of entity on the pane
			stack.relocate(vmx, vmy);
			canvas.getChildren().add(stack);
			vmy=vmy+150;
		}
		
		// determines which x&y value for vm or hub is larger
		if (hubx > vmx) {
			maxX=hubx-150;
		} else {
			maxX=vmx-150;
		}
		if (huby > vmy) {
			maxY=huby-150;
		} else {
			maxY=vmy-150;
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
							}
						}
					}
				}
			}
		});
	}
}
