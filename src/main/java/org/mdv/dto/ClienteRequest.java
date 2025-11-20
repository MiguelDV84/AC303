package org.mdv.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record ClienteRequest(
        @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "El DNI del cliente no tiene un formato válido")
        @NotEmpty(message = "El dni no puede estar vacío")
        String dni,

        @NotEmpty(message = "El nombre no puede estar vacío")
        String nombre,

        @NotEmpty(message = "Los apellidos no pueden estar vacíos")
        String apellidos,

        @NotEmpty(message = "El teléfono no puede estar vacío")
        String telefono,

        String dirHabitual,

        @NotEmpty(message = "La dirección de envío no puede estar vacía")
        String dirEnvio
) {
}
