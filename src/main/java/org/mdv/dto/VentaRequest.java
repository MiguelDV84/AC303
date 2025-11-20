package org.mdv.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VentaRequest(
        BigDecimal importeTotal,
        LocalDate fechaVenta,
        String clienteDni,
        List<VentaDetalleRequest> ventaDetalleRequests
) {
}
