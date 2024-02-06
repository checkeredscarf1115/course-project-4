package com.example.myapplication.domain.bookmark.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = WordEntity.class,
        parentColumns = {"word_id"}, childColumns = {"word_id"}, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)}
//        ,primaryKeys = {"word_id", "syn_comp"}
)
public class SynsEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "syn_id")
    private int synID;

    @ColumnInfo(name = "word_id")
    @NonNull
    private int wordID;

    @ColumnInfo(name = "syn_comp")
    @NonNull
    private String synComp;

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public String getSynComp() {
        return synComp;
    }

    public void setSynComp(String synComp) {
        this.synComp = synComp;
    }

    public SynsEntity(int wordID, String synComp) {
        this.wordID = wordID;
        this.synComp = synComp;
    }

    public int getSynID() {
        return synID;
    }

    public void setSynID(int synID) {
        this.synID = synID;
    }

    @Override
    public String toString() {
//        return "SynsEntity{" +
//                "synID=" + wordID +
//                ", synComp=" + synComp +
//                '}';
        return synComp;
    }
}
