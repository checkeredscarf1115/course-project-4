package com.example.myapplication.ui.PageResult;

import java.util.ArrayList;
import java.util.List;

public class ResultRepository {
    private List<String> compsTo;
    //private List<List<String>> meaningList;
    private List<List<String>> synsList;
    private List<List<String>> antsList;
    private List<List<String>> contextList;
    private List<String> defsTo;
    private List<String> defsFrom;
    private String compFrom;

    public ResultRepository() {
        compsTo = new ArrayList<>();
        //meaningList = new ArrayList<>();
        synsList = new ArrayList<>();
        antsList = new ArrayList<>();
        contextList = new ArrayList<>();
        defsTo = new ArrayList<>();
        defsFrom = new ArrayList<>();
    }

//    public List<List<String>> getMeaningList() {
//        return meaningList;
//    }

    public List<List<String>> getSynsList() {
        return synsList;
    }

    public List<List<String>> getAntsList() {
        return antsList;
    }

    public List<List<String>> getContextList() {
        return contextList;
    }

    public List<String> getDefsTo() {
        return defsTo;
    }

//    public void setMeaningList(List<List<String>> meaningList) {
//        this.meaningList = meaningList;
//    }

    public void setSynsList(List<List<String>> synsList) {
        this.synsList = synsList;
    }

    public void setAntsList(List<List<String>> antsList) {
        this.antsList = antsList;
    }

    public void setContextList(List<List<String>> contextList) {
        this.contextList = contextList;
    }

    public void setDefsTo(List<String> defsTo) {
        this.defsTo = defsTo;
    }

    public List<String> getCompsTo() {
        return compsTo;
    }

    public void setCompsTo(List<String> compsTo) {
        this.compsTo = compsTo;
    }

    public List<String> getDefsFrom() {
        return defsFrom;
    }

    public void setDefsFrom(List<String> defsFrom) {
        this.defsFrom = defsFrom;
    }

    public String getCompFrom() {
        return compFrom;
    }

    public void setCompFrom(String compFrom) {
        this.compFrom = compFrom;
    }
}
