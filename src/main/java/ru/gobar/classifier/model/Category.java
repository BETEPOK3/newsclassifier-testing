package ru.gobar.classifier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.Arrays;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor()
@FieldNameConstants
public class Category {

    @JsonProperty("category_id")
    private int id;
    @JsonProperty("category_name")
    private String name;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Category)) {
            return false;
        }

        Category a = (Category) object;

        return this.id == a.id && this.name.equals(a.name);
    }
}
