package controller;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import application.HUB;
import application.VM;
import application.Data;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
<<<<<<< HEAD
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
=======
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
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
>>>>>>> pdouanglee

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
	
	private StackPane createHUBNode(HUB hubObject) {
		//each hub is represented by a blue rectangle
		Rectangle node = new Rectangle(Data.nodeLength,Data.nodeWidth);
		node.setFill(Color.BLUE);
		
		//the hub name in the rectangle
		Label lnodeName = new Label(hubObject.getName());
		
		//this stackpane stack the label on top of the rectangle to make them one entity
		StackPane nodeContainer = new StackPane();
		nodeContainer.getChildren().addAll(node, lnodeName);
		nodeContainer.relocate(hubObject.getPosX(), hubObject.getPosY());
		
		//add a popover when one of the hub is clicked
		nodeContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				PopOver popOver = new PopOver(createHUBPopOverContent(hubObject));
				popOver.show(nodeContainer);
			}
			
		});
		
		return nodeContainer;
	};
	
	private VBox createHUBPopOverContent(HUB hubObject) {
		//the first row contains the big label and the toggle button
		HBox headerRow = new HBox(25);
		Label lname = new Label(hubObject.getName());
		lname.getStyleClass().add("popover-label");
		btnEdit.setPrefSize(50, 10);
		headerRow.getChildren().addAll(lname,btnEdit);
		
		//just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
		
		//this is the form within the popover (declare at top of class)
		formPane.setId("formPane");
		
		//the first row of the form (Hub name)
		//each row of the form contains a label and a Textfield
		HBox formRow1 = new HBox(15);
		formRow1.getStyleClass().add("popover-form");
		Label lFormName = new Label("Name:");
		lFormName.getStyleClass().add("popover-form-label");
		TextField tfFormName = new TextField();
		tfFormName.getStyleClass().add("popover-form-textfield-inactive");
		tfFormName.setText(hubObject.getName());
		tfFormName.setEditable(false);
		
		//put the two components into a format of a row
		formRow1.getChildren().addAll(lFormName, tfFormName);
		
		//Here is the same layout as row 1 (Hub subnet)
		HBox formRow2 = new HBox(15);
		formRow2.getStyleClass().add("popover-form");
		Label lformSubnet = new Label("Subnet:");
		lformSubnet.getStyleClass().add("popover-form-label");
		TextField tfFormSubnet = new TextField();
		tfFormSubnet.getStyleClass().add("popover-form-textfield-inactive");
		tfFormSubnet.setText(hubObject.getSubnet());
		tfFormSubnet.setEditable(false);
		formRow2.getChildren().addAll(lformSubnet,tfFormSubnet);
		
		//Row 3 (Hub netmask)
		HBox formRow3 = new HBox(15);
		formRow3.getStyleClass().add("popover-form");
		Label lformNetmask = new Label("Netmask:");
		lformNetmask.getStyleClass().add("popover-form-label");
		TextField tfFormNetmask = new TextField();
		tfFormNetmask.getStyleClass().add("popover-form-textfield-inactive");
		tfFormNetmask.setText(hubObject.getNetmask());
		tfFormNetmask.setEditable(false);
		formRow3.getChildren().addAll(lformNetmask,tfFormNetmask);
		
		//Row 4 (Hub interfaces)
		HBox formRow4 = new HBox(15);
		formRow4.getStyleClass().add("popover-form");
		Label lformInf = new Label("Interfaces:");
		lformInf.getStyleClass().add("popover-form-label");
		TextField tfFormInf = new TextField();
		tfFormInf.getStyleClass().add("popover-form-textfield-inactive");
		tfFormInf.setText(hubObject.getInf());
		tfFormInf.setEditable(false);
		formRow4.getChildren().addAll(lformInf,tfFormInf);
		
		//add all the rows to make them one form, but clear it first
		formPane.getChildren().clear();
		formPane.getChildren().addAll(formRow1, formRow2, formRow3, formRow4);
		
		//this content container is eveything that is going to go on the PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
		
		//add all nodes (the headerRow, separator, and the form) into one container to be transfer to the PopOver
		content.getChildren().addAll(headerRow, hr, formPane);
		
		return content;
	}
	
	private StackPane createVMNode(VM vmObject) {
		//each vm is represented by a red rectangle
		Rectangle node = new Rectangle(Data.nodeLength,Data.nodeWidth);
		node.setFill(Color.RED);
				
		//the hub name in the rectangle
		Label lnodeName = new Label(vmObject.getName());
				
		//this stackpane stack the label on top of the rectangle to make them one entity
		StackPane nodeContainer = new StackPane();
		nodeContainer.getChildren().addAll(node, lnodeName);
		nodeContainer.relocate(vmObject.getPosX(), vmObject.getPosY());
				
		//add a popover when one of the hub is clicked
		nodeContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				PopOver popOver = new PopOver(createVMPopOverContent(vmObject));
				popOver.show(nodeContainer);
			}			
		});
				
		return nodeContainer;
	}

	private VBox createVMPopOverContent(VM vmObject) {
		//the first row contains the big label and the toggle button
		HBox headerRow = new HBox(25);
		Label lname = new Label(vmObject.getName());
		lname.getStyleClass().add("popover-label");
		btnEdit.setPrefSize(50, 10);
		headerRow.getChildren().addAll(lname,btnEdit);
				
		//just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
				
		//this is the form within the popover (declare at top of class)
		formPane.setId("formPane");
				
		//the first row of the form (VM name)
		//each row of the form contains a label and a Textfield
		HBox formRow1 = new HBox(15);
		formRow1.getStyleClass().add("popover-form");
		Label lFormName = new Label("Name:");
		lFormName.getStyleClass().add("popover-form-label");
		TextField tfFormName = new TextField();
		tfFormName.getStyleClass().add("popover-form-textfield-inactive");
		tfFormName.setText(vmObject.getName());
		tfFormName.setEditable(false);
				
		//put the two components into a format of a row
		formRow1.getChildren().addAll(lFormName, tfFormName);
				
		//Here is the same layout as row 1 (VM os)
		HBox formRow2 = new HBox(15);
		formRow2.getStyleClass().add("popover-form");
		Label lformSubnet = new Label("OS:");
		lformSubnet.getStyleClass().add("popover-form-label");
		TextField tfFormSubnet = new TextField();
		tfFormSubnet.getStyleClass().add("popover-form-textfield-inactive");
		tfFormSubnet.setText(vmObject.getOs());
		tfFormSubnet.setEditable(false);
		formRow2.getChildren().addAll(lformSubnet,tfFormSubnet);
				
		//Row 3 (VM ver)
		HBox formRow3 = new HBox(15);
		formRow3.getStyleClass().add("popover-form");
		Label lformNetmask = new Label("Ver:");
		lformNetmask.getStyleClass().add("popover-form-label");
		TextField tfFormNetmask = new TextField();
		tfFormNetmask.getStyleClass().add("popover-form-textfield-inactive");
		tfFormNetmask.setText(vmObject.getVer().toString());
		tfFormNetmask.setEditable(false);
		formRow3.getChildren().addAll(lformNetmask,tfFormNetmask);
				
		//Row 4 (VM src)
		HBox formRow4 = new HBox(15);
		formRow4.getStyleClass().add("popover-form");
		Label lformInf = new Label("Src:");
		lformInf.getStyleClass().add("popover-form-label");
		TextField tfFormInf = new TextField();
		tfFormInf.getStyleClass().add("popover-form-textfield-inactive");
		tfFormInf.setText(vmObject.getSrc());
		tfFormInf.setEditable(false);
		formRow4.getChildren().addAll(lformInf,tfFormInf);
		
		//add all the rows we have now to the formPane, but first clear the form
		formPane.getChildren().clear();
		formPane.getChildren().addAll(formRow1, formRow2, formRow3, formRow4);
		
		//this will dynamically add rows to the formPane base on the # of eth0 entries
		for(Map.Entry<String, String> entry : vmObject.getInterfaceHashMap().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			HBox ethRow = new HBox(15);
			ethRow.getStyleClass().add("popover-form");
			Label lformEth = new Label(key);
			lformEth.getStyleClass().add("popover-form-label");
			TextField tfFormEth = new TextField();
			tfFormEth.getStyleClass().add("popover-form-textfield-inactive");
			tfFormEth.setText(value);
			tfFormEth.setEditable(false);
			ethRow.getChildren().addAll(lformEth,tfFormEth);
			
			formPane.getChildren().add(ethRow);
		}
				
		//this content container is eveything that is going to go on the PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
				
		//add all nodes (the headerRow, separator, and the form) into one container to be transfer to the PopOver
		content.getChildren().addAll(headerRow, hr, formPane);
				
		return content;
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
			canvas.getChildren().add(createHUBNode(currentHub));
			Data.hubPosY += 150;
			//huby=huby+150;
		}
		
		//Draw a Red rectangle for each vm
		for(Map.Entry<String, VM> entry : application.Data.vmMap.entrySet()) {
			String currentVMName = entry.getKey();
			VM currentVM = application.Data.vmMap.get(currentVMName);
			currentVM.setPosX(Data.vmPosX);
			currentVM.setPosY(Data.vmPosY);
			canvas.getChildren().add(createVMNode(currentVM));
			Data.vmPosY += 150;
			//vmy=vmy+150;
		}
		
		// determines which x&y value for vm or hub is larger
		/*if (hubx > vmx) {
			maxX=hubx-150;
		} else {
			maxX=vmx-150;
		}
		if (huby > vmy) {
			maxY=huby-150;
		} else {
			maxY=vmy-150;
		}
		*/
		
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
