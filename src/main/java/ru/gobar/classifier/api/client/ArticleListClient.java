package ru.gobar.classifier.api.client;

import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ArticleListClient extends AbstractClient{

    public static final String CATEGORY_PARAM = "category";
    public static final String SORT_PARAM = "sort_by";
    public static final String TEXT_PARAM = "search_text";
    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "page_size";

    public ArticleListClient(String endpoint) {
        super(endpoint);
    }

    private ValidatableResponse sendGet(Map<String, ?> params) {
        return Allure.step("POST " + ENDPOINT, () -> given()
                .spec(specs())
                .params(params)
                .get(ENDPOINT)
                .then());
    }

    public ValidatableResponse get(Map<String, ?> params) {
        return sendGet(params);
    }

    public enum SortBy {
        title,
        author,
        date
    }
}
