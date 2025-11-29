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

    // Buttons and Labels
    @FXML
    private Button backButton;

    @FXML
    private Label urlValueLabel;

    @FXML
    private Label usernameValueLabel;

    @FXML
    private Label passwordValueLabel;

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

    // Proxies for sensitive fields
    private MaskedFieldProxy usernameProxy;
    private MaskedFieldProxy passwordProxy;
    private final SensitiveValueProxy urlProxy = new SensitiveValueProxy("https://example.com", false);
    private final SensitiveValueProxy usernameSensitiveProxy = new SensitiveValueProxy("demo-user", false);
    private final SensitiveValueProxy passwordSensitiveProxy = new SensitiveValueProxy("strong-password", true);

    // Observable data for table
    private final ObservableList<MaskedCredentialProxy> credentialData = FXCollections.observableArrayList();

    /**
     * Main Initialize method for the controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize sensitive field proxies
        urlValueLabel.setText(urlProxy.getDisplayValue());
        usernameValueLabel.setText(usernameSensitiveProxy.getDisplayValue());
        passwordValueLabel.setText(passwordSensitiveProxy.getDisplayValue());

        // Populate the table with example data
        credentialData.addAll(
                new MaskedCredentialProxy(new Credentials(1, 1, "example.com", "alice@example.com", "P@ssword1!")),
                new MaskedCredentialProxy(new Credentials(2, 1, "example.org", "bob@example.org", "Sup3rSecret"))
        );

        credentialsTable.setItems(credentialData);

        // Set up columns for the table
        urlColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getURL()));
        usernameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPassword()));
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(buildActionCellFactory());
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
    public void CopyUrlClicked() {
        ClipboardManager.copyFromProxy(urlProxy);
    }

    @FXML
    public void CopyUsernameClicked() {
        ClipboardManager.copyFromProxy(usernameSensitiveProxy);
    }

    @FXML
    public void CopyPasswordClicked() {
        ClipboardManager.copyFromProxy(passwordSensitiveProxy);
    }

    public void loadCredentials(Credentials credentials) {
        if (credentials == null) {
            return;
        }
        urlProxy.updateValue(credentials.getURL());
        usernameSensitiveProxy.updateValue(credentials.getUsername());
        passwordSensitiveProxy.updateValue(credentials.getPassword());

        urlValueLabel.setText(urlProxy.getDisplayValue());
        usernameValueLabel.setText(usernameSensitiveProxy.getDisplayValue());
        passwordValueLabel.setText(passwordSensitiveProxy.getDisplayValue());
    }

    private void updateToggle(ToggleButton toggle, MaskedFieldProxy proxy) {
        if (toggle.isSelected()) {
            proxy.reveal();
            toggle.setText("Hide");
        } else {
            proxy.hide();
            toggle.setText("Show");
        }
    }

    private Callback<TableColumn<MaskedCredentialProxy, MaskedCredentialProxy>, TableCell<MaskedCredentialProxy, MaskedCredentialProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy");

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
                    ClipboardManager.copyToClipboard("Username: " + proxy.getUsername() + "\nPassword: " + proxy.getPassword());
                });
            }

            @Override
            protected void updateItem(MaskedCredentialProxy proxy, boolean empty) {
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
}