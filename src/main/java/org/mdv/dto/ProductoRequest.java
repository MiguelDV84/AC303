package org.mdv.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProductoRequest(
        @NotEmpty(message = "El código del producto es obligatorio")
        String codigo,

        @NotEmpty(message = "El nombre del producto no puede estar vacío")
        String descripcion,

        @NotEmpty(message = "El precio recomendado no puede ser vacío")
        @PositiveOrZero(message = "El precio no puede ser negativo")
        BigDecimal precioRecomendado,

        @NotEmpty(message = "Las existencias recomendado no pueden ser vacías")
        @PositiveOrZero(message = "Las existencias no pueden ser negativas")
        int existencias,

        Long categoriaId
) {
}
