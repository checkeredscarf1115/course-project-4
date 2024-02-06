package com.example.myapplication.ui.slideshow;

public class ModelLanguage {
    private String langCode;
    private String langTitle;

    public ModelLanguage(String langCode, String langTitle) {
        this.langCode = langCode;
        this.langTitle = langTitle;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getLangTitle() {
        return langTitle;
    }

    public void setLangTitle(String langTitle) {
        this.langTitle = langTitle;
    }
}
