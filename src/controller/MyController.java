package controller;

import java.io.File;

import application.FileOperations;
import application.FileWriter;
import application.Graphics;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MyController{
	@FXML
	TextArea textEditor;
	@FXML
	TabPane tabPane;
	@FXML
	Pane canvas;
	@FXML
	AnchorPane anchorPane, mainWindow;
	@FXML
	ScrollPane scrollPane;
	@FXML
	ContextMenu contextMenu;
	@FXML
	MenuItem deleteMenu;
	// These are the toggle button and the VBox container for the form
	public static ToggleButton btnEdit = new ToggleButton("Edit");
	public static Button btnAddInf = new Button("Add Infterface");
	public static File currentFile = null;

	@FXML
	private void fileClose() {
		Platform.exit();
	}
	
	@FXML
	private void fileNew() {
		FileOperations.newFile(textEditor, canvas, contextMenu);
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
		try {
			if(currentFile.exists()) {
				FileOperations.writeFile(currentFile, textEditor.getParagraphs());
			}else {
				fileSaveAs();
			}
		}catch(NullPointerException e) {
			System.out.println("Current file not set");
		}
	}
	
	@FXML
	private void tabEditor() {
		FileWriter.writeFile(textEditor);
	}
	
	@FXML
	private void tabGraphical() {
		application.Graphics.draw(canvas, contextMenu);
	}
	
	@FXML
	private void insertNewHub(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("/application/insertnewhubform.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			Stage formWindow = new Stage();
			formWindow.setScene(scene);
			formWindow.setTitle("Insert New Hub");
			formWindow.setResizable(false);
			formWindow.setFullScreen(false);
			
			//prevent the user from switching to the main GUI without closing the insert form first
			formWindow.initModality(Modality.WINDOW_MODAL);
			formWindow.initOwner(scrollPane.getScene().getWindow());
			formWindow.showAndWait();
			Graphics.draw(canvas, contextMenu);
			Graphics.draw(canvas, contextMenu);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@FXML
	private void insertNewVm(){
		try{
			Parent root = FXMLLoader.load(getClass().getResource("/application/insertnewvmform.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			Stage formWindow = new Stage();
			formWindow.setTitle("Insert New Vm");
			formWindow.setScene(scene);
			formWindow.setFullScreen(false);
			formWindow.setResizable(false);
			
			//prevent the user from switching to the main GUI without closing the insert form first
			formWindow.initModality(Modality.WINDOW_MODAL);
			formWindow.initOwner(scrollPane.getScene().getWindow());
			formWindow.showAndWait();
			Graphics.draw(canvas, contextMenu);
			Graphics.draw(canvas, contextMenu);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}