package org.mdv.dto;

import lombok.Builder;
import org.mdv.model.Categoria;

@Builder
public record CategoriaResponse(
        Long id,
        String nombre
) {

    public static CategoriaResponse fromEntity(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre()
        );
    }

    public static Categoria toEntity(CategoriaResponse response) {
        Categoria categoria = new Categoria();
        categoria.setId(response.id());
        categoria.setNombre(response.nombre());
        return categoria;
    }
}
