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

public class SecureNoteController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TableView<MaskedSecureNoteProxy> noteTable;

    @FXML
    private TableColumn<MaskedSecureNoteProxy, String> titleColumn;

    @FXML
    private TableColumn<MaskedSecureNoteProxy, String> noteColumn;

    @FXML
    private TableColumn<MaskedSecureNoteProxy, MaskedSecureNoteProxy> actionsColumn;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea noteField;

    private final ObservableList<MaskedSecureNoteProxy> notes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        noteTable.setItems(notes);

        titleColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTitle()));
        noteColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNote()));
        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionsColumn.setCellFactory(buildActionCellFactory());

        noteTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> populateForm(newVal));

        loadNotes();
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
    public void addNote() {
        if (!validateInput()) {
            return;
        }
        SecureNote note = new SecureNote(0, getOwnerId(), titleField.getText().trim(), noteField.getText().trim());
        int id = VaultDB.addSecureNote(note);
        if (id > 0) {
            note.setID(id);
            notes.add(new MaskedSecureNoteProxy(note));
            clearSelection();
        } else {
            showAlert("Save failed", "Unable to save note.");
        }
    }

    @FXML
    public void updateNote() {
        MaskedSecureNoteProxy proxy = noteTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to update.");
            return;
        }
        if (!validateInput()) {
            return;
        }
        proxy.getSecureNote().setTitle(titleField.getText().trim());
        proxy.getSecureNote().setContents(noteField.getText().trim());
        VaultDB.updateSecureNote(proxy.getSecureNote());
        noteTable.refresh();
        clearSelection();
    }

    @FXML
    public void deleteNote() {
        MaskedSecureNoteProxy proxy = noteTable.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            showAlert("No selection", "Select an entry to delete.");
            return;
        }
        deleteNote(proxy);
    }

    @FXML
    public void clearSelection() {
        noteTable.getSelectionModel().clearSelection();
        titleField.clear();
        noteField.clear();
    }

    private Callback<TableColumn<MaskedSecureNoteProxy, MaskedSecureNoteProxy>, TableCell<MaskedSecureNoteProxy, MaskedSecureNoteProxy>> buildActionCellFactory() {
        return column -> new TableCell<>() {
            private final Button toggleButton = new Button("Reveal");
            private final Button copyButton = new Button("Copy Note");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                toggleButton.setOnAction(event -> {
                    MaskedSecureNoteProxy proxy = getTableView().getItems().get(getIndex());
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
                    MaskedSecureNoteProxy proxy = getTableView().getItems().get(getIndex());
                    ClipboardManager.copyToClipboard(proxy.getUnmaskedNote());
                });

                editButton.setOnAction(event -> {
                    MaskedSecureNoteProxy proxy = getTableView().getItems().get(getIndex());
                    populateForm(proxy);
                    noteTable.getSelectionModel().select(proxy);
                });

                deleteButton.setOnAction(event -> {
                    MaskedSecureNoteProxy proxy = getTableView().getItems().get(getIndex());
                    deleteNote(proxy);
                });
            }

            @Override
            protected void updateItem(MaskedSecureNoteProxy proxy, boolean empty) {
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

    private void populateForm(MaskedSecureNoteProxy proxy) {
        if (proxy == null) {
            return;
        }
        titleField.setText(proxy.getTitle());
        noteField.setText(proxy.getUnmaskedNote());
    }

    private void loadNotes() {
        notes.clear();
        for (SecureNote note : VaultDB.getSecureNotes(getOwnerId())) {
            notes.add(new MaskedSecureNoteProxy(note));
        }
    }

    private void deleteNote(MaskedSecureNoteProxy proxy) {
        VaultDB.deleteSecureNote(proxy.getSecureNote().getID());
        notes.remove(proxy);
        if (noteTable.getSelectionModel().getSelectedItem() == proxy) {
            clearSelection();
        }
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Missing information", "A title is required for a secure note.");
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

    private int getOwnerId() {
        UserLogin loggedInUser = SessionManager.getInstance().getLoggedInUser();
        return loggedInUser != null ? loggedInUser.getID() : 1;
    }
}
