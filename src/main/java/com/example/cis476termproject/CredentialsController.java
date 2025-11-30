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

public class CredentialsController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<MaskedCredentialProxy> credentialsTable;

    @FXML
    private TableColumn<MaskedCredentialProxy, String> urlColumn;

    @FXML
    private TableColumn<MaskedCredentialProxy, String> usernameColumn;

    @FXML
    private TableColumn<MaskedCredentialProxy, String> passwordColumn;

    @FXML
    private TableColumn<MaskedCredentialProxy, MaskedCredentialProxy> actionsColumn;

    @FXML
    private TextField urlField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final ObservableList<MaskedCredentialProxy> credentialData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        credentialsTable.setItems(credentialData);

        urlColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getURL()));
        usernameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassword()));
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(buildActionCellFactory());

        credentialsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> populateForm(newVal));

        loadCredentials();
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
    public void addCredential() {
        if (!validateInput()) {
            return;
        }
        Credentials credentials = new Credentials(0, getOwnerId(), urlField.getText().trim(), usernameField.getText().trim(), passwordField.getText().trim());
        int id = VaultDB.addCredentials(credentials);
        if (id > 0) {
            credentials.setID(id);
            credentialData.add(new MaskedCredentialProxy(credentials));
            clearSelection();
        } else {
            showAlert("Save failed", "Unable to save credentials. Please try again.");
        }
    }

    @FXML
    public void updateCredential() {
        MaskedCredentialProxy proxy = credentialsTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to update.");
            return;
        }
        if (!validateInput()) {
            return;
        }
        proxy.getCredentials().setURL(urlField.getText().trim());
        proxy.getCredentials().setUsername(usernameField.getText().trim());
        proxy.getCredentials().setPassword(passwordField.getText().trim());
        VaultDB.updateCredentials(proxy.getCredentials());
        credentialsTable.refresh();
        clearSelection();
    }

    @FXML
    public void deleteCredential() {
        MaskedCredentialProxy proxy = credentialsTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to delete.");
            return;
        }
        deleteCredential(proxy);
    }

    @FXML
    public void clearSelection() {
        credentialsTable.getSelectionModel().clearSelection();
        urlField.clear();
        usernameField.clear();
        passwordField.clear();
    }

    private void deleteCredential(MaskedCredentialProxy proxy) {
        VaultDB.deleteCredentials(proxy.getCredentials().getID());
        credentialData.remove(proxy);
        if (credentialsTable.getSelectionModel().getSelectedItem() == proxy) {
            clearSelection();
        }
    }

    private Callback<TableColumn<MaskedCredentialProxy, MaskedCredentialProxy>, TableCell<MaskedCredentialProxy, MaskedCredentialProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy All");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                toggleButton.setOnAction(event -> {
                    MaskedCredentialProxy proxy = getTableView().getItems().get(getIndex());
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
                    MaskedCredentialProxy proxy = getTableView().getItems().get(getIndex());
                    ClipboardManager.copyToClipboard(
                            "URL: " + proxy.getURL() +
                                    "\nUsername: " + proxy.getUsername() +
                                    "\nPassword: " + proxy.getPassword()
                    );
                });

                editButton.setOnAction(event -> {
                    MaskedCredentialProxy proxy = getTableView().getItems().get(getIndex());
                    populateForm(proxy);
                    credentialsTable.getSelectionModel().select(proxy);
                });

                deleteButton.setOnAction(event -> {
                    MaskedCredentialProxy proxy = getTableView().getItems().get(getIndex());
                    deleteCredential(proxy);
                });
            }

            @Override
            protected void updateItem(MaskedCredentialProxy proxy, boolean empty) {
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

    private void populateForm(MaskedCredentialProxy proxy) {
        if (proxy == null) {
            return;
        }
        urlField.setText(proxy.getURL());
        usernameField.setText(proxy.getUnmaskedUsername());
        passwordField.setText(proxy.getUnmaskedPassword());
    }

    private void loadCredentials() {
        credentialData.clear();
        for (Credentials credentials : VaultDB.getCredentials(getOwnerId())) {
            credentialData.add(new MaskedCredentialProxy(credentials));
        }
    }

    private int getOwnerId() {
        UserLogin loggedInUser = SessionManager.getInstance().getLoggedInUser();
        return loggedInUser != null ? loggedInUser.getID() : 1;
    }

    private boolean validateInput() {
        if (urlField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            showAlert("Missing information", "URL, Username, and Password are required.");
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
