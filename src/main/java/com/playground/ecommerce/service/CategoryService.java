package com.playground.ecommerce.service;

import com.playground.ecommerce.dto.category.CategoryRequest;
import com.playground.ecommerce.dto.category.CategoryResponse;
import com.playground.ecommerce.exception.BadRequestException;
import com.playground.ecommerce.exception.ResourceNotFoundException;
import com.playground.ecommerce.model.Category;
import com.playground.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(Long id) {
        return CategoryResponse.from(findCategory(id));
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new BadRequestException("A category with this name already exists.");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategory(id);
        String name = request.getName().trim();

        if (!category.getName().equalsIgnoreCase(name) && categoryRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException("A category with this name already exists.");
        }

        category.setName(name);
        category.setDescription(request.getDescription());
        return CategoryResponse.from(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategory(id);
        if (!category.getProducts().isEmpty()) {
            throw new BadRequestException("Cannot delete category with existing products.");
        }
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));
    }
}
