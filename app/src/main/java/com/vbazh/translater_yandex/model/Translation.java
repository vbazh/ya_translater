package com.vbazh.translater_yandex.model;

/**
 *
 * Модель перевода
 *
 */

public class Translation {

    private Integer id;
    private String sourceLang;
    private String targetLang;
    private String sourceText;
    private String translatedText;
    private boolean isFavorite;

    public Translation() {

    }

    public Translation(Integer id, String sourceLang, String targetLang, String sourceText, String translatedText, Boolean inFavorites) {
        this.id = id;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
        this.sourceText = sourceText;
        this.translatedText = translatedText;
        this.isFavorite = inFavorites;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSourceLang() {
        return sourceLang;
    }

    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    public String getTargetLang() {
        return targetLang;
    }

    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
