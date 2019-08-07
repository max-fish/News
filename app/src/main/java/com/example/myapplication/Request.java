package com.example.myapplication;


public class Request {

    private String query;
    private String perspective;
    private String language;
    private String sortBy;
    private String category;

    public Request(String query, String perspective, String language, String sortBy, String category){
        this.query = query;
        this.perspective = perspective;
        this.language = language;
        this.sortBy = sortBy;
        this.category = category;
    }

    public String getCategory(){return category;}

    public String getLanguage() {
        return language;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getQuery() {
        return query;
    }

    public String getPerspective() {
        return perspective;
    }

    public boolean equals(Request request){
        return query.equals(request.getQuery()) && perspective.equals(request.getPerspective()) &&
                language.equals(request.getLanguage()) && sortBy.equals(request.getSortBy())
                && category.equals(request.getCategory());
    }
}
