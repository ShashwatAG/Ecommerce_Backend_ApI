package com.playground.ecommerce.service;

import com.playground.ecommerce.dto.product.ProductRequest;
import com.playground.ecommerce.dto.product.ProductResponse;
import com.playground.ecommerce.exception.BadRequestException;
import com.playground.ecommerce.exception.ResourceNotFoundException;
import com.playground.ecommerce.model.Category;
import com.playground.ecommerce.model.Product;
import com.playground.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String keyword,
                                                Long categoryId,
                                                BigDecimal minPrice,
                                                BigDecimal maxPrice,
                                                Pageable pageable) {
        return productRepository.findAll(ProductSpecifications.filter(keyword, categoryId, minPrice, maxPrice), pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku().trim())) {
            throw new BadRequestException("A product with this SKU already exists.");
        }

        Category category = categoryService.findCategory(request.getCategoryId());
        Product product = new Product();
        applyProductRequest(product, request, category);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);
        String requestedSku = request.getSku().trim();
        if (!product.getSku().equalsIgnoreCase(requestedSku) && productRepository.findBySku(requestedSku).isPresent()) {
            throw new BadRequestException("A product with this SKU already exists.");
        }

        Category category = categoryService.findCategory(request.getCategoryId());
        applyProductRequest(product, request, category);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.delete(findProduct(id));
    }

    @Transactional(readOnly = true)
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));
    }

    public ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setActive(product.isActive());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }

    private void applyProductRequest(Product product, ProductRequest request, Category category) {
        product.setName(request.getName().trim());
        product.setSku(request.getSku().trim());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setActive(request.getActive() == null || request.getActive());
        product.setCategory(category);
    }
}
