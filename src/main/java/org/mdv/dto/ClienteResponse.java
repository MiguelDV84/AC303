package org.mdv.dto;

import lombok.Builder;
import org.mdv.model.Cliente;

@Builder
public record ClienteResponse(
        String dni,
        String nombre,
        String apellidos,
        String telefono,
        String dirHabitual,
        String dirEnvio
) {
    public static ClienteResponse fromEntity(Cliente cliente) {
        return ClienteResponse.builder()
                .dni(cliente.getDni())
                .nombre(cliente.getNombre())
                .apellidos(cliente.getApellidos())
                .telefono(cliente.getTelefono())
                .dirHabitual(cliente.getDirHabitual())
                .dirEnvio(cliente.getDirEnvio())
                .build();
    }

    public static Cliente toEntity(ClienteResponse response) {
        return Cliente.builder()
                .dni(response.dni)
                .nombre(response.nombre)
                .apellidos(response.apellidos)
                .telefono(response.telefono)
                .dirHabitual(response.dirHabitual)
                .dirEnvio(response.dirEnvio)
                .build();
    }
}
