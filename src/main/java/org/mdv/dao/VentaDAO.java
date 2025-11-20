package org.mdv.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.mdv.SQL.VentaSQL;
import org.mdv.model.Venta;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class VentaDAO {

    private final EntityManager em;

    public Optional<Venta> buscarPorId(Long id) {
        return Optional.of(em.find(Venta.class, id));
    }

    public List<Venta> buscarTodos() {
        return em.createQuery(VentaSQL.SELECT_ALL.getQuery(), Venta.class)
                .getResultList();
    }

    public void guardar(Venta venta) {
        em.persist(venta);
    }

    public void actualizar(Venta venta) {
        em.merge(venta);
    }

    public void borrar(Long id) {
        Venta venta = em.find(Venta.class, id);
        if (venta != null) {
            em.remove(venta);
        }
    }
}
