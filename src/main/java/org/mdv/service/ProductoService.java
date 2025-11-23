package org.mdv.service;

import jakarta.persistence.EntityManagerFactory;
import org.mdv.dao.ProductoDAO;
import org.mdv.dto.CategoriaResponse;
import org.mdv.dto.ProductoRequest;
import org.mdv.dto.ProductoResponse;
import org.mdv.model.Categoria;
import org.mdv.model.Producto;
import org.mdv.util.TransactionUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static jakarta.persistence.Persistence.createEntityManagerFactory;


public class ProductoService {

    private final EntityManagerFactory emf;
    private final CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService1) {
        this.categoriaService = categoriaService1;
        this.emf = createEntityManagerFactory("unidadPersistenciaMDV");
    }

    public ProductoResponse buscarPorCodigo(String codigo) {

        AtomicReference<Optional<Producto>> response = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            ProductoDAO dao = new ProductoDAO(em);
            response.set(dao.buscarPorCodigo(codigo));
        });

        return response.get()
                .map(ProductoResponse::fromEntity)
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el producto."));
    }


    public List<ProductoResponse> buscarTodos() {
        AtomicReference<List<Producto>> response = new AtomicReference<>();
        TransactionUtil.doInSession(emf, em -> {
            ProductoDAO productoDAO = new ProductoDAO(em);
            response.set(productoDAO.buscarTodos());
        });

        return response.get()
                .stream()
                .map(ProductoResponse::fromEntity)
                .toList();
    }

    public List<ProductoResponse> buscarPorNombre(String nombre) {
        AtomicReference<List<Producto>> response = new AtomicReference<>();
        TransactionUtil.doInSession(emf, em -> {
            ProductoDAO productoDAO = new ProductoDAO(em);
            response.set(productoDAO.buscarPorNombre(nombre));
        });

        return response.get()
                .stream()
                .map(ProductoResponse::fromEntity)
                .toList();
    }

    public List<ProductoResponse> buscarConStockBajo() {
        AtomicReference<List<Producto>> response = new AtomicReference<>();
        TransactionUtil.doInSession(emf, em -> {
            ProductoDAO productoDAO = new ProductoDAO(em);
            response.set(productoDAO.buscarConStockBajo());
        });

        return response.get()
                .stream()
                .map(ProductoResponse::fromEntity)
                .toList();
    }

    public void guardar(ProductoRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            ProductoDAO productoDAO = new ProductoDAO(em);

            Categoria categoria = CategoriaResponse.toEntity(categoriaService.buscarPorId(request.categoriaId()));

            Producto productoNuevo = Producto.builder()
                    .codigo(request.codigo())
                    .descripcion(request.descripcion())
                    .precioRecomendado(request.precioRecomendado())
                    .existencias(request.existencias())
                    .categoria(categoria)
                    .build();

            productoDAO.guardar(productoNuevo);
        });
    }

    public void borrar(String cod) {
        TransactionUtil.doInTransaction(emf, em -> {
            ProductoDAO productoDAO = new ProductoDAO(em);
            productoDAO.borrar(cod);
        });
    }

    public void actualizar(ProductoRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            ProductoDAO productoDAO = new ProductoDAO(em);
            Producto producto = productoDAO.buscarPorCodigo(request.codigo()).orElseThrow(
                    () -> new RuntimeException("No se ha encontrado el producto"));

            Categoria categoria = CategoriaResponse.toEntity(categoriaService.buscarPorId(request.categoriaId()));

            producto.setCodigo(request.codigo());
            producto.setDescripcion(request.descripcion());
            producto.setPrecioRecomendado(request.precioRecomendado());
            producto.setExistencias(request.existencias());
            producto.setCategoria(categoria);


            productoDAO.actualizar(producto);
        });
    }

}
