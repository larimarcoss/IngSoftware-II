package servicio;

import dominio.Caja;
import dominio.Egreso;
import dominio.Ingreso;
import dominio.MovimientoDeCaja;
import util.GeneradorCodigo;

import java.util.List;

public class GestorCaja {
    private final Caja caja;

    public GestorCaja(Caja caja) {
        this.caja = caja;
    }

    // CU12: Registrar movimiento de caja (ingreso manual, fuera de un pago de reserva)
    public void registrarIngreso(double monto, String descripcion, String medioDePago) {
        Ingreso ingreso = new Ingreso(GeneradorCodigo.siguiente("MOV"), monto, descripcion, medioDePago);
        caja.registrarMovimiento(ingreso);
    }

    // CU12: Registrar movimiento de caja (egreso manual)
    public void registrarEgreso(double monto, String descripcion, String motivo) {
        Egreso egreso = new Egreso(GeneradorCodigo.siguiente("MOV"), monto, descripcion, motivo);
        caja.registrarMovimiento(egreso);
    }

    // CU13: Consultar movimientos de caja
    public List<MovimientoDeCaja> listarMovimientos() {
        return caja.getMovimientos();
    }

    public double obtenerSaldoActual() {
        return caja.getSaldoActual();
    }

    public String obtenerCodigoCaja() {
        return caja.getCodigoCaja();
    }
}
