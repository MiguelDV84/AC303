package org.mdv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "ventas")
@ToString
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotEmpty
    private Cliente cliente;

    @Column(name = "fecha_venta", nullable = false)
    @NotEmpty
    private LocalDate fechaVenta;

    @Column(name = "importe_total", nullable = false,scale = 2)
    @NotEmpty
    private BigDecimal importeTotal;

    @OneToMany(mappedBy = "venta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaDetalle> ventaDetalles = new ArrayList<>();

    // Métodos auxiliares para mantener la relación bidireccional
    public void addVentaDetalle(VentaDetalle detalle) {
        if (detalle == null) return;
        ventaDetalles.add(detalle);
        detalle.setVenta(this);
    }

    //Borrar cuando se elimine la venta
    public void removeVentaDetalle(VentaDetalle detalle) {
        if (detalle == null) return;
        ventaDetalles.remove(detalle);
        detalle.setVenta(null);
    }
}
