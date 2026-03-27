package com.playground.ecommerce.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryRequest {

    @NotBlank(message = "Category name is required.")
    @Size(max = 120, message = "Category name must be at most 120 characters.")
    private String name;

    @Size(max = 1000, message = "Description must be at most 1000 characters.")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

