package ru.gobar.classifier.test.article.get;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleGetClient;
import ru.gobar.classifier.database.Databaser;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;

import java.util.List;
import java.util.Map;

import static ru.gobar.classifier.Endpoints.ARTICLE_GET;
import static ru.gobar.classifier.data.Constants.WRONG_STRING;

@DisplayName(ARTICLE_GET + " - получение статьи")
public class ArticleGetNegativeTest extends AbstractTest {

    private final ArticleGetClient client = new ArticleGetClient();

    private int notExist;

    @BeforeAll
    void prepare() {
        notExist = Databaser.getLastId() + 1;
    }

    private List<TestData> supplier() {
        return List.of(new TestData("Случайная строка", Map.of(ArticleGetClient.PARAM, WRONG_STRING)),
                new TestData("null", Map.of(ArticleGetClient.PARAM, "null")),
                new TestData("Пустая строка", Map.of(ArticleGetClient.PARAM, "")),
                new TestData("Не существующий id", Map.of(ArticleGetClient.PARAM, notExist)),
                new TestData("Не существующий id", Map.of(ArticleGetClient.PARAM, -1)),
                new TestData("Без параметра", Map.of())
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989493")
    @DisplayName("[T8] /article Неуспешное получение статьи - некорректный параметр")
    void testBase() {
        AllureStepUtil stepper = new AllureStepUtil();
        supplier().forEach(data -> stepper.runStep(data.name, () -> client.get(data.params).assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)));
        stepper.check();
    }

    private static class TestData{

        private final String name;
        private final Map<String, ?> params;

        public TestData(String name, Map<String, ?> params) {
            this.name = name;
            this.params = params;
        }
    }

}
