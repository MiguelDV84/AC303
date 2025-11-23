package org.mdv.dao;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.mdv.SQL.VentaSQL;
import org.mdv.model.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class VentaDAO {

    private final EntityManager em;

    public Optional<Venta> buscarPorId(int id) {
        return Optional.of(em.find(Venta.class, id));
    }

    public List<Venta> buscarTodos() {
        return em.createQuery(VentaSQL.SELECT_ALL.getQuery(), Venta.class)
                .getResultList();
    }

    public Optional<BigDecimal> sumaTotalDia(LocalDate fecha) {
        return Optional.of(em.createQuery(VentaSQL.SUM_TOTAL_DIA.getQuery(), BigDecimal.class)
                .setParameter("fecha", fecha)
                .getSingleResult());
    }

    public void guardar(Venta venta) {
        em.persist(venta);
    }

    public void actualizar(Venta venta) {
        em.merge(venta);
    }

    public void borrar(int id) {
        Venta venta = em.find(Venta.class, id);
        if (venta != null) {
            em.remove(venta);
        }
    }
}
