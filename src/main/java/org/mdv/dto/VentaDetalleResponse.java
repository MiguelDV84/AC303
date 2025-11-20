package org.mdv.dto;

import org.mdv.model.Venta;
import org.mdv.model.VentaDetalle;

import java.math.BigDecimal;

public record VentaDetalleResponse(
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal totalLinea,
        ProductoResponse producto
) {
    public static VentaDetalleResponse fromEntity(VentaDetalle ventaDetalle) {
        return new VentaDetalleResponse(
                ventaDetalle.getCantidad(),
                ventaDetalle.getPrecioUnitario(),
                ventaDetalle.getTotalLinea(),
                ProductoResponse.fromEntity(ventaDetalle.getProducto())
        );
    }

    public static VentaDetalle toEntity(VentaDetalleResponse ventaDetalleResponse) {
        VentaDetalle ventaDetalle = new VentaDetalle();
        ventaDetalle.setCantidad(ventaDetalleResponse.cantidad());
        ventaDetalle.setPrecioUnitario(ventaDetalleResponse.precioUnitario());
        ventaDetalle.setTotalLinea(ventaDetalleResponse.totalLinea());
        ventaDetalle.setProducto(ProductoResponse.toEntity(ventaDetalleResponse.producto()));
        return ventaDetalle;
    }
}
