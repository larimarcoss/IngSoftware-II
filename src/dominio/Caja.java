package dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Caja {
    private final String codigoCaja;
    private final double monto;              // monto inicial de apertura
    private double saldoActual;
    private LocalDateTime ultimaActualizacion;
    private final List<MovimientoDeCaja> movimientos = new ArrayList<>();  // composición

    public Caja(String codigoCaja, double montoInicial) {
        this.codigoCaja = codigoCaja;
        this.monto = montoInicial;
        this.saldoActual = montoInicial;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public void registrarMovimiento(MovimientoDeCaja movimiento) {
        double nuevoSaldo = saldoActual + movimiento.efectoSobreSaldo();
        if (nuevoSaldo < 0) {
            throw new IllegalStateException("Saldo insuficiente en caja para registrar el egreso");
        }
        movimientos.add(movimiento);
        this.saldoActual = nuevoSaldo;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public String getCodigoCaja() { return codigoCaja; }
    public double getMonto() { return monto; }
    public double getSaldoActual() { return saldoActual; }
    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public List<MovimientoDeCaja> getMovimientos() { return Collections.unmodifiableList(movimientos); }
}
