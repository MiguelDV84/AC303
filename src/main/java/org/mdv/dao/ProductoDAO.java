package org.mdv.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.mdv.SQL.ProductoSQL;
import org.mdv.model.Producto;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ProductoDAO {

    private final EntityManager em;


    public Optional<Producto> buscarPorCodigo(String codigo) {
        try {
            Producto prod = em.createQuery(ProductoSQL.SELECT_BY_CODIGO.getQuery(),Producto.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();

            return Optional.of(prod);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Producto> buscarTodos() {
        return em.createQuery(ProductoSQL.SELECT_ALL.getQuery(), Producto.class)
                .getResultList();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return em.createQuery(
                        ProductoSQL.SELECT_BY_NOMBRE.getQuery(), Producto.class
                )
                .setParameter("nombre", "%" + nombre + "%")
                .getResultList();
    }

    public List<Producto> buscarConStockBajo() {
        return em.createQuery(ProductoSQL.SELECT_ALL_LOW_STOCK.getQuery(),Producto.class)
                .setParameter("limite",25)
                .getResultList();
    }

    public void borrar(String cod) {
        Producto ref = em.getReference(Producto.class, cod);
        em.remove(ref);
    }

    public void guardar(Producto producto) {
        em.persist(producto);
    }

    public void actualizar(Producto producto) {
        em.merge(producto);
    }


}
