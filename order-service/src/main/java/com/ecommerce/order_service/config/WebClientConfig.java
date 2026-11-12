package com.ecommerce.order_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.ecommerce.order_service.service.client.InventoryClient;

@Configuration
public class WebClientConfig {

    /**
     * Crea el cliente HTTP con la URL de inventario configurada para el entorno.
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder inventoryWebClient() {
        return WebClient.builder();
    }

    /**
     * Crea el proxy de inventario con un tiempo máximo para cada llamada.
     */
    @Bean
    public InventoryClient inventoryClient(WebClient.Builder builder) {
        WebClient webClient1 = builder.baseUrl("http://INVENTORY-SERVICE").build();
        
        
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                            .builderFor(WebClientAdapter.create(webClient1))
                                            .build();

        return factory.createClient(InventoryClient.class);
    }


}
