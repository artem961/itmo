package lab6.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lab6.ui.AppManager;

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
        Set<String> languages = AppManager.getInstance().localeManager.getLocales().stream()
                .map(locale -> locale.getDisplayLanguage(locale))
                .collect(Collectors.toSet());

        languageComboBox.getItems().addAll(languages);
        languageComboBox.setPromptText(
                AppManager.getInstance()
                        .getUserSettings().getLocale().getDisplayLanguage(
                                AppManager.getInstance().getUserSettings().getLocale()));
    }

    public void languageSelected(ActionEvent actionEvent) {
        String selectedLanguage = languageComboBox.getValue();
        if (selectedLanguage != null) {
            changeLanguage(selectedLanguage);
        }
    }

    private void changeLanguage(String language) {
       Set<Locale> locales = AppManager.getInstance().localeManager.getLocales();
        for (Locale locale : locales) {
            if (language.equals(locale.getDisplayLanguage(locale))) {
                AppManager.getInstance().changeLanguage(locale);
            }
        }
    }
}
