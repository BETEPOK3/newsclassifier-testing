package ru.gobar.classifier.api.client;

import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.ARTICLE_DELETE;

public class ArticleDeleteClient extends AbstractClient {

    public ArticleDeleteClient() {
        super(ARTICLE_DELETE);
    }

    private ValidatableResponse sendDelete(Object id) {
        return Allure.step("POST " + ENDPOINT + "{article_id}", () -> given()
                .spec(specs())
                .delete(ENDPOINT + id)
                .then());
    }

    public ValidatableResponse delete(int id) {
        return sendDelete(id);
    }

    public ValidatableResponse delete(Object id) {
        return sendDelete(id);
    }
}
