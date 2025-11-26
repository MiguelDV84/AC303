package org.mdv;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.mdv.dto.ClienteResponse;
import org.mdv.model.Cliente;
import org.mdv.service.ClienteService;
import org.mdv.view.App;

import java.io.File;
import java.io.IOException;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ClienteService clienteService = new ClienteService();
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        List<Cliente> listaClientes = clienteService.buscarTodos()
                .stream()
                .map(ClienteResponse::toEntity)
                .toList();

        File file = new File("listaClientes.json");

        try {
            mapper.writeValue(file, listaClientes);

            List<Cliente> clientes = mapper.readValue(
                    file,
                    new TypeReference<List<Cliente>>() {}
            );
            clientes.forEach(c -> {
                System.out.println("DNI: " + c.getDni());
                System.out.println("Nombre: " + c.getNombre());
                System.out.println("Apellidos: " + c.getApellidos());
                System.out.println("Teléfono: " + c.getTelefono());
                System.out.println("---------------");
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        App.launch(App.class, args);

    }
}