package ru.gobar.classifier.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;
import ru.gobar.classifier.api.request.ArticleCreateRequest;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.ARTICLE_UPDATE;

public class ArticleUpdateClient extends AbstractClient {

    public ArticleUpdateClient() {
        super(ARTICLE_UPDATE);
    }

    private ValidatableResponse sendPost(Object request, Object id) {
        return Allure.step("POST " + ENDPOINT, () -> given()
                .spec(specs())
                .body(request)
                .post(ENDPOINT + id)
                .then());
    }

    private ValidatableResponse sendGet(Object id) {
        return Allure.step("GET " + ENDPOINT, () -> given()
                .spec(specs())
                .get(ENDPOINT + id)
                .then());
    }

    public ValidatableResponse post(JsonNode request, int id) {
        return sendPost(request, id);
    }

    public ValidatableResponse post(ArticleCreateRequest request, int id) {
        return sendPost(request, id);
    }

    public ValidatableResponse get(Object id) {
        return sendGet(id);
    }

    public ValidatableResponse get(int id) {
        return sendGet(id);
    }
}
