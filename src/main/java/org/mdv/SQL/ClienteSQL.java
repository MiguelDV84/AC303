package org.mdv.SQL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClienteSQL {
    SELECT_ALL("SELECT c FROM Cliente c"),
    SELECT_BY_DNI("SELECT c FROM Cliente c WHERE c.dni = :dni"),
    SELECT_BY_NOMBRE("SELECT c FROM Cliente c WHERE LOWER(c.nombre) LIKE :nombre");

    private final String query;

}
