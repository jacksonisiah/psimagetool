package me.jackson.kissa;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;


public class MainView {
    public static Text message;

    @SuppressWarnings({"unchecked"})
    public static FlowPane getView() {

        // Warning Label
        message = new Text();
        message.setTextAlignment(TextAlignment.CENTER);

        // Console type
        Label consoleText = new Label("Console type to image:");
        ComboBox consoleBox = new ComboBox();
        consoleBox.getItems().add("PlayStation 4");
        consoleBox.getItems().add("PlayStation 5");
        HBox console = new HBox(10, consoleText, consoleBox);
        console.setAlignment(Pos.CENTER);

        // USB Drive
        Label usbText = new Label("USB Drive:");
        ComboBox usbBox = new ComboBox();
        for (File drive: USBDevice.getUSBDevices()) {
            usbBox.getItems().add(drive.toString());
        }
        HBox usb = new HBox(10, usbText, usbBox);
        usb.setAlignment(Pos.CENTER);

        Button button = new Button("Create Media");

        button.setOnAction(actionEvent ->  {
            String usbVal = (String) usbBox.getValue();
            String conVal = (String) consoleBox.getValue();

            if (usbVal == null || conVal == null) {
                message.setText("Console or USB is not selected.");
                message.setFill(Color.RED);
            }
            else {
                var alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Potentially Destructive Action Ahead");
                alert.setHeaderText(null);
                alert.getButtonTypes().addAll(ButtonType.CANCEL);
                alert.setContentText("Your data WILL be destroyed and the device will be formatted. Are you sure you want to continue?");
                alert.showAndWait().ifPresent((btnType) -> {
                    if (btnType == ButtonType.OK) {
                        button.setDisable(true);
                        var status = PlayStation.createMedia(usbVal, conVal);
                        message.setText(status);
                        button.setDisable(false);
                    } else if (btnType == ButtonType.CANCEL) {
                        // do nothing
                    }
                });
            }
        });

        HBox media = new HBox(button);
        media.setAlignment(Pos.CENTER);

        Button infobutton = new Button("Info");
        infobutton.setOnAction(actionEvent -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Copyright (c) 2022 Jackson Isaiah." +
                "\nThis software is provided under the terms of the MIT Licence. For more information, see the LICENSE file provided with this software." +
                "\n\nPlayStation®, PlayStation®4 System Software and PlayStation®5 System Software are trademarks or registered trademarks of Sony Interactive Entertainment Inc. or its affiliates in the United States and other countries.");
            alert.showAndWait();
        });

        HBox info = new HBox(infobutton);
        info.setAlignment(Pos.BOTTOM_RIGHT);

        FlowPane pane = new FlowPane(message, console, usb, media, info);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setOrientation(Orientation.VERTICAL);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }
}
