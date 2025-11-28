package com.example.cis476termproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

public class SecureNoteController {

    @FXML
    private Button backButton;

    @FXML
    private Label noteContentsLabel;

    @FXML
    private ToggleButton noteToggle;

    private MaskedFieldProxy noteProxy;

    @FXML
    public void initialize() {
        SecureNote secureNote = new SecureNote(1, 1, "Travel itinerary and locker combination");

        noteProxy = new MaskedFieldProxy(secureNote.getContents());
        noteContentsLabel.textProperty().bind(noteProxy.displayValueProperty());

        updateToggle(noteToggle, noteProxy);
        noteToggle.selectedProperty().addListener((obs, oldVal, newVal) -> updateToggle(noteToggle, noteProxy));
    }

    @FXML
    public void toggleNote() {
        updateToggle(noteToggle, noteProxy);
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
}
