package ru.gobar.classifier.data;

import com.github.javafaker.Faker;
import ru.gobar.classifier.model.Category;

import java.util.*;

public class RandomCategoryGenerator {

    private static Faker faker = new Faker(new Locale("ru"));

    public static Category randomCategory() {
        return new Category().setName(UUID.randomUUID().toString());
    }

}
