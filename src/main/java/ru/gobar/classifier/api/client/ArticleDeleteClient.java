package ru.gobar.classifier.api.client;

import io.qameta.allure.Allure;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.ARTICLE_DELETE;

public class ArticleDeleteClient extends AbstractClient {

    private ValidatableResponse sendDelete(Object id) {
        return Allure.step("POST " + ARTICLE_DELETE + "{article_id}", () -> given()
                .spec(specs())
                .delete(ARTICLE_DELETE + id)
                .then());
    }

    public ValidatableResponse delete(int id) {
        return sendDelete(id);
    }

    public ValidatableResponse delete(Object id) {
        return sendDelete(id);
    }
}
