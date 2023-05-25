package ru.gobar.classifier.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.gobar.classifier.api.request.ArticleCreateRequest;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.ARTICLE_CREATE;

public class ArticleCreateClient extends AbstractClient {

    private ValidatableResponse sendPost(Object request) {
        return Allure.step("POST " + ARTICLE_CREATE, () -> given()
                .spec(specs())
                .body(request)
                .post(ARTICLE_CREATE)
                .then());
    }

    public ValidatableResponse post(JsonNode request) {
        return sendPost(request);
    }

    public ValidatableResponse post(ArticleCreateRequest request) {
        return sendPost(request);
    }
}
