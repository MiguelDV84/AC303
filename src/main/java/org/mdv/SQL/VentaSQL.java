package org.mdv.SQL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VentaSQL {

    SELECT_ALL("SELECT DISTINCT v FROM Venta v JOIN FETCH v.cliente LEFT JOIN FETCH v.ventaDetalles d LEFT JOIN FETCH d.producto p LEFT JOIN FETCH p.categoria");

    private final String query;
}
