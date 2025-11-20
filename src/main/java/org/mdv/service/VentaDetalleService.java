package org.mdv.service;

import jakarta.persistence.EntityManagerFactory;
import org.mdv.dao.VentaDetalleDAO;
import org.mdv.dto.ProductoResponse;
import org.mdv.dto.VentaDetalleRequest;
import org.mdv.dto.VentaDetalleResponse;
import org.mdv.dto.VentaResponse;
import org.mdv.model.Producto;
import org.mdv.model.Venta;
import org.mdv.model.VentaDetalle;
import org.mdv.util.TransactionUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static jakarta.persistence.Persistence.createEntityManagerFactory;


public class VentaDetalleService {

    private final EntityManagerFactory emf;

    private final VentaService ventaService;
    private final ProductoService productoService;

    public VentaDetalleService(VentaService ventaService, ProductoService productoService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.emf = createEntityManagerFactory("unidadPersistenciaMDV");
    }

    public VentaDetalleResponse buscarPorId(Long id) {
        AtomicReference<Optional<VentaDetalle>> response = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO(em);
            response.set(ventaDetalleDAO.buscarPorId(id));
        });

        return VentaDetalleResponse.fromEntity(response.get()
                .orElseThrow(() -> new RuntimeException("No se ha encontrado el detalle de venta")));
    }

    public List<VentaDetalleResponse> buscarTodos() {
        AtomicReference<List<VentaDetalle>> ventaDetalles = new AtomicReference<>();
        TransactionUtil.doInSession(emf, em -> {
            VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO(em);
            ventaDetalles.set(ventaDetalleDAO.buscarTodos());
        });

        return ventaDetalles.get()
                .stream()
                .map(VentaDetalleResponse::fromEntity)
                .toList();
    }

    public void guardar(VentaDetalleRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO(em);

            Venta venta = VentaResponse.toEntity(ventaService.buscarPorId(request.ventaId()));
            Producto producto = ProductoResponse.toEntity(productoService.buscarPorCodigo(request.codProd()));

            VentaDetalle ventaDetalle = VentaDetalle.builder()
                    .cantidad(request.cantidad())
                    .precioUnitario(request.precioUnitario())
                    .totalLinea(request.totalLinea())
                    .producto(producto)
                    .venta(venta)
                    .build();

            ventaDetalleDAO.guardar(ventaDetalle);
        });
    }

    public void actualizar(VentaDetalleRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO(em);

            Venta venta = VentaResponse.toEntity(ventaService.buscarPorId(request.ventaId()));
            Producto producto = ProductoResponse.toEntity(productoService.buscarPorCodigo(request.codProd()));

            VentaDetalle ventaDetalle = VentaDetalle.builder()
                    .cantidad(request.cantidad())
                    .precioUnitario(request.precioUnitario())
                    .totalLinea(request.totalLinea())
                    .producto(producto)
                    .venta(venta)
                    .build();

            ventaDetalleDAO.actualizar(ventaDetalle);
        });
    }

    public void borrar(Long id) {
        TransactionUtil.doInSession(emf, em -> {
            VentaDetalleDAO ventaDetalleDAO = new VentaDetalleDAO(em);
            ventaDetalleDAO.borrar(id);
        });
    }
}
