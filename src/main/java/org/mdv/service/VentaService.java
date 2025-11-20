package org.mdv.service;

import jakarta.persistence.EntityManagerFactory;
import org.mdv.dao.VentaDAO;
import org.mdv.dto.*;
import org.mdv.model.Cliente;
import org.mdv.model.Venta;
import org.mdv.model.VentaDetalle;
import org.mdv.util.TransactionUtil;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class VentaService {

    private final EntityManagerFactory emf;
    private final ClienteService clienteService;
    private final VentaDetalleService ventaDetalleService;

    public VentaService(ClienteService clienteService, VentaDetalleService ventaDetalleService) {
        this.clienteService = clienteService;
        this.ventaDetalleService = ventaDetalleService;
        this.emf = createEntityManagerFactory("unidadPersistenciaMDV");
    }

    public VentaResponse buscarPorId(Long id) {
        AtomicReference<Optional<Venta>> response = new AtomicReference<>();

        TransactionUtil.doInTransaction(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);
            response.set(ventaDAO.buscarPorId(id));
        });

        return VentaResponse.fromEntity(response.get()
                .orElseThrow(() -> new RuntimeException("No se ha encontrado la venta")));
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

            Cliente cliente = ClienteResponse.toEntity(clienteService.buscarPorDni(request.clienteDni()));
            List<VentaDetalle> ventaDetalles = request.ventaDetalleRequests()
                    .stream()
                    .map(vd -> VentaDetalleResponse.toEntity(
                            ventaDetalleService.buscarPorId(vd.ventaId())))
                    .toList();

            Venta venta = Venta.builder()
                    .importeTotal(request.importeTotal())
                    .fechaVenta(request.fechaVenta())
                    .ventaDetalles(ventaDetalles)
                    .cliente(cliente)
                    .build();

            ventaDAO.guardar(venta);
        });
    }

    public void actualizar(VentaRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);

            Cliente cliente = ClienteResponse.toEntity(clienteService.buscarPorDni(request.clienteDni()));
            List<VentaDetalle> ventaDetalles = request.ventaDetalleRequests()
                    .stream()
                    .map(vd -> VentaDetalleResponse.toEntity(
                            ventaDetalleService.buscarPorId(vd.ventaId())))
                    .toList();

            Venta venta = Venta.builder()
                    .importeTotal(request.importeTotal())
                    .fechaVenta(request.fechaVenta())
                    .ventaDetalles(ventaDetalles)
                    .cliente(cliente)
                    .build();

            ventaDAO.actualizar(venta);
        });
    }

    public void borrar(Long id) {
        TransactionUtil.doInSession(emf, em -> {
            VentaDAO ventaDAO = new VentaDAO(em);
            ventaDAO.borrar(id);
        });
    }

}
