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

public class CreditCardController implements Initializable {

    // FXML Annotations
    @FXML
    private Button backButton;

    @FXML
    private Label issuerLabel;

    @FXML
    private Label cardholderLabel;

    @FXML
    private Label expirationLabel;

    @FXML
    private Label cardNumberLabel;

    @FXML
    private Label ccvLabel;

    @FXML
    private ToggleButton cardNumberToggle;

    @FXML
    private ToggleButton ccvToggle;

    @FXML
    private Label issuerValueLabel;

    @FXML
    private Label numberValueLabel;

    @FXML
    private Label nameValueLabel;

    @FXML
    private Label expirationValueLabel;

    @FXML
    private Label ccvValueLabel;

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

    // Proxies for Sensitive Fields
    private MaskedFieldProxy cardNumberProxy;
    private MaskedFieldProxy ccvProxy;
    private final SensitiveValueProxy issuerProxy = new SensitiveValueProxy("My Bank", false);
    private final SensitiveValueProxy numberProxy = new SensitiveValueProxy("4111111111111111", true);
    private final SensitiveValueProxy nameProxy = new SensitiveValueProxy("Card Holder", false);
    private final SensitiveValueProxy expirationProxy = new SensitiveValueProxy("12/29", false);
    private final SensitiveValueProxy ccvSensitiveProxy = new SensitiveValueProxy("123", true);

    // Observable List for Table Data
    private final ObservableList<MaskedCreditCardProxy> creditCards = FXCollections.observableArrayList();

    /**
     * Main initialize method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up information for sensitive value proxies
        issuerValueLabel.setText(issuerProxy.getDisplayValue());
        numberValueLabel.setText(numberProxy.getDisplayValue());
        nameValueLabel.setText(nameProxy.getDisplayValue());
        expirationValueLabel.setText(expirationProxy.getDisplayValue());
        ccvValueLabel.setText(ccvSensitiveProxy.getDisplayValue());

        // Create credit card proxies
        CreditCard creditCard = new CreditCard(1, 1, "Visa", 411111111, "Alex Smith", 1225, 321);
        issuerLabel.setText(creditCard.getIssuer());
        cardholderLabel.setText(creditCard.getCardholderName());
        expirationLabel.setText(String.valueOf(creditCard.getExpirationDate()));

        // Initialize masked proxies for card number and CCV
        cardNumberProxy = new MaskedFieldProxy(String.valueOf(creditCard.getCreditCardNumber()));
        ccvProxy = new MaskedFieldProxy(String.valueOf(creditCard.getCCVCode()));

        // Bind text properties
        cardNumberLabel.textProperty().bind(cardNumberProxy.displayValueProperty());
        ccvLabel.textProperty().bind(ccvProxy.displayValueProperty());

        // Update toggle buttons
        updateToggle(cardNumberToggle, cardNumberProxy);
        updateToggle(ccvToggle, ccvProxy);

        // Set up listeners for toggles
        cardNumberToggle.selectedProperty().addListener((obs, oldVal, newVal) -> updateToggle(cardNumberToggle, cardNumberProxy));
        ccvToggle.selectedProperty().addListener((obs, oldVal, newVal) -> updateToggle(ccvToggle, ccvProxy));

        // Populate table with initial data
        creditCards.addAll(
                new MaskedCreditCardProxy(new CreditCard(1, 1, "Visa", 411111111, "Alex Doe", 1225, 123)),
                new MaskedCreditCardProxy(new CreditCard(2, 1, "Mastercard", 550000000, "Sam Smith", 1124, 987))
        );
        creditCardTable.setItems(creditCards);

        // Set cell value factories
        issuerColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getIssuer()));
        numberColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCreditCardNumber()));
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCardholderName()));
        expirationColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getExpirationDate()));
        ccvColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCcvCode()));
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

    private void updateToggle(ToggleButton toggle, MaskedFieldProxy proxy) {
        if (toggle.isSelected()) {
            proxy.reveal();
            toggle.setText("Hide");
        } else {
            proxy.hide();
            toggle.setText("Show");
        }
    }

    @FXML
    public void CopyIssuerClicked() {
        ClipboardManager.copyFromProxy(issuerProxy);
    }

    @FXML
    public void CopyNumberClicked() {
        ClipboardManager.copyFromProxy(numberProxy);
    }

    @FXML
    public void CopyNameClicked() {
        ClipboardManager.copyFromProxy(nameProxy);
    }

    @FXML
    public void CopyExpirationClicked() {
        ClipboardManager.copyFromProxy(expirationProxy);
    }

    @FXML
    public void CopyCcvClicked() {
        ClipboardManager.copyFromProxy(ccvSensitiveProxy);
    }

    public void loadCreditCard(CreditCard creditCard) {
        if (creditCard == null) {
            return;
        }

        // Update proxies with new credit card data
        issuerProxy.updateValue(creditCard.getIssuer());
        numberProxy.updateValue(String.valueOf(creditCard.getCreditCardNumber()));
        nameProxy.updateValue(creditCard.getCardholderName());
        expirationProxy.updateValue(String.valueOf(creditCard.getExpirationDate()));
        ccvSensitiveProxy.updateValue(String.valueOf(creditCard.getCCVCode()));

        // Update UI with new data
        issuerValueLabel.setText(issuerProxy.getDisplayValue());
        numberValueLabel.setText(numberProxy.getDisplayValue());
        nameValueLabel.setText(nameProxy.getDisplayValue());
        expirationValueLabel.setText(expirationProxy.getDisplayValue());
        ccvValueLabel.setText(ccvSensitiveProxy.getDisplayValue());
    }

    private Callback<TableColumn<MaskedCreditCardProxy, MaskedCreditCardProxy>, TableCell<MaskedCreditCardProxy, MaskedCreditCardProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy");

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
                    ClipboardContent content = new ClipboardContent();
                    content.putString("Number: " + proxy.getCreditCardNumber()
                            + "\nName: " + proxy.getCardholderName()
                            + "\nExpiration: " + proxy.getExpirationDate()
                            + "\nCCV: " + proxy.getCcvCode());
                    Clipboard.getSystemClipboard().setContent(content);
                });
            }

            @Override
            protected void updateItem(MaskedCreditCardProxy proxy, boolean empty) {
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