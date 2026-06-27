package com.unipacifico_ingenieria_sistemas.tienda_virtual.config;

import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Category;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Product;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Role;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.Users;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.CategoryRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.ProductRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.RoleRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.repository.UserRepository;
import com.unipacifico_ingenieria_sistemas.tienda_virtual.model.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Role adminRole = getOrCreateRole(RoleType.ROLE_ADMIN);
        Role clientRole = getOrCreateRole(RoleType.ROLE_CLIENTE);
        Role vendorRole = getOrCreateRole(RoleType.ROLE_VENDEDOR);

        if (!userRepository.existsByUsername("Luis_David")) {
            userRepository.save(Users.builder()
                .username("Luis_David")
                .email("luis.david@tienda.com")
                .password(passwordEncoder.encode("luis123"))
                .fullName("Luis David")
                .enabled(true)
                .roles(Set.of(adminRole))
                .build());
        }

        if (!userRepository.existsByUsername("Viviana_Hurtado")) {
            userRepository.save(Users.builder()
                .username("Viviana_Hurtado")
                .email("viviana.hurtado@tienda.com")
                .password(passwordEncoder.encode("viviana123"))
                .fullName("Viviana Hurtado")
                .enabled(true)
                .roles(Set.of(vendorRole))
                .build());
        }

        if (!userRepository.existsByUsername("cliente1")) {
            userRepository.save(Users.builder()
                .username("cliente1")
                .email("cliente@tienda.com")
                .password(passwordEncoder.encode("cliente123"))
                .fullName("Cliente Demo")
                .enabled(true)
                .roles(Set.of(clientRole))
                .build());
        }

        if (categoryRepository.count() == 0) {
            Category electronics = categoryRepository.save(Category.builder()
                .name("Electrónica").description("Productos tecnológicos y electrónicos").build());
            Category clothing = categoryRepository.save(Category.builder()
                .name("Ropa").description("Moda, ropa y accesorios").build());
            Category home = categoryRepository.save(Category.builder()
                .name("Hogar").description("Artículos para el hogar").build());
            Category sports = categoryRepository.save(Category.builder()
                .name("Deportes").description("Equipamiento deportivo").build());

            productRepository.save(Product.builder().name("Smartphone Galaxy X10")
                .description("Teléfono inteligente con cámara de 108 MP y batería de 5000 mAh.")
                .price(new BigDecimal("899.99")).stock(50).category(electronics).active(true).build());
            productRepository.save(Product.builder().name("Laptop UltraSlim Pro")
                .description("Portátil ultradelgado con procesador Intel Core i7 y 16 GB RAM.")
                .price(new BigDecimal("1299.99")).stock(20).category(electronics).active(true).build());
            productRepository.save(Product.builder().name("Auriculares Bluetooth NC")
                .description("Auriculares con cancelación activa de ruido y 30 h de batería.")
                .price(new BigDecimal("149.99")).stock(75).category(electronics).active(true).build());
            productRepository.save(Product.builder().name("Camiseta Casual Premium")
                .description("Camiseta de algodón 100% peinado, disponible en varios colores.")
                .price(new BigDecimal("25.99")).stock(200).category(clothing).active(true).build());
            productRepository.save(Product.builder().name("Jeans Slim Fit")
                .description("Pantalón vaquero corte slim de alta calidad.")
                .price(new BigDecimal("59.99")).stock(100).category(clothing).active(true).build());
            productRepository.save(Product.builder().name("Cafetera Automática Digital")
                .description("Cafetera con temporizador, capacidad 1.5 L y pantalla LCD.")
                .price(new BigDecimal("89.99")).stock(30).category(home).active(true).build());
            productRepository.save(Product.builder().name("Set de Sartenes Antiadherentes")
                .description("Juego de 3 sartenes de aluminio con recubrimiento antiadherente.")
                .price(new BigDecimal("49.99")).stock(40).category(home).active(true).build());
            productRepository.save(Product.builder().name("Bicicleta de Montaña 21V")
                .description("Bicicleta todoterreno con 21 velocidades y frenos de disco.")
                .price(new BigDecimal("379.99")).stock(15).category(sports).active(true).build());
        }
    }

    private Role getOrCreateRole(RoleType type) {
        return roleRepository.findByName(type).orElseGet(() ->
            roleRepository.save(Role.builder().name(type).build()));
    }
}
