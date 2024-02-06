package com.example.myapplication.domain.bookmark.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = WordEntity.class,
        parentColumns = {"word_id"}, childColumns = {"word_id"}, onDelete = ForeignKey.CASCADE,onUpdate = ForeignKey.CASCADE)}
//        ,primaryKeys = {"word_id", "ant_comp"}
)
public class AntsEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ant_id")
    private int antID;
    @ColumnInfo(name = "word_id")
    @NonNull
    private int wordID;

    @ColumnInfo(name = "ant_comp")
    @NonNull
    private String antComp;

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String getAntComp() {
        return antComp;
    }

    public void setAntComp(String antComp) {
        this.antComp = antComp;
    }

    public AntsEntity(int wordID, String antComp) {
        this.wordID = wordID;
        this.antComp = antComp;
    }

    public int getAntID() {
        return antID;
    }

    public void setAntID(int antID) {
        this.antID = antID;
    }

    @Override
    public String toString() {
//        return "AntsEntity{" +
//                "wordID=" + wordID +
//                ", antComp=" + antComp +
//                '}';
        return antComp;
    }
}
