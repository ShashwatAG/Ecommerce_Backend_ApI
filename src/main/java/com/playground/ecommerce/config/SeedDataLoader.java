package com.playground.ecommerce.config;

import com.playground.ecommerce.model.Cart;
import com.playground.ecommerce.model.Category;
import com.playground.ecommerce.model.Product;
import com.playground.ecommerce.model.Role;
import com.playground.ecommerce.model.User;
import com.playground.ecommerce.repository.CartRepository;
import com.playground.ecommerce.repository.CategoryRepository;
import com.playground.ecommerce.repository.ProductRepository;
import com.playground.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SeedDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedDataLoader(UserRepository userRepository,
                          CartRepository cartRepository,
                          CategoryRepository categoryRepository,
                          ProductRepository productRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createUserIfMissing("Admin User", "admin@shop.local", "Admin@123", Role.ADMIN);
        createUserIfMissing("Demo Customer", "customer@shop.local", "Customer@123", Role.CUSTOMER);

        Category electronics = createCategoryIfMissing("Electronics", "Devices, accessories, and smart gadgets.");
        Category fashion = createCategoryIfMissing("Fashion", "Everyday wear, premium layers, and accessories.");
        Category home = createCategoryIfMissing("Home Essentials", "Functional upgrades for home and workspace.");

        createProductIfMissing("ELEC-1001", "Noise Cancelling Headphones", "Premium wireless headphones with 30-hour battery life.", new BigDecimal("149.99"), 25, electronics, "https://example.com/images/headphones.jpg");
        createProductIfMissing("ELEC-1002", "4K Smart Monitor", "27-inch 4K productivity monitor with USB-C docking.", new BigDecimal("329.00"), 12, electronics, "https://example.com/images/monitor.jpg");
        createProductIfMissing("FASH-2001", "Minimal Utility Jacket", "Lightweight jacket designed for travel and everyday wear.", new BigDecimal("89.50"), 40, fashion, "https://example.com/images/jacket.jpg");
        createProductIfMissing("HOME-3001", "Ergonomic Desk Lamp", "Adjustable LED lamp with warm and cool light modes.", new BigDecimal("39.99"), 60, home, "https://example.com/images/lamp.jpg");
    }

    private void createUserIfMissing(String fullName, String email, String rawPassword, Role role) {
        userRepository.findByEmail(email.toLowerCase()).orElseGet(() -> {
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email.toLowerCase());
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            user.setEnabled(true);
            User savedUser = userRepository.save(user);

            Cart cart = new Cart();
            cart.setUser(savedUser);
            savedUser.setCart(cart);
            cartRepository.save(cart);
            return savedUser;
        });
    }

    private Category createCategoryIfMissing(String name, String description) {
        return categoryRepository.findByNameIgnoreCase(name).orElseGet(() -> {
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            return categoryRepository.save(category);
        });
    }

    private void createProductIfMissing(String sku,
                                        String name,
                                        String description,
                                        BigDecimal price,
                                        int stockQuantity,
                                        Category category,
                                        String imageUrl) {
        productRepository.findBySku(sku).orElseGet(() -> {
            Product product = new Product();
            product.setSku(sku);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            product.setImageUrl(imageUrl);
            product.setActive(true);
            return productRepository.save(product);
        });
    }
}

