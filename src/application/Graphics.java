package application;

import java.util.Map;

import org.controlsfx.control.PopOver;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Graphics {
	public static Node createHUBNode(HUB hubObject) {
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
				popOver.setDetachable(false);
				popOver.show(nodeContainer);
			}
			
		});
		
		return nodeContainer;
	}
	
	public static Node createVMNode(VM vmObject) {
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
				popOver.setDetachable(false);
				popOver.show(nodeContainer);
			}			
		});
				
		return nodeContainer;
	}
	
	public static VBox createHUBPopOverContent(HUB hubObject) {
		//the first row contains the big label and the toggle button
		HBox headerRow = new HBox(25);
		Label lname = new Label(hubObject.getName());
		lname.getStyleClass().add("popover-label");
		controller.MyController.btnEdit.setPrefSize(50, 10);
		headerRow.getChildren().addAll(lname,controller.MyController.btnEdit);
		
		//just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
		
		//this is the form within the popover (declare at top of class)
		controller.MyController.formPane.setId("controller.MyController.formPane");
		
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
//		HBox formRow4 = new HBox(15);
//		formRow4.getStyleClass().add("popover-form");
//		Label lformInf = new Label("Interfaces:");
//		lformInf.getStyleClass().add("popover-form-label");
//		TextField tfFormInf = new TextField();
//		tfFormInf.getStyleClass().add("popover-form-textfield-inactive");
	//	tfFormInf.setText(hubObject.getInf());
//		tfFormInf.setEditable(false);
//		formRow4.getChildren().addAll(lformInf,tfFormInf);
		
		//add all the rows to make them one form, but clear it first
		controller.MyController.formPane.getChildren().clear();
		controller.MyController.formPane.getChildren().addAll(formRow1, formRow2, formRow3);
		
		//this will dynamically add rows to the formPane base on the # of inf entries
		int count = 0;
		for(String inf : hubObject.getInf()) {
			HBox ethRow = new HBox(15);
			ethRow.getStyleClass().add("popover-form");
			Label lformEth = new Label();
			if (count==0 & hubObject.getInf().size() > 1){
				lformEth.setText("Infs:");
			} else if (count==0 & hubObject.getInf().size() == 0){
				
			}
			lformEth.getStyleClass().add("popover-form-label");
			TextField tfFormEth = new TextField();
			tfFormEth.getStyleClass().add("popover-form-textfield-inactive");
			tfFormEth.setText(inf);
			tfFormEth.setEditable(false);
			ethRow.getChildren().addAll(lformEth,tfFormEth);
			
			controller.MyController.formPane.getChildren().add(ethRow);
			count++;
		}
		
		//this content container is everything that is going to go on the PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
		
		//add all nodes (the headerRow, separator, and the form) into one container to be transfer to the PopOver
		content.getChildren().addAll(headerRow, hr, controller.MyController.formPane);
		
		return content;
	}
	
	public static VBox createVMPopOverContent(VM vmObject) {
		//the first row contains the big label and the toggle button
		HBox headerRow = new HBox(25);
		Label lname = new Label(vmObject.getName());
		lname.getStyleClass().add("popover-label");
		controller.MyController.btnEdit.setPrefSize(50, 10);
		headerRow.getChildren().addAll(lname, controller.MyController.btnEdit);
				
		//just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
				
		//this is the form within the popover (declare at top of class)
		controller.MyController.formPane.setId("formPane");
				
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
		controller.MyController.formPane.getChildren().clear();
		controller.MyController.formPane.getChildren().addAll(formRow1, formRow2, formRow3, formRow4);
		
		//this will dynamically add rows to the formPane base on the # of eth* entries
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
			
			controller.MyController.formPane.getChildren().add(ethRow);
		}
				
		//this content container is eveything that is going to go on the PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
				
		//add all nodes (the headerRow, separator, and the form) into one container to be transfer to the PopOver
		content.getChildren().addAll(headerRow, hr, controller.MyController.formPane);
				
		return content;
	}

}
