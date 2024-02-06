package com.example.myapplication.domain.bookmark.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "word_id")
    private int wordID;

    @ColumnInfo(name = "word_comp")
    private String wordComp;

    @ColumnInfo(name = "word_def")
    private String wordDef;

    @ColumnInfo(name = "translation")
    private String translation;

    @ColumnInfo(name = "language_from")
    private String languageFrom;

    @ColumnInfo(name = "language_to")
    private String languageTo;

    public WordEntity(@NonNull String wordComp, @NonNull String wordDef, @NonNull String translation, @NonNull String languageFrom, @NonNull String languageTo) {
        this.wordComp = wordComp;
        this.wordDef = wordDef;
        this.translation = translation;
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    @NonNull
    public String getWordComp() {
        return wordComp;
    }

    public void setWordComp(@NonNull String wordComp) {
        this.wordComp = wordComp;
    }

    @NonNull
    public String getWordDef() {
        return wordDef;
    }

    public void setWordDef(@NonNull String wordDef) {
        this.wordDef = wordDef;
    }

    @NonNull
    public String getTranslation() {
        return translation;
    }

    public void setTranslation(@NonNull String translation) {
        this.translation = translation;
    }

    @NonNull
    public String getLanguageFrom() {
        return languageFrom;
    }

    public void setLanguageFrom(@NonNull String languageFrom) {
        this.languageFrom = languageFrom;
    }

    @NonNull
    public String getLanguageTo() {
        return languageTo;
    }

    public void setLanguageTo(@NonNull String languageTo) {
        this.languageTo = languageTo;
    }

    @Override
    public String toString() {
        return "WordEntity{" +
                "wordID=" + wordID +
                ", wordComp='" + wordComp + '\'' +
                ", wordDef='" + wordDef + '\'' +
                ", translation='" + translation + '\'' +
                ", languageFrom='" + languageFrom + '\'' +
                ", languageTo='" + languageTo + '\'' +
                '}';
    }
}
