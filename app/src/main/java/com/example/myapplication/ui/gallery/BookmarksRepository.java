package com.example.myapplication.ui.gallery;

import com.example.myapplication.domain.bookmark.entities.AntsEntity;
import com.example.myapplication.domain.bookmark.entities.ContextsEntity;
import com.example.myapplication.domain.bookmark.entities.SynsEntity;
import com.example.myapplication.domain.bookmark.entities.WordEntity;

import java.util.ArrayList;
import java.util.List;

public class BookmarksRepository {
    private List<WordEntity> wordEntityList;
    private List<List<SynsEntity>> synsEntityList;
    private List<List<AntsEntity>> antsEntitiesList;
    private List<List<ContextsEntity>> contextsEntitiesList;

    public BookmarksRepository() {
        wordEntityList = new ArrayList<>();
        synsEntityList = new ArrayList<>();
        antsEntitiesList = new ArrayList<>();
        contextsEntitiesList = new ArrayList<>();
    }

    public List<WordEntity> getWordEntityList() {
        return wordEntityList;
    }

    public void setWordEntityList(List<WordEntity> wordEntityList) {
        this.wordEntityList = wordEntityList;
    }

    public List<List<SynsEntity>> getSynsEntityList() {
        return synsEntityList;
    }

    public void setSynsEntityList(List<List<SynsEntity>> synsEntityList) {
        this.synsEntityList = synsEntityList;
    }

    public List<List<AntsEntity>> getAntsEntitiesList() {
        return antsEntitiesList;
    }

    public void setAntsEntitiesList(List<List<AntsEntity>> antsEntitiesList) {
        this.antsEntitiesList = antsEntitiesList;
    }

    public List<List<ContextsEntity>> getContextsEntitiesList() {
        return contextsEntitiesList;
    }

    public void setContextsEntitiesList(List<List<ContextsEntity>> contextsEntitiesList) {
        this.contextsEntitiesList = contextsEntitiesList;
    }
}
