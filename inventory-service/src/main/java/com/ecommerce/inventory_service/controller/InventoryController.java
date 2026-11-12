package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.InventoryRequest;
import com.ecommerce.inventory_service.dto.InventoryResponse;
import com.ecommerce.inventory_service.service.InventoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@RefreshScope 
@Slf4j 
public class InventoryController {
    private final InventoryService inventoryService;

    @Value ("${inventory.allow-backorders:false}")
    private boolean allowBackorder;

    @GetMapping("/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku") String sku,@RequestParam("quantity") Integer quantity){
        if(allowBackorder){
            log.info("MODO BACKORDER ACTIVO: Autirizando stock para sku: {}", sku);
            return true;
        }
        
        return inventoryService.isInStock(sku, quantity);
    }
 
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse createInventory(@Valid @RequestBody InventoryRequest inventoryRequest){
        return inventoryService.createInventory(inventoryRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInventory(HttpServletRequest request)
    {
        System.out.println("Peticion atentida desde el puerto: " + request.getServerPort());
        return inventoryService.getAllInventory();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse updateInventory( @PathVariable Long id,@Valid @RequestBody InventoryRequest inventoryRequest){
        return inventoryService.updateInventory(id, inventoryRequest);
    }

    @PutMapping("/reduce/{sku}")
    @ResponseStatus(HttpStatus.OK)
    public String reduceStock( @PathVariable String sku, @RequestParam Integer quantity){
        inventoryService.reduceStock(sku, quantity);
        return "Stock reducido exitosamente";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable Long id){
        inventoryService.deleteInventory(id);
    }
















}
