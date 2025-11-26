package org.mdv.service;

import jakarta.persistence.EntityManagerFactory;
import org.mdv.dao.CategoriaDAO;
import org.mdv.dto.CategoriaRequest;
import org.mdv.dto.CategoriaResponse;
import org.mdv.model.Categoria;
import org.mdv.util.TransactionUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class CategoriaService {

    private final EntityManagerFactory emf;

    public CategoriaService() {
        this.emf = createEntityManagerFactory("unidadPersistenciaMDV");
    }

    public void guardar(CategoriaRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {

            CategoriaDAO categoriaDAO = new CategoriaDAO(em);
            Categoria categoria = Categoria.builder()
                    .nombre(request.nombre())
                    .build();

            categoriaDAO.guardar(categoria);
        });
    }

    public void actualizar(Long id, CategoriaRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            CategoriaDAO categoriaDAO = new CategoriaDAO(em);
            Categoria categoria = categoriaDAO.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("No se encuentra la categoría"));

            categoria.setNombre(request.nombre());

            categoriaDAO.actualizar(categoria);
        });
    }

    public void borrar(Long id) {
        TransactionUtil.doInTransaction(emf, em -> {
            CategoriaDAO categoriaDAO = new CategoriaDAO(em);
            categoriaDAO.borrar(id);
        });
    }

    public CategoriaResponse buscarPorId(Long id) {
        AtomicReference<Optional<Categoria>> categoria = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            CategoriaDAO categoriaDAO = new CategoriaDAO(em);
            categoria.set(categoriaDAO.buscarPorId(id));
        });

        return CategoriaResponse.fromEntity(categoria.get()
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la categoria")));
    }

    public List<CategoriaResponse> buscarPorNombre(String nombre) {
        AtomicReference<List<Categoria>> categorias = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            CategoriaDAO categoriaDAO = new CategoriaDAO(em);
            categorias.set(categoriaDAO.buscarPorNombre(nombre));
        });

        return categorias.get()
                .stream()
                .map(CategoriaResponse::fromEntity)
                .toList();
    }

    public List<CategoriaResponse> buscarTodos() {
        AtomicReference<List<Categoria>> categorias = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            CategoriaDAO categoriaDAO = new CategoriaDAO(em);
            categorias.set(categoriaDAO.buscarTodos());
        });

        return categorias.get()
                .stream()
                .map(CategoriaResponse::fromEntity)
                .toList();
    }
}
