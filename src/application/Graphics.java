package application;

import java.util.Map;
import java.util.Optional;

import org.controlsfx.control.PopOver;

import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Graphics {
	private static Node createHUBNode(HUB hubObject, Pane canvas) {
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
				PopOver popOver = new PopOver(createHUBPopOverContent(hubObject, canvas));
				popOver.setDetachable(false);
				popOver.show(nodeContainer);
			}
			
		});
		
		return nodeContainer;
	}
	
	private static Node createVMNode(VM vmObject, Pane canvas) {
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
				PopOver popOver = new PopOver(createVMPopOverContent(vmObject, canvas));
				popOver.setDetachable(false);
				popOver.show(nodeContainer);
			}			
		});
				
		return nodeContainer;
	}
	
	private static void addRow(String labelText, String textFormText, Integer size, VBox content){
		HBox formRow = new HBox(size);
		formRow.getStyleClass().add("popover-form");
		Label rowLabel = new Label(labelText);
		rowLabel.getStyleClass().add("popover-form-label");
		TextField rowTF = new TextField();
		rowTF.getStyleClass().add("popover-form-textfield-inactive");
		rowTF.setText(textFormText);
		rowTF.setEditable(false);
		formRow.getChildren().addAll(rowLabel, rowTF);
		content.getChildren().add(formRow);
	}
	
	private static void addHeader(String labelText, ToggleButton btn, Integer size, VBox content){
		HBox headerRow = new HBox(size);
		Label lname = new Label(labelText);
		lname.getStyleClass().add("popover-label");
		btn.setPrefSize(50, 10);
		headerRow.getChildren().addAll(lname,btn);
		content.getChildren().add(headerRow);
	}
	
	public static void draw(Pane canvas) {
		System.out.println("Number of Vm's Present "+application.Data.vmMap.keySet().size());
		System.out.println("Number of Hub's Present "+application.Data.hubMap.keySet().size());
		// the pane should be cleared each time
		canvas.getChildren().clear();
		
		// we don't actually want to change the value of Data.hubStartPosY
		// instead we initially set our tempPosY to the startPos and alter that
		int tempPosY = Data.hubStartPosY;
		//Draw a Blue rectangle for each hub
		for(Map.Entry<String, HUB> entry : application.Data.hubMap.entrySet()) {
			String currentHubName = entry.getKey();
			HUB currentHub = application.Data.hubMap.get(currentHubName);
			currentHub.setPosX(Data.hubStartPosX);
			currentHub.setPosY(tempPosY);
			canvas.getChildren().add(application.Graphics.createHUBNode(currentHub, canvas));
			tempPosY += 150;
		}
		
		// we don't actually want to change the value of Data.vmStartPosY either
		tempPosY = Data.vmStartPosY;
		//Draw a Red rectangle for each vm
		for(Map.Entry<String, VM> entry : application.Data.vmMap.entrySet()) {
			String currentVMName = entry.getKey();
			VM currentVM = application.Data.vmMap.get(currentVMName);
			currentVM.setPosX(Data.vmStartPosX);
			currentVM.setPosY(tempPosY);
			canvas.getChildren().add(application.Graphics.createVMNode(currentVM, canvas));
			tempPosY += 150;
		}
	}
	
	private static void btnListener(VBox content, Optional<HUB> hubObject, Optional<VM> vmObject, Pane canvas){
		//Add ability to go into edit mode for the Textfield
		controller.MyController.btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//Query the popover setup schema to eventually find the Textfields and set the editable property based off the btnEdit
				int count = 0;
				for(Node node: content.getChildren()) {
					if(node instanceof HBox) {
						for(Node innerNode: ((HBox) node).getChildren()) {
							if(innerNode instanceof TextField) {
								((TextField) innerNode).editableProperty().bindBidirectional(controller.MyController.btnEdit.selectedProperty());
								if(controller.MyController.btnEdit.isSelected()) {
									innerNode.getStyleClass().remove("popover-form-textfield-inactive");
									innerNode.getStyleClass().add("popover-form-textfield-active");
								}
								if(!controller.MyController.btnEdit.isSelected()) {
									innerNode.getStyleClass().remove("popover-form-textfield-active");
									innerNode.getStyleClass().add("popover-form-textfield-inactive");
									if (hubObject.isPresent()){
										if (count == 0){
											hubObject.get().setName(((TextInputControl) innerNode).getText());
										} else if (count == 1){
											hubObject.get().setSubnet(((TextInputControl) innerNode).getText());
										} else if (count ==2){
											hubObject.get().setNetmask(((TextInputControl) innerNode).getText());
										} else {
											System.out.println("inf"+((TextInputControl) innerNode).getText());
										}
										draw(canvas);
							
									} else if (vmObject.isPresent()){
										if (count == 0){
											vmObject.get().setName(((TextInputControl) innerNode).getText());
										} else if (count == 1){
											vmObject.get().setOs(((TextInputControl) innerNode).getText());
										} else if (count ==2){
											vmObject.get().setVer(Double.parseDouble(((TextInputControl) innerNode).getText()));
										} else if (count==3){
											vmObject.get().setSrc(((TextInputControl) innerNode).getText());
										} else {
											System.out.println("inf"+((TextInputControl) innerNode).getText());
										}
										draw(canvas);
									}
									count++;
								}
							}
						}
					}
				}
			}
		});
	}
	
	private static VBox createHUBPopOverContent(HUB hubObject, Pane canvas) {		
		//this content container is everything that is going to go on the PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
		//the first row contains the big label and the toggle button
		addHeader(hubObject.getName(), controller.MyController.btnEdit, 25, content);
			
		//just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
		content.getChildren().add(hr);
		
		//the first row of the form (Hub name)
		addRow("Name: ", hubObject.getName(), 15, content);
		
		//Here is the same layout as row 1 (Hub subnet)
		addRow("Subnet:", hubObject.getSubnet(), 15, content);
		
		//Row 3 (Hub netmask)
		addRow("Netmask:", hubObject.getNetmask(), 15, content);
		
		//this will dynamically add rows to the formPane base on the # of inf entries
		int count = 0;
		for(String inf : hubObject.getInf()) {
			if (count==0 & hubObject.getInf().size() > 1){
				addRow("Infs:", inf, 15, content);
			} else if (count==0 & hubObject.getInf().size() == 1){
				addRow("Inf:", inf, 15, content);
			} else {
				addRow("", inf, 15, content);
			}
			count++;
		}
		btnListener(content, Optional.of(hubObject), Optional.empty(), canvas);
		return content;
	}
	
	private static VBox createVMPopOverContent(VM vmObject, Pane canvas) {
		//this content container is everything that is going to go on the PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
		//the first row contains the big label and the toggle button
		addHeader(vmObject.getName(), controller.MyController.btnEdit, 25, content);
				
		//just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
		content.getChildren().add(hr);
				
		//the first row of the form (VM name)
		//each row of the form contains a label and a Textfield
		addRow("Name: ", vmObject.getName(), 15, content);

		addRow("OS:", vmObject.getOs(), 15, content);
				
		//Row 3 (VM ver)
		addRow("Ver:", vmObject.getVer().toString(), 15, content);
				
		//Row 4 (VM src)
		addRow("Src:", vmObject.getSrc(), 15, content);

		for(Map.Entry<String, String> entry : vmObject.getInterfaceHashMap().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			addRow(key, value, 15, content);
		}
		btnListener(content,Optional.empty(),Optional.of(vmObject), canvas);
		return content;
	}

}
