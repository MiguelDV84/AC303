package org.mdv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "categorias")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    @NotEmpty
    private String nombre;

    @OneToMany(
            mappedBy = "categoria",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval=true
    )
    private List<Producto> productos;

    public void addProducto(Producto producto) {
        if (producto == null) return;
        productos.add(producto);
        producto.setCategoria(this);
    }

    public void removeProducto(Producto producto) {
        if (producto == null) return;
        productos.remove(producto);
        producto.setCategoria(null);
    }

}
