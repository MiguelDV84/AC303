package org.mdv.dto;

import jakarta.validation.constraints.NotEmpty;

public record CategoriaRequest (
        @NotEmpty(message = "El nombre de la categoria no puede estar vacio")
        String nombre) {
}
