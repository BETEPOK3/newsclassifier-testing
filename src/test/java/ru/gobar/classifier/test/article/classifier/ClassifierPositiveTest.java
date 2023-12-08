package ru.gobar.classifier.test.article.classifier;

import io.qameta.allure.TmsLink;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gobar.classifier.api.client.PredictClient;
import ru.gobar.classifier.api.response.PredictResponse;
import ru.gobar.classifier.model.Category;
import ru.gobar.classifier.test.AbstractTest;
import ru.gobar.classifier.util.AllureStepUtil;
import ru.gobar.classifier.util.AssertUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.gobar.classifier.Endpoints.PREDICT;

@DisplayName(PREDICT + " - классификатор")
public class ClassifierPositiveTest extends AbstractTest {

    private final String ANIME_TEXT_PATH = "AnimeText.txt";
    private final String TECH_AND_ECONOMY_TEXT_PATH = "TechAndEconomy.txt";
    private final PredictClient predictClient = new PredictClient();

    private String animeText;
    private String techAndEconomyText;

    @BeforeAll
    void setUp() {
        animeText = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassifierPositiveTest.class.getClassLoader().getResourceAsStream(ANIME_TEXT_PATH))))
                .lines().collect(Collectors.joining("\n"));
        techAndEconomyText = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassifierPositiveTest.class.getClassLoader().getResourceAsStream(TECH_AND_ECONOMY_TEXT_PATH))))
                .lines().collect(Collectors.joining("\n"));
    }

    private Map<String, List<String>> getCases() {
        return Map.of(
                animeText, List.of(Category.RealCats.ANIME.getName()),
                techAndEconomyText, List.of(Category.RealCats.TECH.getName(), Category.RealCats.ECONOMICS.getName())
        );
    }

    @Test
    @TmsLink("https://www.hostedredmine.com/attachments/1098459")
    @DisplayName("[T32] /article/predict Успешное определение категории статьи")
    void testPredict() {
        AllureStepUtil stepUtil = new AllureStepUtil();
        getCases().forEach((t, c) -> stepUtil.runStep(c.get(0), () -> {
            PredictResponse response = predictClient.post(t)
                    .statusCode(HttpStatus.SC_OK)
                    .extract()
                    .as(PredictResponse.class);

            c.forEach(cat -> Assertions.assertTrue(response.getCategories123().contains(cat)));
        }));
        stepUtil.check();
    }
}
