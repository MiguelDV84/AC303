package org.mdv.dao;

import jakarta.persistence.EntityManager;
import org.mdv.SQL.CategoriaSQL;
import org.mdv.model.Categoria;

import java.util.List;
import java.util.Optional;

public class CategoriaDAO {

    private final EntityManager em;

    public CategoriaDAO(EntityManager em) {
        this.em = em;
    }

    public void actualizar(Categoria categoria) {
        em.merge(categoria);
    }

    public void borrar(Long id) {
        Categoria categoria = em.find(Categoria.class,id);
        if(categoria != null){
            em.remove(categoria);
        }
    }

    public void guardar(Categoria categoria) {
        em.persist(categoria);
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return Optional.of(em.find(Categoria.class, id));
    }

    public List<Categoria> buscarPorNombre(String nombre) {
        return em.createQuery(CategoriaSQL.SELECT_BY_NAME.getQuery(), Categoria.class)
                .setParameter("nombre", "%" + nombre.toLowerCase() + "%")
                .getResultList();
    }

    public List<Categoria> buscarTodos() {
        return em.createQuery(CategoriaSQL.SELECT_ALL.getQuery(), Categoria.class)
            .getResultList();
    }
}
