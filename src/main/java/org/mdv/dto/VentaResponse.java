package org.mdv.dto;

import lombok.Builder;
import org.mdv.model.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record VentaResponse(
        BigDecimal importeTotal,
        LocalDate fechaVenta,
        ClienteResponse cliente,
        List<VentaDetalleResponse> ventaDetalle
) {
    public static VentaResponse fromEntity(Venta venta) {
        return VentaResponse.builder()
                .importeTotal(venta.getImporteTotal())
                .fechaVenta(venta.getFechaVenta())
                .cliente(ClienteResponse.fromEntity(venta.getCliente()))
                .ventaDetalle(venta.getVentaDetalles()
                        .stream()
                        .map(VentaDetalleResponse::fromEntity)
                        .toList())
                .build();

    }

    public static Venta toEntity(VentaResponse response) {
        return Venta.builder()
                .importeTotal(response.importeTotal)
                .fechaVenta(response.fechaVenta)
                .cliente(ClienteResponse.toEntity(response.cliente))
                .ventaDetalles(response.ventaDetalle
                        .stream()
                        .map(VentaDetalleResponse::toEntity)
                        .toList())
                .build();
    }
}
