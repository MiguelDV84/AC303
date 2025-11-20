package org.mdv.SQL;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VentaDetalleSQL {
    SELECT_ALL("SELECT vd FROM VentaDetalle vd");

    private final String query;
}
