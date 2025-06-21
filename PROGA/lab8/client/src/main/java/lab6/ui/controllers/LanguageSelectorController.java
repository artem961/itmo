package lab6.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lab6.ui.AppManager;
import lab6.ui.utils.Language;

import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class LanguageSelectorController {
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    public void initialize() {
        Set<String> languages = Arrays.stream(Language.values())
                .map(Language::getName)
                .collect(Collectors.toSet());
        languageComboBox.getItems().addAll(languages);
        languageComboBox.setPromptText(
                AppManager.getInstance()
                        .getUserSettings().getLocale().getCountry());
    }

    public void languageSelected(ActionEvent actionEvent) {
        String selectedLanguage = languageComboBox.getValue();
        if (selectedLanguage != null) {
            changeLanguage(selectedLanguage);
        }
    }

    private void changeLanguage(String language) {
        Language[] languages = Language.values();
        for (Language lang : languages) {
            if (lang.getName().equals(language)) {
                AppManager.getInstance().changeLanguage(new Locale(lang.getAbb(), lang.getName()));
            }
        }
    }
}
