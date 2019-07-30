package com.example.myapplication;

public class Preferences {
    private String source;
    private String language;
    private String sortBy;

    public Preferences(String source, String language, String sortBy){
        this.source = source;
        this.language = language;
        this.sortBy = sortBy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }


}
