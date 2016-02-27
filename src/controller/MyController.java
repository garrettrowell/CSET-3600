package controller;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import application.Data;
import application.HUB;
import application.VM;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Creates Listeners for Height and Width of the canvas
		//anchorPane.widthProperty().addListener(observable -> drawGraphical());
		//anchorPane.heightProperty().addListener(observable -> drawGraphical());
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
		/*
		canvas.widthProperty().bind(graphAnchor.widthProperty());
		canvas.heightProperty().bind(graphAnchor.heightProperty());
		*/
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
	}
}
