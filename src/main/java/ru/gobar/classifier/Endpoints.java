package ru.gobar.classifier;

public class Endpoints {

    // base
    public static final String BASE_HOST = PropertyReader.getProperty("api.host");
    public static final String DB_HOST = PropertyReader.getProperty("pgsql.host");
    public static final String DB_NAME = PropertyReader.getProperty("pgsql.db");
    public static final String DB_USER = PropertyReader.getProperty("pgsql.user");
    public static final String DB_PASSWORD = PropertyReader.getProperty("pgsql.password");

    // endpoints
    public static final String ARTICLE_CREATE = "/article/create";
    public static final String ARTICLE_DELETE = "/article/";
    public static final String ARTICLE_GET = "/article";
    public static final String ARTICLE_LIST_ROOT = "/";
    public static final String ARTICLE_LIST_INDEX = "/index";
}
