package com.example.myapplication;

import android.util.Log;

public class Request {

    private String query;

    private String perspective;

    public Request(String query, String perspective){
        this.query = query;
        this.perspective = perspective;
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
        return query.equals(request.getQuery()) && perspective.equals(request.getPerspective());
    }
}
