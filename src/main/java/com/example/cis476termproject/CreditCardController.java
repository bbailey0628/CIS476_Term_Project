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

public class CreditCardController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<MaskedCreditCardProxy> creditCardTable;

    @FXML
    private TableColumn<MaskedCreditCardProxy, String> issuerColumn;

    @FXML
    private TableColumn<MaskedCreditCardProxy, String> numberColumn;

    @FXML
    private TableColumn<MaskedCreditCardProxy, String> nameColumn;

    @FXML
    private TableColumn<MaskedCreditCardProxy, String> expirationColumn;

    @FXML
    private TableColumn<MaskedCreditCardProxy, String> ccvColumn;

    @FXML
    private TableColumn<MaskedCreditCardProxy, MaskedCreditCardProxy> actionsColumn;

    @FXML
    private TextField issuerField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField expirationField;

    @FXML
    private TextField ccvField;

    private final ObservableList<MaskedCreditCardProxy> creditCards = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditCardTable.setItems(creditCards);

        issuerColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIssuer()));
        numberColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCreditCardNumber()));
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCardholderName()));
        expirationColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getExpirationDate()));
        ccvColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCcvCode()));
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(buildActionCellFactory());

        creditCardTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> populateForm(newVal));

        loadCards();
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
    public void addCard() {
        if (!validateInput()) {
            return;
        }
        CreditCard card = new CreditCard(0, getOwnerId(),
                issuerField.getText().trim(),
                numberField.getText().trim(),
                nameField.getText().trim(),
                expirationField.getText().trim(),
                ccvField.getText().trim());
        int id = VaultDB.addCreditCard(card);
        if (id > 0) {
            card.setID(id);
            creditCards.add(new MaskedCreditCardProxy(card));
            clearSelection();
        } else {
            showAlert("Save failed", "Unable to save credit card.");
        }
    }

    @FXML
    public void updateCard() {
        MaskedCreditCardProxy proxy = creditCardTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to update.");
            return;
        }
        if (!validateInput()) {
            return;
        }
        proxy.getCreditCard().setIssuer(issuerField.getText().trim());
        proxy.getCreditCard().setCreditCardNumber(numberField.getText().trim());
        proxy.getCreditCard().setCardholderName(nameField.getText().trim());
        proxy.getCreditCard().setExpirationDate(expirationField.getText().trim());
        proxy.getCreditCard().setCCVCode(ccvField.getText().trim());
        VaultDB.updateCreditCard(proxy.getCreditCard());
        creditCardTable.refresh();
        clearSelection();
    }

    @FXML
    public void deleteCard() {
        MaskedCreditCardProxy proxy = creditCardTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to delete.");
            return;
        }
        deleteCard(proxy);
    }

    @FXML
    public void clearSelection() {
        creditCardTable.getSelectionModel().clearSelection();
        issuerField.clear();
        numberField.clear();
        nameField.clear();
        expirationField.clear();
        ccvField.clear();
    }

    private Callback<TableColumn<MaskedCreditCardProxy, MaskedCreditCardProxy>, TableCell<MaskedCreditCardProxy, MaskedCreditCardProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy All");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                toggleButton.setOnAction(event -> {
                    MaskedCreditCardProxy proxy = getTableView().getItems().get(getIndex());
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
                    MaskedCreditCardProxy proxy = getTableView().getItems().get(getIndex());
                    ClipboardManager.copyToClipboard(
                            "Issuer: " + proxy.getIssuer() +
                                    "\nNumber: " + proxy.getCreditCardNumber() +
                                    "\nName: " + proxy.getCardholderName() +
                                    "\nExpiration: " + proxy.getExpirationDate() +
                                    "\nCCV: " + proxy.getCcvCode()
                    );
                });

                editButton.setOnAction(event -> {
                    MaskedCreditCardProxy proxy = getTableView().getItems().get(getIndex());
                    populateForm(proxy);
                    creditCardTable.getSelectionModel().select(proxy);
                });

                deleteButton.setOnAction(event -> {
                    MaskedCreditCardProxy proxy = getTableView().getItems().get(getIndex());
                    deleteCard(proxy);
                });
            }

            @Override
            protected void updateItem(MaskedCreditCardProxy proxy, boolean empty) {
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

    private void populateForm(MaskedCreditCardProxy proxy) {
        if (proxy == null) {
            return;
        }
        issuerField.setText(proxy.getIssuer());
        numberField.setText(proxy.getUnmaskedCreditCardNumber());
        nameField.setText(proxy.getUnmaskedCardholderName());
        expirationField.setText(proxy.getUnmaskedExpirationDate());
        ccvField.setText(proxy.getUnmaskedCcvCode());
    }

    private void loadCards() {
        creditCards.clear();
        for (CreditCard card : VaultDB.getCreditCards(getOwnerId())) {
            creditCards.add(new MaskedCreditCardProxy(card));
        }
    }

    private void deleteCard(MaskedCreditCardProxy proxy) {
        VaultDB.deleteCreditCard(proxy.getCreditCard().getID());
        creditCards.remove(proxy);
        if (creditCardTable.getSelectionModel().getSelectedItem() == proxy) {
            clearSelection();
        }
    }

    private int getOwnerId() {
        UserLogin loggedInUser = SessionManager.getInstance().getLoggedInUser();
        return loggedInUser != null ? loggedInUser.getID() : 1;
    }

    private boolean validateInput() {
        if (issuerField.getText().trim().isEmpty() || numberField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()
                || expirationField.getText().trim().isEmpty() || ccvField.getText().trim().isEmpty()) {
            showAlert("Missing information", "Issuer, Card Number, Cardholder Name, Expiration Date, and CCV are required.");
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
