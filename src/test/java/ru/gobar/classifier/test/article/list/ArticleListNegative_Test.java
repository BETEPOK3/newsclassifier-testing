package ru.gobar.classifier.test.article.list;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleListClient;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;

import java.util.*;

import static ru.gobar.classifier.Endpoints.ARTICLE_LIST_INDEX;
import static ru.gobar.classifier.Endpoints.ARTICLE_LIST_ROOT;
import static ru.gobar.classifier.api.client.ArticleListClient.*;
import static ru.gobar.classifier.data.Constants.WRONG_STRING;

@DisplayName(ARTICLE_LIST_ROOT + " и " + ARTICLE_LIST_INDEX + " - получение списка статей")
public class ArticleListNegative_Test extends AbstractTest {

    private final ArticleListClient clientRoot = new ArticleListClient(ARTICLE_LIST_ROOT);
    private final ArticleListClient clientIndex = new ArticleListClient(ARTICLE_LIST_INDEX);

    private List<ArticleListClient> clients() {
        return List.of(clientRoot, clientIndex);
    }

    private List<TestData> wrongSupplier() {
        return List.of(new TestData("page -1", Map.of(PAGE_PARAM, -1), HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    @TmsLink("...")
    @DisplayName("Запрос списка статей с некорректными параметрами (400)")
    void wrongTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            AllureStepUtil inner = new AllureStepUtil();
            wrongSupplier().forEach(data -> inner.runStep(data.name, () -> c.get(data.params).
                    assertThat().statusCode(data.status)));
            inner.check();
        }));
        stepUtil.check();
    }

    private List<TestData> allowedSupplier() {
        return List.of(new TestData("category не существует", Map.of(CATEGORY_PARAM, Integer.MAX_VALUE), HttpStatus.SC_OK),
                new TestData("sort_by некорректный", Map.of(SORT_PARAM, WRONG_STRING), HttpStatus.SC_OK));
    }

    @Test
    @TmsLink("...")
    @DisplayName("Запрос списка статей с некорректными параметрами (200)")
    void allowedTest() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        clients().forEach(c -> stepUtil.runStep(c.ENDPOINT, () -> {
            AllureStepUtil inner = new AllureStepUtil();
            allowedSupplier().forEach(data -> inner.runStep(data.name, () -> c.get(data.params).
                    assertThat().statusCode(data.status)));
            inner.check();
        }));
        stepUtil.check();
    }

    private static class TestData {

        private final String name;
        private final Map<String, ?> params;
        private final int status;

        public TestData(String name, Map<String, ?> params, int status) {
            this.name = name;
            this.params = params;
            this.status = status;
        }
    }
}
