package org.mdv.service;

import jakarta.persistence.EntityManagerFactory;
import org.mdv.dao.ProductoDAO;
import org.mdv.dao.VentaDAO;
import org.mdv.dto.*;
import org.mdv.model.Cliente;
import org.mdv.model.Producto;
import org.mdv.model.Venta;
import org.mdv.model.VentaDetalle;
import org.mdv.util.TransactionUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class VentaService {

    private final EntityManagerFactory emf;
    private final ClienteService clienteService;

    public VentaService(ClienteService clienteService) {
        this.clienteService = clienteService;
        this.emf = createEntityManagerFactory("unidadPersistenciaMDV");
    }

    public VentaResponse buscarPorId(int id) {
        AtomicReference<Optional<Venta>> response = new AtomicReference<>();

        TransactionUtil.doInTransaction(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);
            response.set(ventaDAO.buscarPorId(id));
        });

        return VentaResponse.fromEntity(response.get()
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la venta")));
    }

    public Optional<BigDecimal> sumTotalDia(LocalDate fecha) {
        AtomicReference<Optional<BigDecimal>> total = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);
            total.set(ventaDAO.sumaTotalDia(fecha));
        });

        return total.get();
    }

    public List<VentaResponse> buscarTodos() {
        AtomicReference<List<Venta>> response = new AtomicReference<>();

        TransactionUtil.doInSession(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);
            response.set(ventaDAO.buscarTodos());
        });

        return response.get()
                .stream()
                .map(VentaResponse::fromEntity)
                .toList();
    }

    public void guardar(VentaRequest request) {

        TransactionUtil.doInTransaction(emf, em -> {

            VentaDAO ventaDAO = new VentaDAO(em);
            ProductoDAO productoDAO = new ProductoDAO(em);

            Cliente cliente = ClienteResponse.toEntity(
                    clienteService.buscarPorDni(request.clienteDni())
            );

            List<VentaDetalle> detalles = new ArrayList<>();

            request.ventaDetalleRequests()
                    .forEach(dreq -> {
                        Producto productoBD = productoDAO.buscarPorCodigo(dreq.codProd())
                                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dreq.codProd()));

                        // VALIDAR STOCK
                        if (productoBD.getExistencias() < dreq.cantidad()) {
                            throw new RuntimeException("Stock insuficiente para " +
                                    productoBD.getDescripcion() +
                                    ". Disponible: " + productoBD.getExistencias());
                        }

                        // DESCONTAR STOCK
                        productoBD.setExistencias(productoBD.getExistencias() - dreq.cantidad());
                        productoDAO.actualizar(productoBD);

                        // CREAR DETALLE
                        VentaDetalle detalle = VentaDetalle.builder()
                                .producto(productoBD)
                                .cantidad(dreq.cantidad())
                                .precioUnitario(dreq.precioUnitario())
                                .totalLinea(dreq.totalLinea())
                                .build();

                        detalles.add(detalle);
                    });


            Venta venta = Venta.builder()
                    .cliente(cliente)
                    .fechaVenta(request.fechaVenta())
                    .importeTotal(request.importeTotal())
                    .ventaDetalles(detalles)
                    .build();

            // vincular
            detalles.forEach(x -> x.setVenta(venta));


            ventaDAO.guardar(venta);

        });
    }


    public void borrar(int id) {
        TransactionUtil.doInSession(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);
            ventaDAO.borrar(id);
        });
    }

}
