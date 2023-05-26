package ru.gobar.classifier.dao;

import ru.gobar.classifier.model.Category;

public interface CategoryDao {

    Category getById(int id);

    Category getByName(String name);

    Category persist(Category category);

}
