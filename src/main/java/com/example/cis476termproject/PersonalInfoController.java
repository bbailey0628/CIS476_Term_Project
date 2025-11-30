package com.example.cis476termproject;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @FXML
    private Button backButton;

    @FXML
    private TextField licenseField;

    @FXML
    private TextField ssnField;

    @FXML
    private TextField passportField;

    private final ObservableList<MaskedPersonalInfoProxy> personalInfos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        personalInfoTable.setItems(personalInfos);

        licenseColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLicenseNumber()));
        ssnColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSocialSecurityNumber()));
        passportColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassportNumber()));
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(buildActionCellFactory());

        personalInfoTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> populateForm(newVal));

        loadPersonalInfos();
    }

    @FXML
    public void BackButtonClicked() {
        try {
            MyPassApplication.switchScene("home-view.fxml");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void addPersonalInfo() {
        if (!validateInput()) {
            return;
        }
        PersonalInfo info = new PersonalInfo(0, getOwnerId(), licenseField.getText().trim(), ssnField.getText().trim(), passportField.getText().trim());
        int id = VaultDB.addPersonalInfo(info);
        if (id > 0) {
            info.setID(id);
            personalInfos.add(new MaskedPersonalInfoProxy(info));
            clearSelection();
        } else {
            showAlert("Save failed", "Unable to save personal info.");
        }
    }

    @FXML
    public void updatePersonalInfo() {
        MaskedPersonalInfoProxy proxy = personalInfoTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to update.");
            return;
        }
        if (!validateInput()) {
            return;
        }
        proxy.getPersonalInfo().setLicenseNumber(licenseField.getText().trim());
        proxy.getPersonalInfo().setSocialSecurityNumber(ssnField.getText().trim());
        proxy.getPersonalInfo().setPassportNumber(passportField.getText().trim());
        VaultDB.updatePersonalInfo(proxy.getPersonalInfo());
        personalInfoTable.refresh();
        clearSelection();
    }

    @FXML
    public void deletePersonalInfo() {
        MaskedPersonalInfoProxy proxy = personalInfoTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to delete.");
            return;
        }
        deletePersonalInfo(proxy);
    }

    @FXML
    public void clearSelection() {
        personalInfoTable.getSelectionModel().clearSelection();
        licenseField.clear();
        ssnField.clear();
        passportField.clear();
    }

    private Callback<TableColumn<MaskedPersonalInfoProxy, MaskedPersonalInfoProxy>, TableCell<MaskedPersonalInfoProxy, MaskedPersonalInfoProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy All");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

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
                    ClipboardManager.copyToClipboard(
                            "License: " + proxy.getLicenseNumber() +
                                    "\nSSN: " + proxy.getSocialSecurityNumber() +
                                    "\nPassport: " + proxy.getPassportNumber()
                    );
                });

                editButton.setOnAction(event -> {
                    MaskedPersonalInfoProxy proxy = getTableView().getItems().get(getIndex());
                    populateForm(proxy);
                    personalInfoTable.getSelectionModel().select(proxy);
                });

                deleteButton.setOnAction(event -> {
                    MaskedPersonalInfoProxy proxy = getTableView().getItems().get(getIndex());
                    deletePersonalInfo(proxy);
                });
            }

            @Override
            protected void updateItem(MaskedPersonalInfoProxy proxy, boolean empty) {
                super.updateItem(proxy, empty);
                if (empty || proxy == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setGraphic(new javafx.scene.layout.HBox(8, toggleButton, copyButton, editButton, deleteButton));
                }
            }
        };
    }

    private void populateForm(MaskedPersonalInfoProxy proxy) {
        if (proxy == null) {
            return;
        }
        licenseField.setText(proxy.getUnmaskedLicenseNumber());
        ssnField.setText(proxy.getUnmaskedSocialSecurityNumber());
        passportField.setText(proxy.getUnmaskedPassportNumber());
    }

    private void loadPersonalInfos() {
        personalInfos.clear();
        for (PersonalInfo info : VaultDB.getPersonalInfos(getOwnerId())) {
            personalInfos.add(new MaskedPersonalInfoProxy(info));
        }
    }

    private void deletePersonalInfo(MaskedPersonalInfoProxy proxy) {
        VaultDB.deletePersonalInfo(proxy.getPersonalInfo().getID());
        personalInfos.remove(proxy);
        if (personalInfoTable.getSelectionModel().getSelectedItem() == proxy) {
            clearSelection();
        }
    }

    private int getOwnerId() {
        UserLogin loggedInUser = SessionManager.getInstance().getLoggedInUser();
        return loggedInUser != null ? loggedInUser.getID() : 1;
    }

    private boolean validateInput() {
        if (licenseField.getText().trim().isEmpty() && ssnField.getText().trim().isEmpty() && passportField.getText().trim().isEmpty()) {
            showAlert("Missing information", "At least one of License, SSN, or Passport must be provided.");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
