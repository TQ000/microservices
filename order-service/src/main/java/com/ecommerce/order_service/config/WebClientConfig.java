package com.ecommerce.order_service.config;

import com.ecommerce.order_service.service.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    /**
     * Crea el cliente HTTP con la URL de inventario configurada para el entorno.
     */
    @Bean
    public WebClient inventoryWebClient(WebClient.Builder builder,
            @Value("${clients.inventory.base-url}") String baseUrl) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Crea el proxy de inventario con un tiempo máximo para cada llamada.
     */
    @Bean
    public InventoryClient inventoryClient(WebClient inventoryWebClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                            .builderFor(WebClientAdapter.create(inventoryWebClient))
                                            .build();

        return factory.createClient(InventoryClient.class);
    }


}
