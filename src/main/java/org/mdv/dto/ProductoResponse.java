package org.mdv.dto;

import lombok.Builder;
import org.mdv.model.Producto;

import java.math.BigDecimal;

@Builder
public record ProductoResponse(
        String codigo,
        String descripcion,
        BigDecimal precioRecomendado,
        int existencias,
        CategoriaResponse categoria
) {
    public static ProductoResponse fromEntity(Producto producto) {
        return new ProductoResponse(
                producto.getCodigo(),
                producto.getDescripcion(),
                producto.getPrecioRecomendado(),
                producto.getExistencias(),
                CategoriaResponse.fromEntity(producto.getCategoria())
        );
    }

    public static Producto toEntity(ProductoResponse productoResponse) {
        Producto producto = new Producto();
        producto.setCodigo(productoResponse.codigo());
        producto.setDescripcion(productoResponse.descripcion());
        producto.setPrecioRecomendado(productoResponse.precioRecomendado());
        producto.setExistencias(productoResponse.existencias());
        producto.setCategoria(CategoriaResponse.toEntity(productoResponse.categoria()));
        return producto;
    }
}
