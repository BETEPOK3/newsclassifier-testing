package ru.gobar.classifier.test.article.delete;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.ArticleDeleteClient;
import ru.gobar.classifier.database.Databaser;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;

import java.util.List;

import static ru.gobar.classifier.Endpoints.ARTICLE_DELETE;
import static ru.gobar.classifier.data.Constants.WRONG_STRING;

@DisplayName(ARTICLE_DELETE + " - удаление статей")
public class ArticleDeleteNegativeTest extends AbstractTest {

    private final ArticleDeleteClient client = new ArticleDeleteClient();
    private int notExist;

    @BeforeAll
    void prepare() {
        notExist = Databaser.getLastId() + 1;
    }

    private List<TestData> supplierWrong() {
        return List.of(new TestData("Случайная строка", WRONG_STRING, HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData("null", "null", HttpStatus.SC_UNPROCESSABLE_ENTITY),
                new TestData("Пустая строка", "", HttpStatus.SC_TEMPORARY_REDIRECT)
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989495")
    @DisplayName("[T6] /article Не успешное удаление статьи - некорректный id")
    void suckDelete() {
        AllureStepUtil stepper = new AllureStepUtil();
        supplierWrong().forEach(data -> stepper.runStep(data.name, () -> client.delete(data.param).assertThat().statusCode(data.status)));
        stepper.check();
    }

    private List<TestData> supplierSemiWrong() {
        return List.of(new TestData("Не существующий id", notExist, HttpStatus.SC_OK),
                new TestData("-1", -1, HttpStatus.SC_OK)
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/989494")
    @DisplayName("[T7] /article Не успешное удаление статьи - несуществующие id")
    void semiSuckDelete() {
        AllureStepUtil stepper = new AllureStepUtil();
        supplierSemiWrong().forEach(data -> stepper.runStep(data.name, () -> client.delete(data.param).assertThat().statusCode(data.status)));
        stepper.check();
    }

    private static class TestData {

        private final String name;
        private final Object param;
        private final int status;

        public TestData(String name, Object param, int status) {
            this.name = name;
            this.param = param;
            this.status = status;
        }
    }
}
