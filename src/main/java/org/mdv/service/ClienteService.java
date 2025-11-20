package org.mdv.service;

import jakarta.persistence.EntityManagerFactory;
import org.mdv.dao.ClienteDAO;
import org.mdv.dto.ClienteRequest;
import org.mdv.dto.ClienteResponse;
import org.mdv.model.Cliente;
import org.mdv.util.TransactionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class ClienteService {

    private final EntityManagerFactory emf;

    public ClienteService() {
        this.emf = createEntityManagerFactory("unidadPersistenciaMDV");
    }

    public void guardar(ClienteRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            ClienteDAO clienteDAO = new ClienteDAO(em);
            Cliente nuevoCliente = Cliente.builder()
                    .dni(request.dni())
                    .nombre(request.nombre())
                    .apellidos(request.apellidos())
                    .telefono(request.telefono())
                    .dirHabitual(request.dirHabitual())
                    .dirEnvio(request.dirEnvio())
                    .build();

            clienteDAO.guardar(nuevoCliente);
        });
    }

    public void update(ClienteRequest request) {
        TransactionUtil.doInTransaction(emf, em -> {
            ClienteDAO clienteDAO = new ClienteDAO(em);
            Cliente cliente = Cliente.builder()
                    .dni(request.dni())
                    .nombre(request.nombre())
                    .apellidos(request.apellidos())
                    .telefono(request.telefono())
                    .dirHabitual(request.dirHabitual())
                    .dirEnvio(request.dirEnvio())
                    .build();

            clienteDAO.actualizar(cliente);
        });
    }

    public void delete(String dni) {
        TransactionUtil.doInTransaction(emf, em -> {
            ClienteDAO clienteDAO = new ClienteDAO(em);
            clienteDAO.borrar(dni);

        });
    }

    public ClienteResponse buscarPorDni(String dni) {
       AtomicReference<Optional<Cliente>> cliente = new AtomicReference<>();

       TransactionUtil.doInSession(emf, em -> {
           ClienteDAO clienteDAO = new ClienteDAO(em);
           cliente.set(clienteDAO.buscarPorDni(dni));
       });

        return ClienteResponse.fromEntity(cliente.get()
                .orElseThrow(() -> new RuntimeException("Cliente no Encontrado")));
    }

    public List<ClienteResponse> buscarPorNombre(String nombre) {
        AtomicReference<List<Cliente>> clientes = new AtomicReference<>(new ArrayList<>());


        TransactionUtil.doInSession(emf, em -> {
            ClienteDAO clienteDAO = new ClienteDAO(em);
            clientes.set(clienteDAO.buscarPorNombre(nombre));
        });

        return clientes.get()
                .stream()
                .map(ClienteResponse::fromEntity)
                .toList();
    }

    public List<ClienteResponse> buscarTodos() {
        AtomicReference<List<Cliente>> clientes = new AtomicReference<>(new ArrayList<>());

        TransactionUtil.doInSession(emf, em -> {
            ClienteDAO clienteDAO = new ClienteDAO(em);
            clientes.set(clienteDAO.buscarTodos());
        });

        return clientes.get()
                .stream()
                .map(ClienteResponse::fromEntity)
                .toList();

    }
}
