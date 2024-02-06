package com.example.myapplication.domain.bookmark.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = WordEntity.class, parentColumns = {"word_id"}, childColumns = {"word_id"}, onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)}
//        ,primaryKeys = {"word_id", "context"}
)
public class ContextsEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "context_id")
    private int contextID;
    @ColumnInfo(name = "word_id")
    @NonNull
    private int wordID;

    @ColumnInfo(name = "context")
    @NonNull
    private String context;

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    @NonNull
    public String getContext() {
        return context;
    }

    public void setContext(@NonNull String context) {
        this.context = context;
    }

    public ContextsEntity(int wordID, @NonNull String context) {
        this.wordID = wordID;
        this.context = context;
    }

    public int getContextID() {
        return contextID;
    }

    public void setContextID(int contextID) {
        this.contextID = contextID;
    }

    @Override
    public String toString() {
//        return "ContextsEntity{" +
//                ", wordID=" + wordID +
//                ", context='" + context + '\'' +
//                '}';
        return context;
    }
}
