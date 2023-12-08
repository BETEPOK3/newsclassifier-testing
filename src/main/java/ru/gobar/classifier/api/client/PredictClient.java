package ru.gobar.classifier.api.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Allure;
import io.restassured.response.ValidatableResponse;
import ru.gobar.classifier.api.request.ArticleCreateRequest;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.gobar.classifier.Endpoints.PREDICT;

public class PredictClient extends AbstractClient {

    public PredictClient() {
        super(PREDICT);
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

    public ValidatableResponse post(String request) {
        return sendPost(Map.of("article_text", request));
    }
}
