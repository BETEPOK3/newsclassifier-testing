package ru.gobar.classifier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

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

}
