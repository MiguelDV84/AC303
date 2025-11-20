package org.mdv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "clientes")
@ToString
public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dni", nullable = false, length = 50, unique = true)
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "El DNI del cliente no tiene un formato válido")
    @NotEmpty
    private String dni;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotEmpty
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "dir_habitual", length = 255)
    private String dirHabitual;

    @Column(name = "dir_envio", length = 255)
    @NotEmpty
    private String dirEnvio;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Venta> ventas;

    public Cliente(String dni, String nombre, String apellidos, String telefono, String dirHabitual, String dirEnvio) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.dirHabitual = dirHabitual;
        this.dirEnvio = dirEnvio;
    }
}
