package com.example.myapplication;

import com.example.myapplication.data.Request;

public class Constants {
    public enum NewsType{
        ALL, RECOMMENDED;
    }

    public static final Request DEFAULT_REQUEST =
            new Request("", "cnn", "en", "publishedAt", "");

    public static final String CNN_SOURCE = "cnn";
    public static final String BBC_SOURCE = "bbc-news";
    public static final String FOX_SOURCE = "fox-news";
    public static final String MSNBC_SOURCE = "msnbc";

    public static final String EN_LANGUAGE = "en";
    public static final String RU_LANGUAGE = "ru";
    public static final String FR_LANGUAGE = "fr";
    public static final String ES_LANGUAGE = "es";

    public static final String PUBLISHED_AT_SORT = "publishedAt";
    public static final String RELEVANCY_SORT = "relevancy";
    public static final String POPULARITY_SORT = "popularity";

    public static final String BUSINESS_CATEGORY = "business";
    public static final String ENTERTAINMENT_CATEGORY = "entertainment";
    public static final String GENERAL_CATEGORY = "general";
    public static final String HEALTH_CATEGORY = "health";
    public static final String SCIENCE_CATEGORY = "science";
    public static final String SPORTS_CATEGORY = "sports";
    public static final String TECHNOLOGY_CATEGORY = "technology";

    public static final int FILTER_PREFERENCE_QUERY_ID = 0;
    public static final int FILTER_PREFERENCE_SOURCE_ID = 1;
    public static final int FILTER_PREFERENCE_LANGUAGE_ID = 2;
    public static final int FILTER_PREFERENCE_SORT_BY_ID = 3;
    public static final int FILTER_PREFERENCE_CATEGORY_ID = 4;
}
