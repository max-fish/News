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

    static final String BUSINESS_CATEGORY = "business";
    static final String ENTERTAINMENT_CATEGORY = "entertainment";
    static final String GENERAL_CATEGORY = "general";
    static final String HEALTH_CATEGORY = "health";
    static final String SCIENCE_CATEGORY = "science";
    static final String SPORTS_CATEGORY = "sports";
    static final String TECHNOLOGY_CATEGORY = "technology";
}
