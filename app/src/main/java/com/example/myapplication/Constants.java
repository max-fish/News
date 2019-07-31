package com.example.myapplication;

public class Constants {
    public enum NewsType{
        ALL, RECCOMENDED;
    }

    static final String CNN_SOURCE = "cnn";
    static final String BBC_SOURCE = "bbc-news";
    static final String FOX_SOURCE = "fox-news";
    static final String MSNBC_SOURCE = "msnbc";

    static final String EN_LANGUAGE = "en";
    static final String RU_LANGUAGE = "ru";
    static final String FR_LANGUAGE = "fr";
    static final String ES_LANGUAGE = "es";

    static final String PUBLISHED_AT_SORT = "publishedAt";
    static final String RELEVANCY_SORT = "relevancy";
    static final String POPULARITY_SORT = "popularity";
}
