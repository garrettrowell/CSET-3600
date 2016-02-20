package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class MyController implements Initializable {
	@FXML
	TextArea textEditor;
	@FXML
	TabPane tabPane;
	@FXML
	Canvas canvas;
	@FXML
	AnchorPane graphAnchor;
	@FXML
	ScrollBar scrollX;
	@FXML
	ScrollBar scrollY;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Creates Listeners for Height and Width of the canvas
		graphAnchor.widthProperty().addListener(observable -> drawGraphical());
		graphAnchor.heightProperty().addListener(observable -> drawGraphical());
		//ScrollBar Listeners
		scrollX.valueProperty().addListener(observable -> scrollXAxis());
		scrollY.valueProperty().addListener(observable -> scrollYAxis());
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
		System.out.println("im scrolling x axis " + scrollX.getValue());
		drawGraphical();
	}
	
	@FXML
	private void scrollYAxis() {
		System.out.println("im scrolling y axis " + scrollY.getValue());
		drawGraphical();
	}
	
	@FXML
	private void drawGraphical() {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		//resize canvas to the width and height of the AnchorPane
		canvas.widthProperty().bind(graphAnchor.widthProperty());
		canvas.heightProperty().bind(graphAnchor.heightProperty());
		//Clear the canvas
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		System.out.println("Number of Vm's Present "+application.Data.vmMap.keySet().size());
		System.out.println("Number of Hub's Present "+application.Data.hubMap.keySet().size());
		
		int maxX=100;
		int maxY=100;
		
		//Draw a Blue rectangle for each hub
		int hubx = 50;
		int huby = 50;
		for(int i=0; i<=application.Data.hubMap.keySet().size()-1; i++){
			//hubx=hubx+100;
			//offsets each hub vertically so they don't overlap
			if (i>0) {
			  huby=huby+150;
			}
			gc.setFill(Color.BLUE);
			gc.fillRect(hubx-scrollX.getValue(), huby-scrollY.getValue(), 100, 100);

		}
		
		//Draw a Red rectangle for each vm
		int vmx = 200;
		int vmy = 50;
		for(int i=0; i<=application.Data.vmMap.keySet().size()-1; i++){
			//hubx=hubx+100;
			//offsets each vm vertically so they don't overlap
			if (i>0) {
				vmy=vmy+150;
			}
			gc.setFill(Color.RED);
			gc.fillRect(vmx-scrollX.getValue(), vmy-scrollY.getValue(), 100, 100);

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
		// Sets the max amount the scrollbar can recognize
		scrollX.setMax(maxX);
		scrollY.setMax(maxY);
		
	}
}
