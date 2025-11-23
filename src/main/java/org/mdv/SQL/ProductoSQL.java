package org.mdv.SQL;

import lombok.Getter;

@Getter
public enum ProductoSQL {
    SELECT_BY_CODIGO("SELECT p FROM Producto p JOIN FETCH p.categoria WHERE LOWER(p.codigo) = LOWER(:codigo)"),
    SELECT_ALL("SELECT p FROM Producto p JOIN FETCH p.categoria"),
    SELECT_BY_NOMBRE("SELECT p FROM Producto p JOIN FETCH p.categoria WHERE LOWER(p.descripcion) LIKE LOWER(:nombre)"),
    SELECT_ALL_LOW_STOCK("SELECT p FROM Producto p JOIN FETCH p.categoria WHERE p.existencias < :limite");

    private final String query;

    ProductoSQL(String query) {
        this.query = query;
    }
}
