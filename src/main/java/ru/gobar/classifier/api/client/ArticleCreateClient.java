package ru.gobar.classifier.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;
import ru.gobar.classifier.api.request.ArticleCreateRequest;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.ARTICLE_CREATE;

public class ArticleCreateClient extends AbstractClient {

    public ArticleCreateClient() {
        super(ARTICLE_CREATE);
    }

    private ValidatableResponse sendPost(Object request) {
        return Allure.step("POST " + ENDPOINT, () -> given()
                .spec(specs())
                .body(request)
                .post(ENDPOINT)
                .then());
    }

    public ValidatableResponse post(JsonNode request) {
        return sendPost(request);
    }

    public ValidatableResponse post(ArticleCreateRequest request) {
        return sendPost(request);
    }
}
