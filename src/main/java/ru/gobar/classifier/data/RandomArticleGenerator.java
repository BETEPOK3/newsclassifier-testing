package ru.gobar.classifier.data;

import com.github.javafaker.Faker;
import ru.gobar.classifier.model.Article;
import ru.gobar.classifier.model.Category;

import java.time.ZoneId;
import java.util.*;

public class RandomArticleGenerator {

    private static Faker faker = new Faker(new Locale("ru"));

    public static Article randomArticleWithAllFields() {
        Random random = new Random();
        int k = random.nextInt(4);
        Set<String> keywords = new HashSet<>();
        for (int i = 0; i < k; ++i) {
            keywords.add(faker.food().ingredient());
        }

        int c = random.nextInt(3) + 1;
        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < c; ++i) {
            categories.add(new Category().setName(UUID.randomUUID().toString()));
        }

        return new Article()
                .setTitle(UUID.randomUUID().toString())
                .setAuthor(UUID.randomUUID().toString())
                .setKeywords(keywords)
                .setDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .setText(faker.regexify("(([a-zA-Zа-яА-ЯёЁ0-9]{10})[ ]){"+ random.nextInt(60) + "}\\."))
                .setCategories(categories);
    }

    public static Article randomArticleBase() {
        Random random = new Random();

        int c = random.nextInt(3) + 1;
        Set<Category> categories = new HashSet<>();
        for (int i = 0; i < c; ++i) {
            categories.add(new Category().setName(UUID.randomUUID().toString()));
        }

        return new Article()
                .setTitle(UUID.randomUUID().toString())
                .setDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .setText(faker.regexify("(([a-zA-Zа-яА-ЯёЁ0-9]{10})[ ]){"+ random.nextInt(60) + "}\\."))
                .setCategories(categories)
                .setKeywords(new HashSet<>());
    }
}
