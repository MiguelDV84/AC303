package org.mdv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "productos")
@ToString
public class Producto implements Serializable {

    @Id
    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 100)
    @NotEmpty
    private String descripcion;

    @Column(name = "precio_recomendado", nullable = false, precision = 10, scale = 2)
    @NotEmpty
    @Min(0)
    private BigDecimal precioRecomendado;

    @Column(name = "existencias", nullable = false)
    @NotEmpty
    @Min(0)
    private int existencias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
