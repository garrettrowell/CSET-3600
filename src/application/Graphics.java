package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.controlsfx.control.PopOver;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
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
				PopOver popOver = new PopOver(createHUBPopOverContent(hubObject, canvas));
				popOver.setDetachable(false);
				popOver.show(nodeContainer);
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
				PopOver popOver = new PopOver(createVMPopOverContent(vmObject, canvas));
				popOver.setDetachable(false);
				popOver.show(nodeContainer);
			}
		});

		return nodeContainer;
	}

	private static void addRow(String labelText, String textFormText, Integer size, VBox content) {
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

	private static void btnListener(VBox content, Optional<HUB> hubObject, Optional<VM> vmObject, Pane canvas) {
		// Add ability to go into edit mode for the Textfield
		controller.MyController.btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Query the popover setup schema to eventually find the
				// Textfields and set the editable property based off the
				// btnEdit

				// inf is used for hubs
				TreeSet<String> inf = new TreeSet<String>();
				// interfaces is used for vm's
				TreeMap<String, String> interfaces = new TreeMap<String, String>();

				int count = 0;
				for (Node node : content.getChildren()) {
					if (node instanceof HBox) {
						for (Node innerNode : ((HBox) node).getChildren()) {
							if (innerNode instanceof TextField) {
								((TextField) innerNode).editableProperty()
										.bindBidirectional(controller.MyController.btnEdit.selectedProperty());
								if (controller.MyController.btnEdit.isSelected()) {
									innerNode.getStyleClass().remove("popover-form-textfield-inactive");
									innerNode.getStyleClass().add("popover-form-textfield-active");
								}
								if (!controller.MyController.btnEdit.isSelected()) {
									innerNode.getStyleClass().remove("popover-form-textfield-active");
									innerNode.getStyleClass().add("popover-form-textfield-inactive");
									if (hubObject.isPresent()) {
										if (count == 0) {
											hubObject.get().setName(((TextField) innerNode).getText());
										} else if (count == 1) {
											hubObject.get().setSubnet(((TextField) innerNode).getText());
										} else if (count == 2) {
											hubObject.get().setNetmask(((TextField) innerNode).getText());
										} else {
											inf.add(((TextField) innerNode).getText());
											// System.out.println("inf" +
											// ((TextField)
											// innerNode).getText());
										}
										draw(canvas);

									} else if (vmObject.isPresent()) {
										if (count == 0) {
											vmObject.get().setName(((TextField) innerNode).getText());
										} else if (count == 1) {
											vmObject.get().setOs(((TextField) innerNode).getText());
										} else if (count == 2) {
											vmObject.get()
													.setVer(Double.parseDouble(((TextField) innerNode).getText()));
										} else if (count == 3) {
											vmObject.get().setSrc(((TextField) innerNode).getText());
										} else {
											// vmObject.get().setInterfaces(key,
											// value);
											// interfaces.put(key, value)
											System.out.println("inf" + ((TextField) innerNode).getText());
										}
										draw(canvas);
									}
									count++;
									if (hubObject.isPresent()) {
										hubObject.get().setInfs(inf);
									}
								}
							}
						}
					}
				}
			}
		});
	}

	private static void vmBtnListener(VBox content, VM vmObject, Pane canvas) {
		// Add ability to go into edit mode for the Textfield
		controller.MyController.btnEdit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Query the popover setup schema to eventually find the
				// Textfields and set the editable property based off the
				// btnEdit

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
						}
					}
				}
				vmObject.setInterfaces(interfaces);
				draw(canvas);
			}
		});
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
		addRow("Name: ", hubObject.getName(), 15, content);

		// Here is the same layout as row 1 (Hub subnet)
		addRow("Subnet:", hubObject.getSubnet(), 15, content);

		// Row 3 (Hub netmask)
		addRow("Netmask:", hubObject.getNetmask(), 15, content);

		// this will dynamically add rows to the formPane base on the # of inf
		// entries
		int count = 0;
		for (String inf : hubObject.getInfs()) {
			if (count == 0 & hubObject.getInfs().size() > 1) {
				addRow("Infs:", inf, 15, content);
			} else if (count == 0 & hubObject.getInfs().size() == 1) {
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
		addRow("Name: ", vmObject.getName(), 15, content);

		addRow("OS:", vmObject.getOs(), 15, content);

		// Row 3 (VM ver)
		addRow("Ver:", vmObject.getVer().toString(), 15, content);

		// Row 4 (VM src)
		addRow("Src:", vmObject.getSrc(), 15, content);

		for (Map.Entry<String, String> entry : vmObject.getInterfaces().entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			addRow(key, value, 15, content);
		}
		vmBtnListener(content, vmObject, canvas);
		// btnListener(content, Optional.empty(), Optional.of(vmObject),
		// canvas);
		return content;
	}

}
