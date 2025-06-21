package lab6.ui.utils;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Language {
    ENGLISH("English", "en"),
    RUSSIAN("Русский", "ru"),
    MACEDONIAN("Македонски", "ma"),
    SPANISH("Español", "sp"),
    GREEK("ελληνική", "gr");

    @Getter
    private final String name;
    @Getter
    private final String abb;
    Language(String name, String abb) {
        this.name = name;
        this.abb = abb;
    }
}

