package org.mdv.dao;

import jakarta.persistence.EntityManager;
import org.mdv.SQL.ClienteSQL;
import org.mdv.model.Cliente;

import java.util.List;
import java.util.Optional;

public class ClienteDAO {

    private final EntityManager em;

    public ClienteDAO(EntityManager em) {
        this.em = em;
    }

    public void guardar(Cliente cliente) {
        em.persist(cliente);
    }

    public List<Cliente> buscarTodos() {
        return em.createQuery(ClienteSQL.SELECT_ALL.getQuery(), Cliente.class)
                 .getResultList();
    }

    public void borrar(String dni) {
        Cliente ref = em.getReference(Cliente.class, dni);
        em.remove(ref);
    }

    public void actualizar(Cliente cliente) {
        em.merge(cliente);
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return Optional.of(em.createQuery(ClienteSQL.SELECT_BY_DNI.getQuery(), Cliente.class)
                 .setParameter("dni", dni)
                 .getSingleResult());
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return em.createQuery(ClienteSQL.SELECT_BY_NOMBRE.getQuery(), Cliente.class)
                .setParameter("nombre", "%" + nombre.toLowerCase() + "%")
                .getResultList();
    }
}
