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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SeedDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createUserIfMissing("Admin User", "admin@shop.local", "Admin@123", Role.ADMIN);
        createUserIfMissing("Demo Customer", "customer@shop.local", "Customer@123", Role.USER);

        Category electronics = createCategoryIfMissing("Electronics", "Devices, accessories, and smart gadgets.");
        Category fashion = createCategoryIfMissing("Fashion", "Everyday wear, premium layers, and accessories.");
        Category home = createCategoryIfMissing("Home Essentials", "Functional upgrades for home and workspace.");
        Category gaming = createCategoryIfMissing("Gaming", "Consoles, accessories, and immersive desk setups.");
        Category books = createCategoryIfMissing("Books", "Programming reads, design references, and long-form learning.");
        Category fitness = createCategoryIfMissing("Fitness", "Home workout gear and recovery essentials.");
        Category office = createCategoryIfMissing("Office Setup", "Desk tools for focused work and ergonomic comfort.");
        Category travel = createCategoryIfMissing("Travel", "Compact carry gear built for movement and utility.");

        createProductIfMissing("ELEC-1001", "Noise Cancelling Headphones", "Premium wireless headphones with 30-hour battery life.", new BigDecimal("149.99"), 25, electronics, "https://example.com/images/headphones.jpg");
        createProductIfMissing("ELEC-1002", "4K Smart Monitor", "27-inch 4K productivity monitor with USB-C docking.", new BigDecimal("329.00"), 12, electronics, "https://example.com/images/monitor.jpg");
        createProductIfMissing("ELEC-1003", "Portable SSD 1TB", "Pocket-sized NVMe storage for fast backups and transfers.", new BigDecimal("119.00"), 34, electronics, "https://example.com/images/ssd.jpg");
        createProductIfMissing("ELEC-1004", "Smart Home Speaker", "Voice assistant speaker with room-filling sound and routines.", new BigDecimal("79.00"), 28, electronics, "https://example.com/images/speaker.jpg");
        createProductIfMissing("ELEC-1005", "Wireless Charging Stand", "Fast charging stand that supports portrait and landscape viewing.", new BigDecimal("36.00"), 65, electronics, "https://example.com/images/charging-stand.jpg");
        createProductIfMissing("ELEC-1006", "1080p Streaming Webcam", "Sharp low-light webcam with dual microphones for calls and streaming.", new BigDecimal("69.50"), 29, electronics, "https://example.com/images/webcam.jpg");
        createProductIfMissing("ELEC-1007", "Bluetooth Tracker Set", "Compact trackers to keep keys, wallets, and luggage in reach.", new BigDecimal("42.00"), 47, electronics, "https://example.com/images/tracker.jpg");

        createProductIfMissing("FASH-2001", "Minimal Utility Jacket", "Lightweight jacket designed for travel and everyday wear.", new BigDecimal("89.50"), 40, fashion, "https://example.com/images/jacket.jpg");
        createProductIfMissing("FASH-2002", "Canvas Everyday Tote", "Structured tote bag with padded laptop sleeve and bottle pockets.", new BigDecimal("44.90"), 52, fashion, "https://example.com/images/tote.jpg");
        createProductIfMissing("FASH-2003", "Classic Leather Sneakers", "Low-profile sneakers built for all-day city walks.", new BigDecimal("94.00"), 31, fashion, "https://example.com/images/sneakers.jpg");
        createProductIfMissing("FASH-2004", "Merino Knit Sweater", "Soft midweight sweater designed for layering through cooler evenings.", new BigDecimal("72.00"), 26, fashion, "https://example.com/images/sweater.jpg");
        createProductIfMissing("FASH-2005", "Daily Essential Cap", "Minimal six-panel cap with breathable cotton construction.", new BigDecimal("19.99"), 77, fashion, "https://example.com/images/cap.jpg");
        createProductIfMissing("FASH-2006", "Travel Chino Pants", "Stretch chinos built for commuting, flights, and everyday comfort.", new BigDecimal("58.00"), 33, fashion, "https://example.com/images/chinos.jpg");

        createProductIfMissing("HOME-3001", "Ergonomic Desk Lamp", "Adjustable LED lamp with warm and cool light modes.", new BigDecimal("39.99"), 60, home, "https://example.com/images/lamp.jpg");
        createProductIfMissing("HOME-3002", "Stoneware Brew Mug", "Double-walled ceramic mug that keeps drinks warm longer.", new BigDecimal("24.50"), 80, home, "https://example.com/images/mug.jpg");
        createProductIfMissing("HOME-3003", "Air Purifier Mini", "Compact purifier ideal for bedrooms and work corners.", new BigDecimal("129.00"), 17, home, "https://example.com/images/purifier.jpg");
        createProductIfMissing("HOME-3004", "Soft Throw Blanket", "Textured blanket for sofas, reading corners, and late-night work sessions.", new BigDecimal("32.00"), 46, home, "https://example.com/images/blanket.jpg");
        createProductIfMissing("HOME-3005", "Aroma Diffuser", "Quiet diffuser with timer modes and warm ambient glow.", new BigDecimal("27.99"), 54, home, "https://example.com/images/diffuser.jpg");
        createProductIfMissing("HOME-3006", "Wall Shelf Trio", "Floating shelf set for books, decor, and compact storage.", new BigDecimal("61.00"), 19, home, "https://example.com/images/shelves.jpg");

        createProductIfMissing("GAME-4001", "Mechanical Keyboard", "Hot-swappable keyboard with tactile switches and gasket mount.", new BigDecimal("82.00"), 23, gaming, "https://example.com/images/keyboard.jpg");
        createProductIfMissing("GAME-4002", "Ultra-Light Gaming Mouse", "Responsive wireless mouse tuned for competitive play.", new BigDecimal("59.99"), 44, gaming, "https://example.com/images/mouse.jpg");
        createProductIfMissing("GAME-4003", "Extended Desk Mat", "Large stitched desk mat with smooth control surface.", new BigDecimal("21.00"), 71, gaming, "https://example.com/images/deskmat.jpg");
        createProductIfMissing("GAME-4004", "Pro Controller Dock", "Charging dock with quick-swap slots for console controllers.", new BigDecimal("34.00"), 36, gaming, "https://example.com/images/controller-dock.jpg");
        createProductIfMissing("GAME-4005", "RGB Light Bars", "Pair of reactive desk light bars for immersive play setups.", new BigDecimal("49.00"), 27, gaming, "https://example.com/images/light-bars.jpg");
        createProductIfMissing("GAME-4006", "Gaming Headset Stand", "Weighted stand with USB hub for keeping desk space clean.", new BigDecimal("25.50"), 58, gaming, "https://example.com/images/headset-stand.jpg");

        createProductIfMissing("BOOK-5001", "Java Design Patterns", "A practical guide to object-oriented design patterns in Java.", new BigDecimal("29.99"), 50, books, "https://example.com/images/java-book.jpg");
        createProductIfMissing("BOOK-5002", "System Design Notes", "Architecture field notes for APIs, scale, and reliability.", new BigDecimal("34.00"), 37, books, "https://example.com/images/system-design.jpg");
        createProductIfMissing("BOOK-5003", "Spring Boot Recipes", "Hands-on patterns for security, persistence, and testing.", new BigDecimal("27.50"), 41, books, "https://example.com/images/spring-book.jpg");
        createProductIfMissing("BOOK-5004", "Clean API Design", "Readable API design principles for maintainable backend systems.", new BigDecimal("24.00"), 63, books, "https://example.com/images/api-design.jpg");
        createProductIfMissing("BOOK-5005", "PostgreSQL in Practice", "Real-world database tuning, indexing, and query optimization patterns.", new BigDecimal("31.25"), 39, books, "https://example.com/images/postgres-book.jpg");
        createProductIfMissing("BOOK-5006", "Frontend Systems Handbook", "A visual guide to modern React workflows and UI architecture.", new BigDecimal("28.50"), 34, books, "https://example.com/images/frontend-book.jpg");

        createProductIfMissing("FIT-6001", "Adjustable Kettlebell", "Space-saving kettlebell with selectable weight plates.", new BigDecimal("139.00"), 14, fitness, "https://example.com/images/kettlebell.jpg");
        createProductIfMissing("FIT-6002", "Yoga Flow Mat", "High-grip mat with dense cushioning for home practice.", new BigDecimal("35.00"), 66, fitness, "https://example.com/images/yoga-mat.jpg");
        createProductIfMissing("FIT-6003", "Recovery Foam Roller", "Textured roller for warmups and post-workout recovery.", new BigDecimal("19.75"), 92, fitness, "https://example.com/images/roller.jpg");
        createProductIfMissing("FIT-6004", "Resistance Band Pack", "Five-band strength kit for mobility, warmups, and travel workouts.", new BigDecimal("22.00"), 88, fitness, "https://example.com/images/bands.jpg");
        createProductIfMissing("FIT-6005", "Jump Rope Pro", "Smooth-bearing jump rope with adjustable cable length.", new BigDecimal("17.50"), 73, fitness, "https://example.com/images/jump-rope.jpg");
        createProductIfMissing("FIT-6006", "Massage Ball Set", "Dual-density massage balls for feet, shoulders, and recovery days.", new BigDecimal("14.99"), 95, fitness, "https://example.com/images/massage-balls.jpg");

        createProductIfMissing("OFF-7001", "Ergonomic Footrest", "Adjustable footrest that improves posture at long desk sessions.", new BigDecimal("48.00"), 26, office, "https://example.com/images/footrest.jpg");
        createProductIfMissing("OFF-7002", "Cable Dock Organizer", "Walnut and aluminum organizer for charging and desk declutter.", new BigDecimal("31.00"), 39, office, "https://example.com/images/organizer.jpg");
        createProductIfMissing("OFF-7003", "Notebook Set", "Three premium dotted notebooks for planning and deep work.", new BigDecimal("18.00"), 108, office, "https://example.com/images/notebooks.jpg");
        createProductIfMissing("OFF-7004", "Vertical Laptop Stand", "Aluminum stand that frees desk space while docking your laptop.", new BigDecimal("28.00"), 42, office, "https://example.com/images/laptop-stand.jpg");
        createProductIfMissing("OFF-7005", "Focus Timer Cube", "Desk timer with tactile presets for sprint-based work sessions.", new BigDecimal("16.50"), 69, office, "https://example.com/images/timer-cube.jpg");
        createProductIfMissing("OFF-7006", "Monitor Riser Shelf", "Minimal shelf that creates screen height and hidden storage space.", new BigDecimal("57.00"), 24, office, "https://example.com/images/monitor-riser.jpg");

        createProductIfMissing("TRAV-8001", "Weekend Carry Duffel", "Weather-resistant duffel with shoe compartment and laptop slot.", new BigDecimal("96.00"), 22, travel, "https://example.com/images/duffel.jpg");
        createProductIfMissing("TRAV-8002", "Packing Cube Kit", "Compression cube set for efficient luggage organization.", new BigDecimal("26.00"), 74, travel, "https://example.com/images/packing-cubes.jpg");
        createProductIfMissing("TRAV-8003", "Universal Travel Adapter", "Multi-region fast-charging adapter with USB-C output.", new BigDecimal("33.50"), 58, travel, "https://example.com/images/adapter.jpg");
        createProductIfMissing("TRAV-8004", "Passport Wallet", "Slim organizer for travel documents, cards, and boarding passes.", new BigDecimal("22.00"), 64, travel, "https://example.com/images/passport-wallet.jpg");
        createProductIfMissing("TRAV-8005", "Neck Pillow Air", "Inflatable travel pillow with compact folded form factor.", new BigDecimal("18.50"), 83, travel, "https://example.com/images/neck-pillow.jpg");
        createProductIfMissing("TRAV-8006", "Carry-On Tech Pouch", "Structured accessory pouch for chargers, cables, and adapters.", new BigDecimal("29.00"), 56, travel, "https://example.com/images/tech-pouch.jpg");
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
