package org.mdv.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.mdv.SQL.VentaDetalleSQL;
import org.mdv.model.VentaDetalle;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class VentaDetalleDAO {

    private final EntityManager em;

    public Optional<VentaDetalle> buscarPorId(Long id) {
        return Optional.of(em.find(VentaDetalle.class, id));
    }

    public List<VentaDetalle> buscarTodos() {
        return em.createQuery(VentaDetalleSQL.SELECT_ALL.getQuery(), VentaDetalle.class)
                .getResultList();
    }

    public void borrar(Long id) {
        VentaDetalle ventaDetalle = em.find(VentaDetalle.class, id);
        if(ventaDetalle != null){
            em.remove(ventaDetalle);
        }
    }

    public void guardar(VentaDetalle ventaDetalle) {
        em.persist(ventaDetalle);
    }

    public void actualizar(VentaDetalle ventaDetalle) {
        em.merge(ventaDetalle);
    }
}
