package ru.gobar.classifier.test;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import ru.gobar.classifier.Endpoints;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.DecoderConfig.decoderConfig;

public class SuiteWrapper implements BeforeAllCallback {

    public void beforeAllTests(ExtensionContext context) {
        Awaitility.setDefaultPollDelay(1, TimeUnit.SECONDS);
        Awaitility.setDefaultPollInterval(5, TimeUnit.SECONDS);
        Awaitility.setDefaultTimeout(180, TimeUnit.SECONDS);
        RestAssured.baseURI = Endpoints.BASE_HOST;
        RestAssured.config = RestAssured.config()
                .redirect(redirectConfig().followRedirects(false))
                .decoderConfig(decoderConfig().defaultContentCharset(StandardCharsets.UTF_8));
        RestAssured.filters(List.of(new AllureRestAssured(), new RequestLoggingFilter(),
                new ResponseLoggingFilter()));
    }

    public void afterAllTests() {

    }

    @Override
    public void beforeAll(ExtensionContext context) {
        context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).
                getOrComputeIfAbsent(this.getClass(), i -> {
                    beforeAllTests(context);
                    return (ExtensionContext.Store.CloseableResource) this::afterAllTests;
                });
    }
}
