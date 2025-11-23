package org.mdv.dto;

import java.math.BigDecimal;

public record VentaDetalleRequest(
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal totalLinea,
        String codProd,
        int ventaId
) {
}
