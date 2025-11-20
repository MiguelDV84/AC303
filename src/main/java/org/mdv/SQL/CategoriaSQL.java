package org.mdv.SQL;

import lombok.Getter;

@Getter
public enum CategoriaSQL {
    SELECT_BY_NAME("SELECT c FROM Categoria c WHERE LOWER(c.nombre) LIKE :nombre"),
    SELECT_ALL("SELECT c FROM Categoria c");

    private final String query;

    CategoriaSQL(String query) { this.query = query; }
}
