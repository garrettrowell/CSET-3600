package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

public class FileOperations {

	public static String readFile(File inFile) {
		StringBuilder stringBuffer = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {

			bufferedReader = new BufferedReader(new FileReader(inFile));

			String text;
			while ((text = bufferedReader.readLine()) != null) {
				stringBuffer.append(text + System.getProperty("line.separator"));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return stringBuffer.toString();
	}

	public static void writeFile(File outFile, ObservableList<CharSequence> paragraph) {
		Iterator<CharSequence> iter = paragraph.iterator();
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(new File(outFile.toString())));
			while (iter.hasNext()) {
				CharSequence seq = iter.next();
				bf.append(seq);
				bf.newLine();
			}
			bf.flush();
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File fileOpenConf() {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);
		FileParser fileParser = new FileParser(selectedFile);
		return selectedFile;
	}

	public static File fileSaveAsConf() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"),
				new FileChooser.ExtensionFilter(".cfg", "*.cfg"));
		File selectedFile = fileChooser.showSaveDialog(null);
		return selectedFile;
	}

}
