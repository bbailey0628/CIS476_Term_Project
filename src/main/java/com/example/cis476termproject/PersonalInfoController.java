package com.example.cis476termproject;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonalInfoController implements Initializable {

    @FXML
    private TableView<MaskedPersonalInfoProxy> personalInfoTable;

    @FXML
    private TableColumn<MaskedPersonalInfoProxy, String> licenseColumn;

    @FXML
    private TableColumn<MaskedPersonalInfoProxy, String> ssnColumn;

    @FXML
    private TableColumn<MaskedPersonalInfoProxy, String> passportColumn;

    @FXML
    private TableColumn<MaskedPersonalInfoProxy, MaskedPersonalInfoProxy> actionsColumn;

    // Proxies for masking sensitive information
    private MaskedFieldProxy licenseProxy;
    private MaskedFieldProxy ssnProxy;
    private MaskedFieldProxy passportProxy;

    // Observable list for the table data
    private final ObservableList<MaskedPersonalInfoProxy> personalInfos = FXCollections.observableArrayList();
    @FXML
    private Button backButton;

    /**
     * Initializes the controller for the scene.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table with sample data
        personalInfos.addAll(
                new MaskedPersonalInfoProxy(new PersonalInfo(1, "D1234567", 111223333, "P12345678")),
                new MaskedPersonalInfoProxy(new PersonalInfo(1, "D7654321", 999887777, "P87654321"))
        );

        personalInfoTable.setItems(personalInfos);

        // Configure table columns
        licenseColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLicenseNumber()));
        ssnColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSocialSecurityNumber()));
        passportColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassportNumber()));
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(buildActionCellFactory());
    }

    /**
     * Handles the 'Back' button click event.
     */
    @FXML
    public void BackButtonClicked() {
        try {
            MyPassApplication.switchScene("home-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the toggle button state and associated proxy.
     */
    private void updateToggle(ToggleButton toggle, MaskedFieldProxy proxy) {
        if (toggle.isSelected()) {
            proxy.reveal();
            toggle.setText("Hide");
        } else {
            proxy.hide();
            toggle.setText("Show");
        }
    }

    /**
     * Builds the actions column cell factory for the table view.
     *
     * @return A callback for the actions column.
     */
    private Callback<TableColumn<MaskedPersonalInfoProxy, MaskedPersonalInfoProxy>, TableCell<MaskedPersonalInfoProxy, MaskedPersonalInfoProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy");

            {
                toggleButton.setOnAction(event -> {
                    MaskedPersonalInfoProxy proxy = getTableView().getItems().get(getIndex());
                    if (proxy.isRevealed()) {
                        proxy.hide();
                        toggleButton.setText("Reveal");
                    } else {
                        proxy.reveal();
                        toggleButton.setText("Hide");
                    }
                    getTableView().refresh();
                });

                copyButton.setOnAction(event -> {
                    MaskedPersonalInfoProxy proxy = getTableView().getItems().get(getIndex());
                    ClipboardContent content = new ClipboardContent();
                    content.putString(
                            "License: " + proxy.getLicenseNumber() +
                                    "\nSSN: " + proxy.getSocialSecurityNumber() +
                                    "\nPassport: " + proxy.getPassportNumber()
                    );
                    Clipboard.getSystemClipboard().setContent(content);
                });
            }

            @Override
            protected void updateItem(MaskedPersonalInfoProxy proxy, boolean empty) {
                super.updateItem(proxy, empty);
                if (empty || proxy == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(new javafx.scene.layout.HBox(10, toggleButton, copyButton));
                }
            }
        };
    }

    @FXML
    public void toggleSsn() {
    }

    @FXML
    public void toggleLicense() {
    }

    @FXML
    public void togglePassport() {
    }
}