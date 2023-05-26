package ru.gobar.classifier.api.client;

import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.ARTICLE_GET;

public class ArticleGetClient extends AbstractClient {

    public static final String PARAM = "article_id";

    public ArticleGetClient() {
        super(ARTICLE_GET);
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

    public ValidatableResponse get(Integer id) {
        return sendGet(Map.of(PARAM, id));
    }
}
