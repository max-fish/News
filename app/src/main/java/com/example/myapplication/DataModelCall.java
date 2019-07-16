package com.example.myapplication;

import java.util.List;

public class DataModelCall {

    private String status;
    private String totalResults;
    private List<DataModel> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<DataModel> getArticles() {
        return articles;
    }

    public void setArticles(List<DataModel> articles) {
        this.articles = articles;
    }

}
