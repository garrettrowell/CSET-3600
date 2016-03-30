package application;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.PopOver;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Graphics {

	private static Node createHUBNode(HUB hubObject, Pane canvas) {
		// each hub is represented by a blue rectangle
		Rectangle node = new Rectangle(Data.nodeLength, Data.nodeWidth);
		node.setFill(Color.BLUE);

		// the hub name in the rectangle
		Label lnodeName = new Label(hubObject.getName());

		// this stackpane stack the label on top of the rectangle to make them
		// one entity
		StackPane nodeContainer = new StackPane();
		nodeContainer.getChildren().addAll(node, lnodeName);
		nodeContainer.relocate(hubObject.getPosX(), hubObject.getPosY());

		// add a popover when one of the hub is clicked
		nodeContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY) {
					PopOver popOver = new PopOver(createHUBPopOverContent(hubObject, canvas));
					popOver.setDetachable(false);
					popOver.show(nodeContainer);
				}
				
			}

		});

		return nodeContainer;
	}

	private static Node createVMNode(VM vmObject, Pane canvas) {
		// each vm is represented by a red rectangle
		Rectangle node = new Rectangle(Data.nodeLength, Data.nodeWidth);
		node.setFill(Color.RED);

		// the hub name in the rectangle
		Label lnodeName = new Label(vmObject.getName());

		// this stackpane stack the label on top of the rectangle to make them
		// one entity
		StackPane nodeContainer = new StackPane();
		nodeContainer.getChildren().addAll(node, lnodeName);
		nodeContainer.relocate(vmObject.getPosX(), vmObject.getPosY());

		// add a popover when one of the hub is clicked
		nodeContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY) {
					PopOver popOver = new PopOver(createVMPopOverContent(vmObject, canvas));
					popOver.setDetachable(false);
					popOver.show(nodeContainer);
				}
			}
		});

		return nodeContainer;
	}

	private static void addRow(String labelText, String textFormText, Integer size, VBox content, boolean addToEnd) {
		HBox formRow = new HBox(size);
		formRow.getStyleClass().add("popover-form");
		Label rowLabel = new Label(labelText);
		rowLabel.getStyleClass().add("popover-form-label");
		TextField rowTF = new TextField();
		rowTF.getStyleClass().add("popover-form-textfield-inactive");
		rowTF.setText(textFormText);
		rowTF.setEditable(false);
		
		//do you just add it to the form for befor the row with the button
		if(addToEnd){
			formRow.getChildren().addAll(rowLabel,rowTF);
			content.getChildren().add(content.getChildren().size() - 1,formRow);
		}else{
			formRow.getChildren().addAll(rowLabel, rowTF);
			content.getChildren().add(formRow);
		}
	}

	private static void addHeader(String labelText, ToggleButton btn, Integer size, VBox content) {
		HBox headerRow = new HBox(size);
		Label lname = new Label(labelText);
		lname.getStyleClass().add("popover-label");
		btn.setPrefSize(50, 10);
		headerRow.getChildren().addAll(lname, btn);
		content.getChildren().add(headerRow);
	}

	public static void draw(Pane canvas) {
		System.out.println("Number of Vm's Present " + application.Data.vmMap.keySet().size());
		System.out.println("Number of Hub's Present " + application.Data.hubMap.keySet().size());
		// the pane should be cleared each time
		canvas.getChildren().clear();

		// we don't actually want to change the value of Data.hubStartPosY
		// instead we initially set our tempPosY to the startPos and alter that
		int tempPosY = Data.hubStartPosY;
		// Draw a Blue rectangle for each hub
		for (Map.Entry<String, HUB> entry : application.Data.hubMap.entrySet()) {
			String currentHubName = entry.getKey();
			HUB currentHub = application.Data.hubMap.get(currentHubName);
			currentHub.setPosX(Data.hubStartPosX);
			currentHub.setPosY(tempPosY);
			canvas.getChildren().add(application.Graphics.createHUBNode(currentHub, canvas));
			tempPosY += 150;
		}

		// we don't actually want to change the value of Data.vmStartPosY either
		tempPosY = Data.vmStartPosY;
		// Draw a Red rectangle for each vm
		for (Map.Entry<String, VM> entry : application.Data.vmMap.entrySet()) {
			String currentVMName = entry.getKey();
			VM currentVM = application.Data.vmMap.get(currentVMName);
			currentVM.setPosX(Data.vmStartPosX);
			currentVM.setPosY(tempPosY);
			canvas.getChildren().add(application.Graphics.createVMNode(currentVM, canvas));
			tempPosY += 150;
		}
	}

	private static void vmBtnListener(VBox content, VM vmObject, Pane canvas) {
		// Add ability to go into edit mode for the Textfield
		controller.MyController.btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Query the popover setup schema to eventually find the
				// Textfields and set the editable property based off the
				// btnEdit
				if (event.getButton() == MouseButton.PRIMARY) {
					// interfaces is used to update the vm's interfaces
					TreeMap<String, String> interfaces = new TreeMap<String, String>();
					for (Node node : content.getChildren()) {
						// System.out.println(node);
						if (node instanceof HBox) {

							ObservableList<Node> childNode = ((HBox) node).getChildren();
							for (int i = 0; i < childNode.size(); i++) {
								if (childNode.get(i) instanceof TextField) {
									((TextField) childNode.get(i)).editableProperty()
											.bindBidirectional(controller.MyController.btnEdit.selectedProperty());
									if (controller.MyController.btnEdit.isSelected()) {
										System.out.println("Edit Mode");
										((TextField) childNode.get(i)).getStyleClass()
												.remove("popover-form-textfield-inactive");
										((TextField) childNode.get(i)).getStyleClass().add("popover-form-textfield-active");
									} else if (!controller.MyController.btnEdit.isSelected()) {
										((TextField) childNode.get(i)).getStyleClass()
												.remove("popover-form-textfield-active");
										((TextField) childNode.get(i)).getStyleClass()
												.add("popover-form-textfield-inactive");
										if (childNode.get(i - 1) instanceof Label) {
											if (((Label) childNode.get(i - 1)).getText().matches("Name.*")) {
												vmObject.setName(((TextField) childNode.get(i)).getText());
											} else if (((Label) childNode.get(i - 1)).getText().matches("OS.*")) {
												vmObject.setOs(((TextField) childNode.get(i)).getText());
											} else if (((Label) childNode.get(i - 1)).getText().matches("Ver.*")) {
												vmObject.setVer(
														Double.parseDouble(((TextField) childNode.get(i)).getText()));
											} else if (((Label) childNode.get(i - 1)).getText().matches("Src.*")) {
												vmObject.setSrc(((TextField) childNode.get(i)).getText());
											} else if (((Label) childNode.get(i - 1)).getText().matches("\\w+?\\d+?.*")) {
												interfaces.put(((Label) childNode.get(i - 1)).getText(),
														((TextField) childNode.get(i)).getText());
											}
										}
									}
								}
								
								if(childNode.get(i) instanceof Button) {
									((Button) childNode.get(i)).disableProperty()
										.bindBidirectional(controller.MyController.btnEdit.selectedProperty());
								}
							}
						}
					}
					vmObject.setInterfaces(interfaces);
					draw(canvas);
				}
				
			}
		});
	}

	private static void hubBtnListener(VBox content, HUB hubObject, Pane canvas) {
		// Add ability to go into edit mode for the Textfield
		controller.MyController.btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Query the popover setup schema to eventually find the
				// Textfields and set the editable property based off the
				// btnEdit

				// inf is used to update the hub's interfaces
				TreeSet<String> inf = new TreeSet<String>();
				for (Node node : content.getChildren()) {
					// System.out.println(node);
					if (node instanceof HBox) {

						ObservableList<Node> childNode = ((HBox) node).getChildren();
						for (int i = 0; i < childNode.size(); i++) {
							if (childNode.get(i) instanceof TextField) {
								((TextField) childNode.get(i)).editableProperty()
										.bindBidirectional(controller.MyController.btnEdit.selectedProperty());
								if (controller.MyController.btnEdit.isSelected()) {
									System.out.println("Edit Mode");
									((TextField) childNode.get(i)).getStyleClass()
											.remove("popover-form-textfield-inactive");
									((TextField) childNode.get(i)).getStyleClass().add("popover-form-textfield-active");
								} else if (!controller.MyController.btnEdit.isSelected()) {
									((TextField) childNode.get(i)).getStyleClass()
											.remove("popover-form-textfield-active");
									((TextField) childNode.get(i)).getStyleClass()
											.add("popover-form-textfield-inactive");
									if (childNode.get(i - 1) instanceof Label) {
										if (((Label) childNode.get(i - 1)).getText().matches("Name.*")) {
											hubObject.setName(((TextField) childNode.get(i)).getText());
										} else if (((Label) childNode.get(i - 1)).getText().matches("Subnet.*")) {
											hubObject.setSubnet(((TextField) childNode.get(i)).getText());
										} else if (((Label) childNode.get(i - 1)).getText().matches("Ver.*")) {
											hubObject.setSubnet(((TextField) childNode.get(i)).getText());
										} else if (((Label) childNode.get(i - 1)).getText().matches("Inf.*")) {
											inf.add(((TextField) childNode.get(i)).getText());
										}
									}
								}
							}
						}
					}
				}
				hubObject.setInfs(inf);
				draw(canvas);
			}
		});
	}
	
	private static void addInterfacesPopoverBtnListener(VBox content) {
		//btn listener for the "Add Interfaces" button on the popovers
		ObservableList<Node> popOverContent = content.getChildren();
		String ethRegex = "(\\w+?)(\\d+?.*)";
		Pattern pattern = Pattern.compile(ethRegex);
		int nextInterfaceLabel = 0;
		//determine what is the index tagged at the last eht# on the form
		for(Node node : popOverContent) {
			if(node instanceof HBox){
				for(Node innerNode : ((HBox) node).getChildren()){
					if( innerNode instanceof Label) {
						String nodeLabel = ((Label)innerNode).getText();
						if(nodeLabel.matches(ethRegex)) {
							Matcher matcher = pattern.matcher(nodeLabel);
							if(matcher.find()) {
								nextInterfaceLabel = Integer.parseInt(matcher.group(2)) + 1;
							}
						}
					}
				}
			}
			
		}
		addRow("eth" + String.valueOf(nextInterfaceLabel) , "", 15, content, true);
		
	}
	
	private static VBox createHUBPopOverContent(HUB hubObject, Pane canvas) {
		// this content container is everything that is going to go on the
		// PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
		// the first row contains the big label and the toggle button
		addHeader(hubObject.getName(), controller.MyController.btnEdit, 25, content);

		// just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
		content.getChildren().add(hr);

		// the first row of the form (Hub name)
		addRow("Name: ", hubObject.getName(), 15, content, false);

		// Here is the same layout as row 1 (Hub subnet)
		addRow("Subnet:", hubObject.getSubnet(), 15, content, false);

		// Row 3 (Hub netmask)
		addRow("Netmask:", hubObject.getNetmask(), 15, content, false);

		// this will dynamically add rows to the formPane base on the # of inf
		// entries
		for (String inf : hubObject.getInfs()) {
			addRow("Inf:", inf, 15, content, false);

		}
		hubBtnListener(content, hubObject, canvas);
		return content;
	}

	private static VBox createVMPopOverContent(VM vmObject, Pane canvas) {
		// this content container is everything that is going to go on the
		// PopOver
		VBox content = new VBox(5);
		content.getStyleClass().add("popover-content");
		content.setId("contentPane");
		// the first row contains the big label and the toggle button
		addHeader(vmObject.getName(), controller.MyController.btnEdit, 25, content);

		// just a line separator
		Separator hr = new Separator(Orientation.HORIZONTAL);
		hr.minWidth(Control.USE_COMPUTED_SIZE);
		content.getChildren().add(hr);

		// the first row of the form (VM name)
		// each row of the form contains a label and a Textfield
		addRow("Name: ", vmObject.getName(), 15, content, false);

		addRow("OS:", vmObject.getOs(), 15, content, false);

		// Row 3 (VM ver)
		addRow("Ver:", vmObject.getVer().toString(), 15, content, false);

		// Row 4 (VM src)
		addRow("Src:", vmObject.getSrc(), 15, content, false);

		for (Map.Entry<String, String> entry : vmObject.getInterfaces().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			addRow(key, value, 15, content, false);
		}
		vmBtnListener(content, vmObject, canvas);
		addButtonRow("Add Interfaces", content);
		return content;
	}

	private static void addButtonRow(String btnLabel, VBox content) {
		//add a row with a button to popovers
		HBox btnRow = new HBox();
		btnRow.setId("btnRow");
		btnRow.getStyleClass().add("popover-form-buttonRow");
		Button button = new Button(btnLabel);
		button.getStyleClass().add("popover-form-button-enable");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY) {
					addInterfacesPopoverBtnListener(content);
				}
			}
		});
		btnRow.getChildren().add(button);
		content.getChildren().add(btnRow);
	}
	
	public static void addInfRow(String labelText, VBox content) {
		//this one is used for adding rows in the Insert New VM Form
		HBox formRow = new HBox();
		formRow.getStyleClass().add("vmform-infRow");
		Label rowLabel = new Label(labelText);
		rowLabel.getStyleClass().add("vmform-label");
		TextField rowTF = new TextField();
		formRow.getChildren().addAll(rowLabel, rowTF);
		content.getChildren().add(formRow);
	}
}
