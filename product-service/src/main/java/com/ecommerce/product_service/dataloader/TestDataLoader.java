package com.ecommerce.product_service.dataloader;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class TestDataLoader implements CommandLineRunner {
    
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        

        Product product = Product.builder()
            .name("Laptop")
            .description("Laptop Gaming")
            .price(BigDecimal.valueOf(1000.00))
            .build();

        productRepository.save(product);
        
        System.out.println("Datos iniciales cargados" + product.getName());
    }
}
