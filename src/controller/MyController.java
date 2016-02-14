package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class MyController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	TextArea textEditor;

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
}
