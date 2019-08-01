package com.example.myapplication;


public class Request {

    private String query;
    private String perspective;
    private String language;
    private String sortBy;

    public Request(String query, String perspective, String language, String sortBy){
        this.query = query;
        this.perspective = perspective;
        this.language = language;
        this.sortBy = sortBy;
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

    public void setQuery(String query){
        this.query = query;
    }

    public void setPerspective(String perspective){
        this.perspective = perspective;
    }

    public String getQuery() {
        return query;
    }

    public String getPerspective() {
        return perspective;
    }

    public boolean equals(Request request){
        return query.equals(request.getQuery()) && perspective.equals(request.getPerspective()) &&
                language.equals(request.getLanguage()) && sortBy.equals(request.getSortBy());
    }
}
