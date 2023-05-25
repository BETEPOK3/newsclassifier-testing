package ru.gobar.classifier.api.client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class AbstractClient {

    protected RequestSpecification specs() {
        return new RequestSpecBuilder()
                .setAccept("application/json")
                .setContentType(ContentType.JSON)
                .build();
    }

}
