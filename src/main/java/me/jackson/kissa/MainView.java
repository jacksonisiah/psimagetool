package me.jackson.kissa;

import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;


public class MainView {
    public static FlowPane getView() {
        // region Warning Label
        var message = new Text();
        message.setTextAlignment(TextAlignment.CENTER);
        // endregion

        // region Console type
        Label consoleText = new Label("Console type to image:");
        List<String> consoles = new ArrayList<>() {{
            add("PlayStation 4");
            add("PlayStation 5");
        }};
        var consoleBox = new ComboBox<>(FXCollections.observableList(consoles));
        HBox console = new HBox(10, consoleText, consoleBox);
        console.setAlignment(Pos.CENTER);
        // endregion

        // region USB Drive
        Label usbText = new Label("USB Drive:");
        var usbBox = new ComboBox<>(FXCollections.observableList(USBDevice.getUSBDevices()));
        HBox usb = new HBox(10, usbText, usbBox);
        usb.setAlignment(Pos.CENTER);
        // endregion

        // region Button
        Button button = new Button("Create Media");

        button.setOnAction(actionEvent ->  {
            String usbVal = usbBox.getValue();
            String conVal = consoleBox.getValue();

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
                        message.setText("Creating media (this may take a while)");
                        var status = PlayStation.createMedia(usbVal, conVal);
                        message.setText(status);
                        button.setDisable(false);
                    } else if (btnType == ButtonType.CANCEL) {/*do nothing*/}
                });
            }
        });
        // endregion

        // region InfoButton
        HBox media = new HBox(button);
        media.setAlignment(Pos.CENTER);

        Button infoButton = new Button("Info");
        infoButton.setOnAction(actionEvent -> {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("""
                Copyright (c) 2022 Jackson Isaiah.
                This software is provided under the MIT Licence. For more information, see the LICENSE file.

                PlayStation®, PlayStation®4 System Software and PlayStation®5 System Software are trademarks or registered trademarks of Sony Interactive Entertainment Inc.
                """);
            alert.showAndWait();
        });
        HBox info = new HBox(infoButton);
        info.setAlignment(Pos.BOTTOM_RIGHT);
        //endregion

        FlowPane pane = new FlowPane(message, console, usb, media, info);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setOrientation(Orientation.VERTICAL);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }
}
