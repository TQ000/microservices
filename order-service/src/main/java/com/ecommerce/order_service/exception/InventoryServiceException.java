package com.ecommerce.order_service.exception;

/**
 * Indica que no se pudo completar una llamada al servicio de inventario.
 */
public class InventoryServiceException extends RuntimeException {

    /**
     * Conserva la causa del fallo para su diagnóstico en el servidor.
     *
     * @param cause error de comunicación o tiempo de espera agotado
     */
    public InventoryServiceException(Throwable cause) {
        super("No se pudo completar la operación con inventario", cause);
    }
}
