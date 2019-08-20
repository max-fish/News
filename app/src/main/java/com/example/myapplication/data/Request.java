package com.example.myapplication.data;


public class Request {

    private String query;
    private String source;
    private String language;
    private String sortBy;
    private String category;

    public Request(String query, String source, String language, String sortBy, String category){
        this.query = query;
        this.source = source;
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

    public String getSource() {
        return source;
    }


    public boolean notEquals(Request request){
        return !query.equals(request.getQuery()) || !source.equals(request.getSource()) ||
                !language.equals(request.getLanguage()) || !sortBy.equals(request.getSortBy())
                || !category.equals(request.getCategory());
    }
}
