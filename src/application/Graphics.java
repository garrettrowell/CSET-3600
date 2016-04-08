package application;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.controlsfx.control.PopOver;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
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
import javafx.stage.WindowEvent;

public class Graphics {

	private static Node createHUBNode(HUB hubObject, Pane canvas, ContextMenu contextMenu) {
		// each hub is represented by a blue rectangle
		Rectangle node = new Rectangle(Data.nodeLength, Data.nodeWidth);
		node.setFill(Color.AQUAMARINE);

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
					PopOver popOver = new PopOver(createHUBPopOverContent(hubObject, canvas, contextMenu));
					popOver.setDetachable(false);
					popOver.show(nodeContainer);
					popOver.setOnHiding(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent e) {
							hubNodeHidingListener(popOver, hubObject, canvas, contextMenu);
						}
					});
				}
				if(event.getButton() == MouseButton.SECONDARY) {
					//if user right click on a HUB then enable the "delete" menu
					contextMenu.getItems().get(2).setDisable(false);
					//when user select an object to delete
					contextMenu.getItems().get(2).setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							String hubName = hubObject.getName();
							TreeSet<String> interfaces = hubObject.getInfs();
							for(String inf : interfaces) {
								Pattern pat = Pattern.compile(Data.hubInfPattern);
								Matcher matcher = pat.matcher(inf);
								//delete the VM interface that is connected to the hub
								if(matcher.find()) {
									String vmName = matcher.group(1);
									String vmEth = matcher.group(2);
									Data.vmMap.get(vmName).removeInf(vmEth);
								}
							}
							//delete the hub
							Data.hubMap.remove(hubName);
							draw(canvas, contextMenu);
						}
					});
					//disable the "delete option when the popover closes"
					contextMenu.setOnHidden(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent e) {
							contextMenu.getItems().get(2).setDisable(true);
						}
					});
				}
			}
		});

		return nodeContainer;
	}

	private static Node createVMNode(VM vmObject, Pane canvas, ContextMenu contextMenu) {
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
					PopOver popOver = new PopOver(createVMPopOverContent(vmObject, canvas, contextMenu));
					popOver.setDetachable(false);
					popOver.show(nodeContainer);
					popOver.setOnHiding(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent e) {
							vmNodeHidingListener(popOver, vmObject, canvas, contextMenu);
						}
					});
				}else if(event.getButton() == MouseButton.SECONDARY) {
					System.out.println(Data.vmMap);
					//if user right click on a HUB then enable the "delete" menu
					contextMenu.getItems().get(2).setDisable(false);
					//when user select an object to delete
					contextMenu.getItems().get(2).setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							System.out.println("Delete is clicked");
							String vmName = vmObject.getName();
							TreeMap<String, String> interfaces = vmObject.getInterfaces();
							LinkedHashMap<String, HUB> tempHubMap = Data.hubMap;
							//look in every hubObject
							for(Map.Entry<String, HUB> hubEntry : tempHubMap.entrySet()) {
								TreeSet<String> hubInf = tempHubMap.get(hubEntry.getValue().getName()).getInfs();
								Pattern pat = Pattern.compile(Data.hubInfPattern);
								boolean deleteInf = false;
								ArrayList<String> toRemove = new ArrayList<String>();
								//look in each hubInf
								for(String inf : hubInf) {
									Matcher matcher = pat.matcher(inf);
									if(matcher.find()) {
										if(matcher.group(1).toLowerCase().equals(vmName.toLowerCase()) /*&&
											hubInf.contains(matcher.group(2))*/) {
											toRemove.add(inf);
											deleteInf = true;
										}
									}
								}
								System.out.println(toRemove);
								if(deleteInf == true){
									for(String item : toRemove){
										Data.hubMap.get(hubEntry.getValue().getName()).removeInf(item);
									}
								}
							}
							//delete the vm
							Data.vmMap.remove(vmName);
							draw(canvas, contextMenu);
							System.out.println(Data.vmMap);
						}
					});
					//disable the "delete option when the popover closes"
					contextMenu.setOnHidden(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent e) {
							contextMenu.getItems().get(2).setDisable(true);
						}
					});
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
		//this is so everytime user closes popover in edit, and when user revisits it popover is
		//in edit mode
		btn.setSelected(false);
		headerRow.getChildren().addAll(lname, btn);
		content.getChildren().add(headerRow);
	}

	public static void draw(Pane canvas, ContextMenu contextMenu) {
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
			canvas.getChildren().add(application.Graphics.createHUBNode(currentHub, canvas, contextMenu));
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
			canvas.getChildren().add(application.Graphics.createVMNode(currentVM, canvas, contextMenu));
			tempPosY += 150;
		}
	}

	private static void vmBtnListener(VBox content, VM vmObject, Pane canvas, ContextMenu contextMenu) {
		// Add ability to go into edit mode for the Textfield
		controller.MyController.btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Query the popover setup schema to eventually find the
				// Textfields and set the editable property based off the
				// btnEdit
				if (event.getButton() == MouseButton.PRIMARY) {
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
									}
								}
								
								if(childNode.get(i) instanceof Button) {
									((Button) childNode.get(i)).disableProperty()
										.bindBidirectional(controller.MyController.btnEdit.selectedProperty());
								}
							}
						}
					}
					draw(canvas, contextMenu);
				}
				
			}
		});
	}
	
	private static void vmNodeHidingListener(PopOver popover, VM vmObject, Pane canvas, ContextMenu contextMenu) {
		try{
			//eveything in the popover
			VBox contentPane = (VBox) popover.getContentNode();
			//find the header row
			HBox headerRow = (HBox) contentPane.getChildren().get(0);
			//find the toggle button
			ToggleButton toggleBtn = (ToggleButton) headerRow.getChildren().get(1);
			//the old key used to find the correlating vmObject
			VM oldVM = vmObject;
			
			//only update with it's not in edit mode
			if(!toggleBtn.isSelected()) {
				VM newVmObject = new VM();
				TreeMap<String, String> newInterfaces = new TreeMap<String, String>();
				for(Node row : contentPane.getChildren()) {
					if(row instanceof HBox) {
						ObservableList<Node> childNode = ((HBox) row).getChildren();
						for(int i = 0; i < childNode.size(); i++) {
							if(childNode.get(i) instanceof Label) {
								if(((Label) childNode.get(i)).getText().matches("Name.*")) {
									String vmName = ((TextField) childNode.get(i + 1)).getText();
									//only validate name if it's different than the current one
									if(!oldVM.getName().equals(vmName)) {
										//make sure name input is a valid name
										if(Validator.validateName(vmName)){
											newVmObject.setName(vmName);
										}else {
											//if input is not valid, warn user and keep the old one
											creatAlert(((Label)childNode.get(i)).getText(), "VM");
											newVmObject.setName(oldVM.getName());
										}
									}else {
										//set it to old name if it did not change
										newVmObject.setName(oldVM.getName());
									}
								//same general idea goes for the rest of the labels
								//if input is different
								//then test it's validation before making changes
									//if it's not valid then warn the user
								//else just set it to the old one
								}else if(((Label) childNode.get(i)).getText().matches("OS.*")) {
									String vmOs = ((TextField) childNode.get(i + 1)).getText();
									if(Validator.validateOs(vmOs)) {
										newVmObject.setOs(vmOs);
									}else {
										creatAlert(((Label)childNode.get(i)).getText(), "VM");
										newVmObject.setOs(oldVM.getOs());
									}
								}else if(((Label) childNode.get(i)).getText().matches("Ver.*")) {
									String vmVer = ((TextField) childNode.get(i + 1)).getText();
									if(Validator.validateVer(vmVer)) {
										newVmObject.setVer(Double.parseDouble(vmVer));
									}else {
										creatAlert(((Label)childNode.get(i)).getText(), "VM");
										newVmObject.setVer(oldVM.getVer());
									}
								}else if(((Label) childNode.get(i)).getText().matches("Src.*")) {
									String vmSrc = ((TextField) childNode.get(i + 1)).getText();
									if(Validator.validateSrc(vmSrc)) {
										newVmObject.setSrc(vmSrc);
									}else {
										creatAlert(((Label)childNode.get(i)).getText(), "VM");
										newVmObject.setSrc(oldVM.getSrc());
									}
								} else if (((Label) childNode.get(i)).getText().matches("(\\w+?).(\\w+?\\d+?.*)")) {
									String ipLabel = ((Label) childNode.get(i)).getText();
									String vmIp = ((TextField) childNode.get(i + 1)).getText().trim();
									//does the eth# interface already exist?
									if(oldVM.getInterfaces().containsKey(ipLabel)) {
										//is the new ip value equal to the old one
										if(!oldVM.getInterfaces().get(ipLabel).equals(vmIp)) {
											//if it's a new ip, validate and set it
											if(Validator.validateIp(vmIp)) {
												newInterfaces.put(ipLabel, vmIp);
											}else {
												//if not, alert user and set to old ip
												creatAlert(ipLabel, "VM");
												newInterfaces.put(ipLabel, oldVM.getInterfaces().get(ipLabel));
											}
										}else {
											//if ip value didn't change just set to old value
											newInterfaces.put(ipLabel, oldVM.getInterfaces().get(ipLabel));
										}
									}else {
										//if that eth# doesn't exist yet
										//validate it and set it
										//only set the new eth# if the new ip value is valid
										if(!vmIp.isEmpty()) {
											if(Validator.validateIp(vmIp)) {
												newInterfaces.put(ipLabel, vmIp);
											}else {
												//tell user there was an error and don't insert the new eth# interface
												creatAlert(ipLabel, "VM");
											}
										}
									}
								}
							}
						}
					}
				}
				newVmObject.setInterfaces(newInterfaces);
				Data.vmMap.replace(oldVM.getName(), newVmObject);
				//here we don't want to simply delete old entry because of the coordinates
				//so we update the key to a different key if they change the VM Object name
				LinkedHashMap<String, VM> updatedMap = Data.replaceVMKey(Data.vmMap, oldVM.getName(), newVmObject.getName());
				Data.vmMap = updatedMap;
				draw(canvas, contextMenu);
			}
		}catch(IndexOutOfBoundsException e) {
			System.out.println("Something went Wrong");
		}
	}
	
	private static void hubBtnListener(VBox content, HUB hubObject, Pane canvas, ContextMenu contextMenu) {
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
							if(childNode.get(i) instanceof Button) {
								((Button) childNode.get(i)).disableProperty()
									.bindBidirectional(controller.MyController.btnEdit.selectedProperty());
							}
						}
					}
				}
				hubObject.setInfs(inf);
				draw(canvas, contextMenu);
			}
		});
	}
	
	private static void hubNodeHidingListener(PopOver popover, HUB hubObject, Pane canvas, ContextMenu contextMenu) {
		try{
			//eveything in the popover
			VBox contentPane = (VBox) popover.getContentNode();
			//find the header row
			HBox headerRow = (HBox) contentPane.getChildren().get(0);
			//find the toggle button
			ToggleButton toggleBtn = (ToggleButton) headerRow.getChildren().get(1);
			//the old key used to find the correlating vmObject
			HUB oldHub = hubObject;
					
			//only update with it's not in edit mode
			if(!toggleBtn.isSelected()) {
				HUB newHubObject = new HUB();
				TreeSet<String> newInterfaces = new TreeSet<String>();
				for(Node row : contentPane.getChildren()) {
					if(row instanceof HBox) {
						ObservableList<Node> childNode = ((HBox) row).getChildren();
						for(int i = 0; i < childNode.size(); i++) {
							if(childNode.get(i) instanceof Label) {
								if(((Label) childNode.get(i)).getText().matches("Name.*")) {
									String hubName = ((TextField) childNode.get(i + 1)).getText();
									//only validate name if it's different than the current one
									if(!oldHub.getName().equals(hubName)) {
										//make sure name input is a valid name
										if(Validator.validateName(hubName)){
											newHubObject.setName(hubName);
										}else {
											//if input is not valid, warn user and keep the old one
											creatAlert(((Label)childNode.get(i)).getText(), "HUB");
											newHubObject.setName(oldHub.getName());
												}
											}else {
												//set it to old name if it did not change
												newHubObject.setName(oldHub.getName());
											}
										//same general idea goes for the rest of the labels
										//if input is different
										//then test it's validation before making changes
											//if it's not valid then warn the user
										//else just set it to the old one
										}else if(((Label) childNode.get(i)).getText().matches("Subnet.*")) {
											String hubSubnet = ((TextField) childNode.get(i + 1)).getText();
											if(!hubObject.getSubnet().equals(hubSubnet)) {
												if(Validator.validateIp(hubSubnet)) {
													newHubObject.setSubnet(hubSubnet);
												}else {
													creatAlert(((Label)childNode.get(i)).getText(), "HUB");
													newHubObject.setSubnet(oldHub.getSubnet());
												}
											}else {
												newHubObject.setSubnet(oldHub.getSubnet());
											}
										}else if(((Label) childNode.get(i)).getText().matches("Netmask.*")) {
											String hubNetmask = ((TextField) childNode.get(i + 1)).getText();
											if(Validator.validateNetmask(hubNetmask)) {
												newHubObject.setNetmask(hubNetmask);
											}else {
												creatAlert(((Label)childNode.get(i)).getText(), "HUB");
												newHubObject.setNetmask(oldHub.getNetmask());
											}
										} else if (((Label) childNode.get(i)).getText().matches("Inf.*")) {
											String infValue = ((TextField) childNode.get(i + 1)).getText();
											if(!infValue.isEmpty()) {
												if(!oldHub.getInfs().contains(infValue)) {
													if(Validator.validateHubInf(infValue)) {
														newInterfaces.add(infValue);
													}else {
														creatAlert("Inf.", "HUB");
													}
												}else {
													newInterfaces.add(infValue);
												}
											}
										}
									}
								}
							}
						}
						newHubObject.setInfs(newInterfaces);
						Data.hubMap.replace(oldHub.getName(), newHubObject);
						//here we don't want to simply delete old entry because of the coordinates
						//so we update the key to a different key if they change the Hub Object name
						LinkedHashMap<String, HUB> updatedMap = Data.replaceHUBKey(Data.hubMap, oldHub.getName(), newHubObject.getName());
						Data.hubMap = updatedMap;
						draw(canvas, contextMenu);
					}
		}catch(IndexOutOfBoundsException e){
			System.out.println("Something went wrong");
		}
	}
	
	private static VBox createHUBPopOverContent(HUB hubObject, Pane canvas, ContextMenu contextMenu) {
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
		hubBtnListener(content, hubObject, canvas, contextMenu);
		addButtonRow("Add Interfaces", content, "hub");
		return content;
	}

	private static VBox createVMPopOverContent(VM vmObject, Pane canvas, ContextMenu contextMenu) {
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
		vmBtnListener(content, vmObject, canvas, contextMenu);
		addButtonRow("Add Interfaces", content, "vm");
		return content;
	}

	private static void addButtonRow(String btnLabel, VBox content, String type) {
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
					ObservableList<Node> popOverContent = content.getChildren();
					String ethRegex = "(\\w+?)(\\d+?.*)";
					Pattern ethPattern = Pattern.compile(ethRegex);
					String nextInterfaceLabel = "";
					//determine what is the index tagged at the last eht# on the form
					for(Node node : popOverContent) {
						if(node instanceof HBox){
							for(Node innerNode : ((HBox) node).getChildren()){
								if( innerNode instanceof Label) {
									String nodeLabel = ((Label)innerNode).getText();
									if(nodeLabel.matches(ethRegex) && type.trim().toLowerCase().equals("hub")) {
										Matcher matcher = ethPattern.matcher(nodeLabel);
										if(matcher.find()) {
											nextInterfaceLabel = "eth" + String.valueOf(Integer.parseInt(matcher.group(2)) + 1);
										}else {
											nextInterfaceLabel = "eth0";
										}
									}else if(!nodeLabel.matches(ethRegex) && type.trim().equals("hub")){
										nextInterfaceLabel = "eth0";
									}else if(type.trim().toLowerCase().equals("vm")) {
										
									}
								}
							}
						}
						
					}
					addRow(nextInterfaceLabel, "", 15, content, true);
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
	
	private static void creatAlert(String input, String type ) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(input + " Input Error");
		alert.setHeaderText("Interface Error");
		alert.setContentText("Please check over your '"+ input +"' input parameter. The "+ type + " " + input +" will not be saved.");
		alert.show();
	}
}
